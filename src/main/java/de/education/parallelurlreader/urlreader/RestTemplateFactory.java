package de.education.parallelurlreader.urlreader;

import java.time.Duration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class RestTemplateFactory {

	public static RestTemplate createRestTemplate() {
		
		return new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(3000))
				.setReadTimeout(Duration.ofMillis(3000)).build();
	}
}
