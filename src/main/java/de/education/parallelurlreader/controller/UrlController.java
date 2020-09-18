package de.education.parallelurlreader.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.education.parallelurlreader.UrlCountSummary;
import de.education.parallelurlreader.urlreader.NodeProcessor;

@RestController
@RequestMapping("/urls")
public class UrlController {

	@GetMapping
	public UrlCountSummary findSubUrls(@RequestParam String rootUrl, @RequestParam Integer depth)
			throws InterruptedException, ExecutionException {

		NodeProcessor nodeProcessor = new NodeProcessor("root", rootUrl, 0, depth);

		return new UrlCountSummary(nodeProcessor);
	}
}
