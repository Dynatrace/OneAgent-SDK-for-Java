package com.dynatrace.oneagent.sdk.samples.inprocesslinking;

import com.dynatrace.oneagent.sdk.api.InProcessLink;

public class UrlDownloadItem {

	public static final UrlDownloadItem END = new UrlDownloadItem("END", null);

	// async operation - eg. URL to download
	private String url;

	// trace to link async execution with dynatrace
	private InProcessLink link;

	public UrlDownloadItem(String url, InProcessLink link) {
		this.url = url;
		this.link = link;
	}
	
	public String getUrl() {
		return url;
	}
	
	public InProcessLink getLink() {
		return link;
	}
}
