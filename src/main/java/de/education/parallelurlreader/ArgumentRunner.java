package de.education.parallelurlreader;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import de.education.parallelurlreader.persistence.UrlCountsWriter;
import de.education.parallelurlreader.urlreader.FirstLevelUrlReader;

/**
 * Though, the {@code ArgumentRunner} will always be created within the
 * application context, its method {@code ArgumentRunner#run(String...)} will
 * only call the {@code FirstLevelUrlReader}, if the application was started
 * with an argument. This argument will be interpreted as URL to be called.
 */
@Component
public class ArgumentRunner implements CommandLineRunner
{
	private final FirstLevelUrlReader urlReader;
	private final UrlCountsWriter urlCountsWriter;

	public ArgumentRunner(FirstLevelUrlReader urlReader, UrlCountsWriter urlCountsWriter)
	{
		this.urlReader = Objects.requireNonNull(urlReader);
		this.urlCountsWriter = Objects.requireNonNull(urlCountsWriter);
	}

	@Override
	public void run(String... arguments) throws Exception
	{
		Optional<String> urlToRead = Arrays.asList(arguments).stream().filter(argument -> !argument.startsWith("--"))
				.findFirst();

		if (urlToRead.isPresent())
		{
			this.urlCountsWriter.writeUrlCounts(this.urlReader.readUrls(urlToRead.get()));
			System.out.println("DONE");
		}
	}
}
