package de.education.parallelurlreader.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.education.parallelurlreader.urlreader.NodeProcessor;

@RestController
@RequestMapping("/urls")
public class UrlController {

	@GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
	public String findSubUrls(@RequestParam String rootUrl, @RequestParam Integer depth)
			throws InterruptedException, ExecutionException {

		NodeProcessor nodeProcessor = new NodeProcessor("root", rootUrl, 0, depth);

		String resultString = nodeProcessor.getFoundUrls().entrySet().stream().map(es -> es.getKey() + " --- " + es.getValue() + "\n")
				.reduce("", (s1, s2) -> s1 + s2 + "");
		
		return resultString;
	}
}
