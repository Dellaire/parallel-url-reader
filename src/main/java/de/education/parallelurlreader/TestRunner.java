package de.education.parallelurlreader;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class TestRunner implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {

		Map<String, Long> map1 = new HashMap<>();
		map1.put("one", 1L);
		map1.put("two", 2L);
		
		Map<String, Long> map2 = new HashMap<>();
		map2.put("one", 1L);
		map2.put("three", 3L);
		
		map1.merge("one", 1L, (m1, m2) -> m1 + m2);
		
		System.out.println();
	}
}
