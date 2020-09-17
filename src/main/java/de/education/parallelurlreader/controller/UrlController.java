package de.education.parallelurlreader.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.education.parallelurlreader.urlreader.NodeProcessor;

@RestController
@RequestMapping("/urls")
public class UrlController {

	@GetMapping
	public String findSubUrls(@RequestParam String rootUrl, @RequestParam Integer depth)
			throws InterruptedException, ExecutionException {

		NodeProcessor nodeProcessor = NodeProcessor.create(rootUrl, np -> {
		}, 0, depth).get();

		return nodeProcessor.getFoundUrls().entrySet().stream().map(es -> es.getKey() + " --- " + es.getValue() + "\n")
				.reduce("", (s1, s2) -> s1 + s2 + "");
	}
}
