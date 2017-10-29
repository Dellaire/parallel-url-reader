package de.education.parallelurlreader.urlreader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

/**
 * The {@code SecondLevelUrlReadRunnable} calls the specified URL, collects all
 * URLs from the retrieved resource (called 'second level URLs') and updates the
 * URL count. The {@code SecondLevelUrlReadRunnable} is made to be called within
 * a new thread.
 */
public class SecondLevelUrlReadRunnable implements Runnable
{
	final String urlToBeCalled;
	final Consumer<String> updateUrlCounts;

	public SecondLevelUrlReadRunnable(String urlToBeCalled, Consumer<String> updateUrlCounts)
	{
		this.urlToBeCalled = Objects.requireNonNull(urlToBeCalled);
		this.updateUrlCounts = Objects.requireNonNull(updateUrlCounts);
	}

	@Override
	public void run()
	{
		RestTemplate restTemplate = new RestTemplate();

		List<String> secondLevelUrls = new ArrayList<String>();
		try
		{
			String content = restTemplate.exchange(this.urlToBeCalled, HttpMethod.GET, HttpEntity.EMPTY, String.class)
					.getBody();
			secondLevelUrls = UrlExtractor.extractUrls(content);
		} catch (Exception e)
		{
			System.out.println(this.urlToBeCalled + ": " + e);
		}

		secondLevelUrls.forEach(url -> this.updateUrlCounts.accept(url));
	}
}
