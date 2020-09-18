package de.education.parallelurlreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class NodeProcessor {

	private String url;
	private Map<String, Integer> foundUrls = new ConcurrentHashMap<>();

	private final Logger logger = LoggerFactory.getLogger(NodeProcessor.class);

	public NodeProcessor(String key, String url, Integer depth, Integer maxDepth) {

		this.url = url;

		if (depth <= maxDepth) {

			this.foundUrls = this.findUrls(url);

			List<Future<NodeProcessor>> childNodes = new ArrayList<>();

			foundUrls.keySet().forEach(aUrl -> {
				childNodes.add(CompletableFuture.supplyAsync(() -> {
					return new NodeProcessor("", aUrl, depth + 1, maxDepth);
				}));
			});

			childNodes.forEach(childNode -> {
				try {
					this.collectUrls(childNode.get());
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			});
		}
	}

	private Map<String, Integer> findUrls(String url) {

		RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
		ResponseEntity<String> htmlResponse = null;
		try {
			logger.debug("Visiting " + url);
			htmlResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		} catch (Exception e) {
			logger.warn(e.toString());
		}

		Map<String, Integer> foundUrls = new ConcurrentHashMap<>();
		if (htmlResponse != null && htmlResponse.getStatusCode().is2xxSuccessful()) {

			String htmlContent = htmlResponse.getBody();

			List<String> uncleanHrefs = Arrays.asList(htmlContent.split("href=\""));
			List<String> urls = uncleanHrefs.stream()
					.map(href -> href.substring(0, href.indexOf("\"") > 0 ? href.indexOf("\"") : 0))
					.filter(href -> href.startsWith("http")).collect(Collectors.toList());

			urls.add(url);
			urls.forEach(foundUrl -> {
				foundUrls.merge(foundUrl, 1, (v1, v2) -> v1 + v2);
			});
		}

		return foundUrls;
	}

	private void collectUrls(NodeProcessor childNode) {

		childNode.getFoundUrls().entrySet().forEach(entry -> {
			this.foundUrls.merge(entry.getKey(), 1, (v1, v2) -> v1 + v2);
		});
	}

	public String getUrl() {
		return url;
	}

	public Map<String, Integer> getFoundUrls() {
		return this.foundUrls;
	}
}
