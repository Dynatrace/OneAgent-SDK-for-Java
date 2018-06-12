package com.dynatrace.oneagent.sdk.samples.webrequest;

/*
 * Copyright 2018 Dynatrace LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.sql.SQLException;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.samples.webrequest.FakedWebserver.HttpRequest;

/**
 * Sample application shows how incoming webrequests should be traced.
 * 
 * @author Alram.Lechner
 *
 */
public class IncomingWebRequestApp {

	private final OneAgentSDK oneAgentSdk;
	private final FakedWebserver webServer;

	private IncomingWebRequestApp() {
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
	}

	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**       Running incoming webrequests sample               **");
		System.out.println("*************************************************************");
		try {
			IncomingWebRequestApp app = new IncomingWebRequestApp();
			app.runIncomingWebrequest();
			System.out.println("sample application stopped. sleeping a while, so OneAgent is able to send data to server ...");
			Thread.sleep(15000 * 3); // we have to wait - so OneAgent is able to send data to server
		} catch (Exception e) {
			System.err.println("in-process-linking sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void runIncomingWebrequest() throws IOException, ClassNotFoundException, InterruptedException, SQLException {
		System.out.println("run faked incoming webrequest ...");
		webServer.serve(new HttpRequest("/billing/showBill?id=234324", "GET", "192.168.13.14"));
	}
	
}
