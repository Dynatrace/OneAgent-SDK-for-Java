package com.dynatrace.sample.oneagent.adk;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.dynatrace.oneagent.adk.OneAgentADKFactory;
import com.dynatrace.oneagent.adk.api.IncomingRemoteCall;
import com.dynatrace.oneagent.adk.api.OneAgentADK;
import com.dynatrace.oneagent.adk.api.enums.ADKState;

/**
 * ServerApp is listing for
 * 
 * @author Alram.Lechner
 *
 */
public class ServerApp {

	
	private final OneAgentADK oneAgentAdk;

	private ServerApp() {
		oneAgentAdk = OneAgentADKFactory.createInstance();
		ADKState currentADKState = oneAgentAdk.getCurrentADKState();
		switch (currentADKState) {
		case ACTIVE:
			System.out.println("ADK is active and capturing.");
			break;
		case PERMANENT_INACTIVE:
			System.err.println(
					"ADK is PERMANENT_INACTIVE; Probably no agent injected or agent is incompatible with ADK.");
			break;
		case TEMPORARY_INACTIVE:
			System.err.println("ADK is TEMPORARY_INACTIVE; Agent has been deactived - check agent configuration.");
			break;
		default:
			System.err.println("ADK is in unknown state.");
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
			System.out.println("remote call server stopped.");
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
		IncomingRemoteCall externalIncomingRemoteCall = oneAgentAdk.createExternalIncomingRemoteCall("myMethod", "myService", "endpoint");
		if (receivedTag instanceof String) {
			externalIncomingRemoteCall.setDynatraceStringTag((String) receivedTag);
		} else if (receivedTag instanceof byte[]) {
			externalIncomingRemoteCall.setDynatraceByteTag((byte[]) receivedTag);
		} else {
			System.err.println("invalid tag received: " + receivedTag.getClass().toString());
		}
		
		externalIncomingRemoteCall.start();
		try {
			handleClientRequest();
		} catch (Exception e) {
			externalIncomingRemoteCall.error(e);
		} finally {
			externalIncomingRemoteCall.end();
		}
		
	}

	private void handleClientRequest() {
		// do whatever the server should do ...
	}
	
}