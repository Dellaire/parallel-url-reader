package de.education.parallelurlreader;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/urls")
public class UrlController {

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public UrlCountSummary getUrlsAsJson(@RequestParam String rootUrl, @RequestParam Integer depth)
			throws InterruptedException, ExecutionException {

		NodeProcessor nodeProcessor = new NodeProcessor(rootUrl, 0, depth);

		return new UrlCountSummary(nodeProcessor);
	}

	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public byte[] getUrlsAsFile(@RequestParam String rootUrl, @RequestParam Integer depth) throws Exception {

		UrlCountSummary summary = new UrlCountSummary(new NodeProcessor(rootUrl, 0, depth));

		return this.objectMapper.writeValueAsBytes(summary);
	}
}
