package com.dynatrace.oneagent.sdk.samples.webrequest;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dynatrace.oneagent.sdk.samples.webrequest.FakedWebserver.HttpRequest;
import com.dynatrace.oneagent.sdk.samples.webrequest.FakedWebserver.HttpResponse;

public class FakedHttpClient implements HttpResponse {

	// REQUEST
	private URL url;
	private String method;
	private Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();

	// RESPONSE
	private int statusCode;
	private Map<String, List<String>> responseHeaders = new HashMap<String, List<String>>();
	
	public FakedHttpClient(String url, String method) throws MalformedURLException {
		this.method = method;
		this.url = new URL(url);
	}

	public void addRequestHeader(String requestHeader, String value) {
		List<String> values = requestHeaders.get(requestHeader);
		if (values == null) {
			values = new ArrayList<String>();
			requestHeaders.put(requestHeader, values);
		}
		values.add(value);
	}
	
	public void addResponseHeader(String headerField, String value) {
		List<String> values = responseHeaders.get(headerField);
		if (values == null) {
			values = new ArrayList<String>();
			responseHeaders.put(headerField, values);
		}
		values.add(value);
	}

	public void executeRequest() {
		// a normal HTTP client should open plain tcp socket and send the request now ...
		// ... but we are putting them into non-blocking queue:

		// build the request object, as a standard web container would provide it ...
		HttpRequest httpRequest;
		String clientIp;
		try {
			clientIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			clientIp = "192.168.4.5"; // fake IP
		}
		httpRequest = new HttpRequest(url.getPath() + "?" + url.getQuery(), method, clientIp, requestHeaders);
		
		// ... and queue it for processing: 
		WebRequestApp.instance.webServer.enqeueHttpRequestForProcessing(httpRequest, this);
	}

	public Map<String, List<String>> getRequestHeaders() {
		return requestHeaders;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	@Override
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public void setContent(byte[] content) {
		// ignore the content we got from server
	}

}
