package de.education.parallelurlreader;

import java.util.concurrent.ExecutionException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/urls")
public class UrlController {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public UrlCountSummary getUrlsAsJson(@RequestParam String rootUrl, @RequestParam Integer depth)
			throws InterruptedException, ExecutionException {

		NodeProcessor nodeProcessor = new NodeProcessor("root", rootUrl, 0, depth);

		return new UrlCountSummary(nodeProcessor);
	}
}
