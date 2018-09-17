package com.dynatrace.oneagent.sdk.samples.webrequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.dynatrace.oneagent.sdk.api.IncomingWebRequestTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.infos.WebApplicationInfo;

public class FakedWebserver {

	private final OneAgentSDK oneAgentSDK;
	private final WebApplicationInfo webAppInfo;
	
	private final BlockingQueue<Pair> requestQueue = new ArrayBlockingQueue<Pair>(10);
	
	public FakedWebserver(OneAgentSDK oneAgentSDK) {
		this.oneAgentSDK = oneAgentSDK;
		webAppInfo = oneAgentSDK.createWebApplicationInfo("servername", "BillingService", "/billing");
		new RequestProcessor().start();
	}
	
	private class RequestProcessor extends Thread {
		private RequestProcessor() {
			super("Webserver-Worker");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			while (true) {
				Pair incomingRequest;
				try {
					incomingRequest = requestQueue.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				serve(incomingRequest.httpRequest, incomingRequest.httpResponse);
			}
		}
	}
	
	private class Pair {
		public HttpRequest httpRequest;
		public HttpResponse httpResponse;
		
		public Pair(HttpRequest httpRequest, HttpResponse httpResponse) {
			this.httpRequest = httpRequest;
			this.httpResponse = httpResponse;
		}
	}
	
	public void enqeueHttpRequestForProcessing(HttpRequest httpRequest, HttpResponse httpResponse) {
		try {
			requestQueue.put(new Pair(httpRequest,httpResponse));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static interface HttpResponse {

		public void setStatusCode(int statusCode);
		
		public void addResponseHeader(String headerField, String value);
		
		public void setContent(byte[] content);
	}
	
	public static class HttpRequest {
		private final String remoteIpAddress;
		private final String uri;
		private final String method;
		private final Map<String, List<String>> requestHeaders;
		
		public HttpRequest(String uri, String method, String remoteIpAddress, Map<String, List<String>> requestHeaders) {
			this.uri = uri;
			this.method = method;
			this.remoteIpAddress = remoteIpAddress;
			this.requestHeaders = requestHeaders;
		}
		
		public String getUri() {
			return uri;
		}

		public String getMethod() {
			return method;
		}

		public Map<String, List<String>> getHeaders() {
			return requestHeaders;
		}

		public Map<String, List<String>> getParameters() {
			return new HashMap<String, List<String>>();
		}

		public String getRemoteIpAddress() {
			return remoteIpAddress;
		}
	}

	/** faked http request handling. shows usage of OneAgent SDK's incoming webrequest API */
	private void serve(HttpRequest request, HttpResponse response) {
		String url = request.getUri();
		System.out.println("[Server] serve " + url);
		IncomingWebRequestTracer incomingWebrequestTracer = oneAgentSDK.traceIncomingWebRequest(webAppInfo, url, request.getMethod());

		// add request header, parameter and remote address before start:
		for (Entry<String, List<String>> headerField : request.getHeaders().entrySet()) {
			for (String value : headerField.getValue()) {
				incomingWebrequestTracer.addRequestHeader(headerField.getKey(), value);
			}
		}
		for (Entry<String, List<String>> parameter : request.getParameters().entrySet()) {
			for (String value : parameter.getValue()) {
				incomingWebrequestTracer.addParameter(parameter.getKey(), value);
			}
		}
		incomingWebrequestTracer.setRemoteAddress(request.getRemoteIpAddress());

		incomingWebrequestTracer.start();
		try {
			response.setContent("Hello world!".getBytes());
			response.setStatusCode(200);
			incomingWebrequestTracer.setStatusCode(200);
		} catch (Exception e) {
			// we assume, container is sending http 500 in case of exception is thrown while serving an request:
			incomingWebrequestTracer.error(e);
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			incomingWebrequestTracer.end();
		}
	}

	
}
