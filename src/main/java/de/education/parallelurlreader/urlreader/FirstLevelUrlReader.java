package de.education.parallelurlreader.urlreader;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * The {@code FirstLevelUrlReader} calls the specified root URL and delegates
 * the calls of the found URLs to a {@code SecondLevelUrlReadRunnable} each.
 */
@Component
public class FirstLevelUrlReader
{
	@Value("${read.timeout.total.seconds}")
	private Long totalReadTimeoutInSeconds;

	private ConcurrentMap<String, Long> urlCounts = new ConcurrentHashMap<String, Long>();

	private final RestTemplate restTemplate;

	@Autowired
	public FirstLevelUrlReader(RestTemplateBuilder restTemplateBuilder)
	{
		this.restTemplate = restTemplateBuilder.build();
	}

	public ConcurrentMap<String, Long> readUrls(String url)
	{
		String content = this.restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class).getBody();

		List<String> firstLevelUrls = UrlExtractor.extractUrls(content);
		firstLevelUrls.forEach(firstLevelUrl -> this.updateUrlCounts(firstLevelUrl));

		ExecutorService executor = Executors.newWorkStealingPool();
		for (String firstLevelUrl : firstLevelUrls)
		{
			executor.submit(new SecondLevelUrlReadRunnable(firstLevelUrl,
					secondLevelUrl -> this.updateUrlCounts(secondLevelUrl)));
		}

		try
		{
			executor.shutdown();
			executor.awaitTermination(totalReadTimeoutInSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return this.urlCounts;
	}

	private void updateUrlCounts(String url)
	{
		this.urlCounts.merge(url, 1L, (oldCount, newCount) -> oldCount + 1);
	}
}
