package com.dynatrace.sample.oneagent.sdk;

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
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.IncomingRemoteCallTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

/**
 * ServerApp is listing for
 * 
 * @author Alram.Lechner
 *
 */
public class ServerApp {

	
	private final OneAgentSDK oneAgentSdk;

	private ServerApp() {
		oneAgentSdk = OneAgentSDKFactory.createInstance();
		oneAgentSdk.setLoggingCallback(new StdErrLoggingCallback());
		switch (oneAgentSdk.getCurrentState()) {
		case ACTIVE:
			System.out.println("SDK is active and capturing.");
			break;
		case PERMANENTLY_INACTIVE:
			System.err.println(
					"SDK is PERMANENT_INACTIVE; Probably no agent injected or agent is incompatible with SDK.");
			break;
		case TEMPORARILY_INACTIVE:
			System.err.println("SDK is TEMPORARY_INACTIVE; Agent has been deactived - check agent configuration.");
			break;
		default:
			System.err.println("SDK is in unknown state.");
			break;
		}
	}
	
	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**       Running remote call server                        **");
		System.out.println("*************************************************************");
		int port = 33744; // default port
		for (String arg : args) {
			if (arg.startsWith("port=")) {
				port = Integer.parseInt(arg.substring("port=".length()));
			} else {
				System.err.println("unknown argument: " + arg);
			}
		}
		try {
			new ServerApp().run(port);
			System.out.println("remote call server stopped. sleeping a while, so agent is able to send data to server ...");
			Thread.sleep(15000); // we have to wait - so agent is able to send data to server.
		} catch (Exception e) {
			System.err.println("remote call server failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void run(int port) throws IOException, ClassNotFoundException {
		ServerSocket serverSocket = new ServerSocket(port);
		try {
			System.out.println("Waiting for clients no port " + serverSocket.getInetAddress().getHostName() + ":"
					+ serverSocket.getLocalPort());
			Socket client = serverSocket.accept();
			try {
				System.out.println(
						"Client " + client.getInetAddress().getHostName() + ":" + client.getPort() + " connected");
				ObjectInputStream in = new ObjectInputStream(client.getInputStream());
				Object receviedTag = in.readObject();
				System.out.println("received tag: " + receviedTag.toString());
				traceClientRequest(receviedTag);
			} finally {
				client.close();
			}
		} finally {
			serverSocket.close();
		}
	}
	
	private void traceClientRequest(Object receivedTag) {
		IncomingRemoteCallTracer incomingRemoteCall = oneAgentSdk.traceIncomingRemoteCall("myMethod", "myService", "endpoint");
		if (receivedTag instanceof String) {
			incomingRemoteCall.setDynatraceStringTag((String) receivedTag);
		} else if (receivedTag instanceof byte[]) {
			incomingRemoteCall.setDynatraceByteTag((byte[]) receivedTag);
		} else {
			System.err.println("invalid tag received: " + receivedTag.getClass().toString());
		}
		
		incomingRemoteCall.start();
		try {
			handleClientRequest();
		} catch (Exception e) {
			incomingRemoteCall.error(e);
		} finally {
			incomingRemoteCall.end();
		}
		
	}

	private void handleClientRequest() {
		// do whatever the server should do ...
	}
	
}