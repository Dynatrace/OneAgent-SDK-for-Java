package com.dynatrace.oneagent.sdk.samples.inprocesslinking;

import java.io.BufferedReader;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.net.ssl.HttpsURLConnection;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

/**
 * ServerApp is listing for remote call requests from remote-call client.
 * 
 * @author Alram.Lechner
 *
 */
public class InProcessLinkingApp {

	private final OneAgentSDK oneAgentSdk;

	private BlockingQueue<UrlDownloadItem> blockingQueue = new ArrayBlockingQueue<UrlDownloadItem>(5);

	private InProcessLinkingApp() {
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
	}

	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**       Running in-process-linking sample                 **");
		System.out.println("*************************************************************");
		try {
			InProcessLinkingApp app = new InProcessLinkingApp();
			// start worker thread, responsible for downloading files in background. thread waits on provided blocking queue.
			new UrlDownloaderThread(app.blockingQueue).start();
			String latestVersion = app.startAsyncOperation();
			System.err.println("Found latest OneAgent SDK for Java: " + latestVersion);
			System.out.println("sample application stopped. sleeping a while, so OneAgent is able to send data to server ...");
			Thread.sleep(15000); // we have to wait - so OneAgent is able to send data to server
			app.end();
		} catch (Exception e) {
			System.err.println("in-process-linking sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * TODO: add custom service for this method
	 * check for latest version and start downloading them asynchronously.
	 */
	private String startAsyncOperation() throws IOException, ClassNotFoundException, InterruptedException, SQLException {
		System.out.println("Executor started ...");
		
		// do some sync work - eg. check for latest version:
		HttpsURLConnection url = (HttpsURLConnection) new URL("https://github.com/Dynatrace/OneAgent-SDK-for-Java/releases/latest").openConnection();

		// we expect redirect to latest release url:
		url.setInstanceFollowRedirects(false);
		int responseStatus = url.getResponseCode();
		if (responseStatus != 302) {
			System.out.println("no redirect retrieved!");
			return null;
		}
		
		// e. g.: https://github.com/Dynatrace/OneAgent-SDK-for-Java/releases/tag/v1.0.3
		String location = url.getHeaderField("Location");
		String latestVersion = location.substring(location.lastIndexOf('/')+1);

		// download the big release archive asynchronously ... 
		UrlDownloadItem asyncWorkItem = new UrlDownloadItem(
				"https://github.com/Dynatrace/OneAgent-SDK-for-Java/archive/" + latestVersion + ".zip", 
				oneAgentSdk.createInProcessLink());
		blockingQueue.put(asyncWorkItem);
		
		return latestVersion;
	}
	
	private void end() {
		blockingQueue.offer(UrlDownloadItem.END);
	}

}