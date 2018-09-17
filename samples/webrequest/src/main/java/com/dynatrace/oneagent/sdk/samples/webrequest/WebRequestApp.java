package com.dynatrace.oneagent.sdk.samples.webrequest;

import java.util.List;
import java.util.Map.Entry;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.OutgoingWebRequestTracer;

/**
 * Sample application shows how incoming webrequests should be traced.
 * 
 * @author Alram.Lechner
 *
 */
public class WebRequestApp {

	private final OneAgentSDK oneAgentSdk;
	final FakedWebserver webServer;
	
	static WebRequestApp instance;
	
	
	private WebRequestApp() {
		oneAgentSdk = OneAgentSDKFactory.createInstance();
		oneAgentSdk.setLoggingCallback(new StdErrLoggingCallback());
		switch (oneAgentSdk.getCurrentState()) {
		case ACTIVE:
			System.out.println("SDK is active and capturing.");
			break;
		case PERMANENTLY_INACTIVE:
			System.err.println(
					"SDK is PERMANENT_INACTIVE; Probably no OneAgent injected or OneAgent is incompatible with SDK.");
			break;
		case TEMPORARILY_INACTIVE:
			System.err.println(
					"SDK is TEMPORARY_INACTIVE; OneAgent has been deactivated - check OneAgent configuration.");
			break;
		default:
			System.err.println("SDK is in unknown state.");
			break;
		}
		webServer = new FakedWebserver(oneAgentSdk);
		instance = this;
	}

	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**            Running webrequest sample                    **");
		System.out.println("*************************************************************");
		try {
			WebRequestApp app = new WebRequestApp();
			app.runFakedWebrequest();
			// app.runIncomingWebrequest();
			System.out.println("sample application stopped. sleeping a while, so OneAgent is able to send data to server ...");
			Thread.sleep(15000 * 3); // we have to wait - so OneAgent is able to send data to server
		} catch (Exception e) {
			System.err.println("webrequest sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void runFakedWebrequest() {
		String url = "http://localhost:80/billing/my/path?param1=value1&param2=value2";
		System.out.println("[Client] request " + url);
		OutgoingWebRequestTracer outgoingWebRequestTracer = oneAgentSdk.traceOutgoingWebRequest(url, "GET");
		outgoingWebRequestTracer.start();
		try {
			FakedHttpClient httpClient = new FakedHttpClient(url, "GET");
			
			// add link to headers ...
			httpClient.addRequestHeader(OneAgentSDK.DYNATRACE_HTTP_HEADERNAME, outgoingWebRequestTracer.getDynatraceStringTag());

			httpClient.executeRequest();
			
			// add response headers to tracer:
			for (Entry<String, List<String>> entry : httpClient.getResponseHeaders().entrySet()) {
				for (String value : entry.getValue()) {
					outgoingWebRequestTracer.addResponseHeader(entry.getKey(), value);
				}
			}
			
			outgoingWebRequestTracer.setStatusCode(httpClient.getStatusCode());
		} catch (Exception e) {
			outgoingWebRequestTracer.error(e);
		} finally {
			outgoingWebRequestTracer.end();
		}
	}
	
}
