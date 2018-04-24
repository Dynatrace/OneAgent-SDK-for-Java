package com.dynatrace.oneagent.sdk.samples.inprocesslinking;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

	private BlockingQueue<AsyncDatabaseWorkItem> blockingQueue = new ArrayBlockingQueue<AsyncDatabaseWorkItem>(5);
	private NoopStatement dbStatement = new NoopStatement();

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
			new DatabaseWorkerThread(app.blockingQueue).start();
			app.startAsyncOperation();
			// System.out.println("remote call server stopped. sleeping a while, so OneAgent
			// is able to send data to server ...");
			Thread.sleep(15000); // we have to wait - so OneAgent is able to send data to
			// server.
			app.end();
		} catch (Exception e) {
			System.err.println("in-process-linking sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * TODO: add custom service for this method
	 */
	private void startAsyncOperation() throws IOException, ClassNotFoundException, InterruptedException, SQLException {
		System.out.println("Executor started ...");
		// process synchronous db work:
		ResultSet bookingRs = dbStatement.executeQuery("Select * from bookings where bookingId = 'AB01022'");
		// do some sync work ...
		bookingRs.close();

		// process DB execution, where no result is needed async in background: 
		AsyncDatabaseWorkItem asyncWorkItem = new AsyncDatabaseWorkItem(
				"update bookings set paymentState='FINISHED' where bookingId = 'AB01022'", 
				oneAgentSdk.createInProcessLink());
		blockingQueue.put(asyncWorkItem);
}
	
	private void end() {
		blockingQueue.offer(AsyncDatabaseWorkItem.END);
	}

}