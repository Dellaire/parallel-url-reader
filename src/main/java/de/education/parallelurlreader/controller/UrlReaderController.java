package de.education.parallelurlreader.controller;

import java.io.File;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.education.parallelurlreader.persistence.UrlCountsWriter;
import de.education.parallelurlreader.urlreader.FirstLevelUrlReader;

/**
 * The {@code UrlReaderController} provides HTTP endpoints for calling the
 * {@code FirstLevelUrlReader}.
 */
@RestController
@RequestMapping(value = "/url")
public class UrlReaderController
{
	@Value("${url.counts.file.name}")
	private String fileName;

	private final FirstLevelUrlReader urlReader;
	private final UrlCountsWriter urlCountsWriter;

	public UrlReaderController(FirstLevelUrlReader urlReader, UrlCountsWriter urlCountsWriter)
	{
		this.urlReader = Objects.requireNonNull(urlReader);
		this.urlCountsWriter = Objects.requireNonNull(urlCountsWriter);
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> readUrls(@RequestParam String url)
	{
		return new ResponseEntity<String>(this.urlReader.readUrls(url).toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public ResponseEntity<Resource> getImageAsResource(@RequestParam String url)
	{
		this.urlCountsWriter.writeUrlCounts(this.urlReader.readUrls(url));

		Resource resource = new FileSystemResource(new File(this.fileName));

		return new ResponseEntity<Resource>(resource, new HttpHeaders(), HttpStatus.OK);
	}
}
