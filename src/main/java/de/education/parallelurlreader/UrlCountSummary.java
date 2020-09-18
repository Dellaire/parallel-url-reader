package de.education.parallelurlreader;

import java.util.ArrayList;
import java.util.List;

public class UrlCountSummary {

	private List<UrlCount> urlCounts = new ArrayList<>();

	public UrlCountSummary(NodeProcessor nodeProcessor) {

		nodeProcessor.getFoundUrls().forEach((url, count) -> {
			urlCounts.add(new UrlCount().setUrl(url).setCount(count));
		});
	}

	public List<UrlCount> getUrlCounts() {
		return urlCounts;
	}

	public void setUrlCounts(List<UrlCount> urlCounts) {
		this.urlCounts = urlCounts;
	}

	class UrlCount {

		private String url;
		private Integer count;

		public String getUrl() {
			return url;
		}

		public UrlCount setUrl(String url) {
			this.url = url;
			return this;
		}

		public Integer getCount() {
			return count;
		}

		public UrlCount setCount(Integer count) {
			this.count = count;
			return this;
		}
	}
}
