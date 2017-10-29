package de.education.parallelurlreader.persistence;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The {@code UrlCountsWriter} writes the formatted occurrence of the called
 * URLs in a file.
 */
@Component
public class UrlCountsWriter
{
	@Value("${url.counts.file.name}")
	private String fileName;

	public void writeUrlCounts(ConcurrentMap<String, Long> urlCounts)
	{
		try
		{
			Path path = Paths.get(this.fileName);
			try (BufferedWriter writer = Files.newBufferedWriter(path))
			{
				writer.write(urlCounts.toString().replace(", ", "\n").replace("{", "").replace("}", ""));
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
