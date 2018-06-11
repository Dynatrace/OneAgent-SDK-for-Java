package com.dynatrace.oneagent.sdk.samples.webrequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.dynatrace.oneagent.sdk.api.IncomingWebRequestTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.infos.WebApplicationInfo;

public class FakedWebserver {

	private final OneAgentSDK oneAgentSDK;
	private final WebApplicationInfo webAppInfo;
	
	public FakedWebserver(OneAgentSDK oneAgentSDK) {
		this.oneAgentSDK = oneAgentSDK;
		webAppInfo = oneAgentSDK.createWebApplicationInfo("servername", "BillingService", "/billing");
	}
	
	public static class HttpResponse {

		public void setStatus(int i) {
			
		}
		
	}
	
	public static class HttpRequest {
		private final String remoteHostName;
		private final String uri;
		private final String method;
		
		public HttpRequest(String uri, String method, String remoteHostName) {
			this.uri = uri;
			this.method = method;
			this.remoteHostName = remoteHostName;
		}
		
		public String getUri() {
			return uri;
		}

		public String getMethod() {
			return method;
		}

		public Map<String, String> getHeaders() {
			Map<String, String> receivedHeaders = new HashMap<String, String>();
			// receivedHeaders.put("x-Dynatrace", "");
			return receivedHeaders;
		}

		public Map<String, List<String>> getParameters() {
			return new HashMap<String, List<String>>();
		}

		public String getRemoteHostName() {
			return remoteHostName;
		}
		
		
	}

	/** faked http request handling. shows usage of OneAgent SDK's incoming webrequest API */
	public HttpResponse serve(HttpRequest session) {
		String url = session.getUri();

		IncomingWebRequestTracer incomingWebrequestTracer = oneAgentSDK.traceIncomingWebRequest(webAppInfo, url, session.getMethod());

		// add request header, parameter and remote address before start:
		for (Entry<String, String> headerField : session.getHeaders().entrySet()) {
			incomingWebrequestTracer.addRequestHeader(headerField.getKey(), headerField.getValue());
		}
		for (Entry<String, List<String>> headerField : session.getParameters().entrySet()) {
			for (String value : headerField.getValue()) {
				incomingWebrequestTracer.addParameter(headerField.getKey(), value);
			}
		}
		incomingWebrequestTracer.setRemoteAddress(session.getRemoteHostName());

		incomingWebrequestTracer.start();
		HttpResponse response = new HttpResponse();
		try {
			response.setStatus(200);
			incomingWebrequestTracer.setStatusCode(200);
			return response;
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
