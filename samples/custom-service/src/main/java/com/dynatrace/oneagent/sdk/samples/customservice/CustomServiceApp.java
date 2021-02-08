package com.dynatrace.oneagent.sdk.samples.customservice;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.CustomServiceTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

/**
 * Sample application that shows how a custom service should be traced.
 * 
 * @author wolfgang.ziegler@dynatrace.com
 *
 */
public class CustomServiceApp {

	private final OneAgentSDK oneAgentSdk;
	
	static CustomServiceApp instance;
	
	private CustomServiceApp() {
		oneAgentSdk = OneAgentSDKFactory.createInstance();
		oneAgentSdk.setLoggingCallback(new StdErrLoggingCallback());
		switch (oneAgentSdk.getCurrentState()) {
		case ACTIVE:
			System.out.println("SDK is active and capturing.");
			break;
		case PERMANENTLY_INACTIVE:
			System.err.println(
					"SDK is PERMANENTLY_INACTIVE; Probably no OneAgent injected or OneAgent is incompatible with SDK.");
			break;
		case TEMPORARILY_INACTIVE:
			System.err.println(
					"SDK is TEMPORARILY_INACTIVE; OneAgent has been deactivated - check OneAgent configuration.");
			break;
		default:
			System.err.println("SDK is in unknown state.");
			break;
		}
		instance = this;
	}

	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**         Running custom service request sample           **");
		System.out.println("*************************************************************");
		try {
			CustomServiceApp app = new CustomServiceApp();
			
			app.traceCustomService();
			
			System.out.println("sample application stopped. sleeping a while, so OneAgent is able to send data to server ...");
			Thread.sleep(15000 * 3); // we have to wait - so OneAgent is able to send data to server
		} catch (Exception e) {
			System.err.println("custom service request sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void traceCustomService() throws Exception {
		String serviceMethod = "onTimer";
		String serviceName = "PeriodicCleanupTask";
		CustomServiceTracer tracer = oneAgentSdk.traceCustomService(serviceMethod, serviceName);
		tracer.start();
		try {
			doOtherThings();
		} catch (Exception e) {
			tracer.error(e.getMessage());
			throw e;
		} finally {
			tracer.end();
		}
	}

	private void doOtherThings() {
		// ...
	}
}
