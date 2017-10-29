package de.education.parallelurlreader.urlreader;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code UrlExtractor} parses a resource for containing URLs and retrieves
 * them.
 */
public class UrlExtractor
{
	public static List<String> extractUrls(String content)
	{
		List<String> chunksWithUrl = Arrays.asList(content.split("href=\"")).stream().map(chunk -> chunk.trim())
				.collect(Collectors.toList());
		chunksWithUrl.remove(0);

		List<String> urls = new ArrayList<String>();
		for (String chunkWithUrl : chunksWithUrl)
		{
			int positionOfClosingTag = chunkWithUrl.indexOf("\"");
			urls.add(chunkWithUrl.substring(0, positionOfClosingTag));
		}

		return urls.stream().filter(url -> urlIsValid(url)).collect(Collectors.toList());
	}

	private static Boolean urlIsValid(String url)
	{
		try
		{
			URI uri = new URI(url);

			return uri.isAbsolute() && (uri.getScheme().equals("http") || uri.getScheme().equals("https"));
		} catch (URISyntaxException e)
		{
			return false;
		}
	}
}
