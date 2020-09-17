package de.education.parallelurlreader.urlreader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class NodeProcessor {

	private String url;
	private Consumer<NodeProcessor> finished;
	private Map<String, Long> foundUrls = new ConcurrentHashMap<>();
	private List<NodeProcessor> childNodes = new ArrayList<>();;

	private final Logger logger = LoggerFactory.getLogger(NodeProcessor.class);

	public NodeProcessor(String url, Consumer<NodeProcessor> finishedNotification, Integer depth, Integer maxDepth) {

		this.url = url;
		this.finished = finishedNotification;

		if (depth <= maxDepth) {

			this.foundUrls = this.findUrls(url);

			foundUrls.keySet().forEach(aUrl -> {
				this.childNodes.add(NodeProcessor.create(aUrl, this::collectUrls, depth + 1, maxDepth));
			});

		} else {
			this.finished.accept(this);
		}
	}

	public static Future<NodeProcessor> create(String url, Consumer<NodeProcessor> finishedNotification, Integer depth,
			Integer maxDepth) {

		return CompletableFuture.supplyAsync(() -> new NodeProcessor(url, finishedNotification, depth, maxDepth));
	}

	private Map<String, Long> findUrls(String url) {

		RestTemplate restTemplate = RestTemplateFactory.createRestTemplate();
		ResponseEntity<String> htmlResponse = null;
		try {
			logger.info("Visiting " + url);
			htmlResponse = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		Map<String, Long> foundUrls = new ConcurrentHashMap<>();
		if (htmlResponse != null && htmlResponse.getStatusCode().is2xxSuccessful()) {

			String htmlContent = htmlResponse.getBody();

			List<String> uncleanHrefs = Arrays.asList(htmlContent.split("href=\""));
			List<String> urls = uncleanHrefs.stream()
					.map(href -> href.substring(0, href.indexOf("\"") > 0 ? href.indexOf("\"") : 0))
					.collect(Collectors.toList());

			urls.add(url);
			urls.forEach(foundUrl -> {
				foundUrls.merge(foundUrl, 1L, (v1, v2) -> v1 + v2);
			});
		}

		return foundUrls;
	}

	private void collectUrls(NodeProcessor childNode) {

		childNode.getFoundUrls().entrySet().forEach(entry -> {
			this.foundUrls.merge(entry.getKey(), 1L, (v1, v2) -> v1 + v2);
		});

		this.childNodes.remove(childNode);
		if (this.childNodes.isEmpty()) {
			this.finished.accept(this);
		}
	}

	public String getUrl() {
		return url;
	}

	public Map<String, Long> getFoundUrls() {
		return this.foundUrls;
	}
}
