package com.dynatrace.sample.oneagent.adk;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.dynatrace.oneagent.adk.OneAgentADKFactory;
import com.dynatrace.oneagent.adk.api.OneAgentADK;
import com.dynatrace.oneagent.adk.api.OutgoingRemoteCall;
import com.dynatrace.oneagent.adk.api.enums.ADKState;

public class ClientApp {

	private final OneAgentADK oneAgentAdk;

	private ClientApp() {
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
		System.out.println("**       Running remote call client                        **");
		System.out.println("*************************************************************");
		int port = 33744;
		for (String arg : args) {
			if (arg.startsWith("port=")) {
				port = Integer.parseInt(arg.substring("port=".length()));
			} else {
				System.err.println("unknown argument: " + arg);
			}
		}
		try {
			new ClientApp().run(port);
			System.out.println("remote call client finished.");
		} catch (Exception e) {
			System.err.println("remote call client failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void run(int port) throws IOException, ClassNotFoundException {
		System.out.println("clients connecting to server on port " + port);
		Socket socket = new Socket((String) null, port);
		try {
			System.out.println("Connected to " + socket.getInetAddress().getHostName() + ":" + socket.getPort()
					+ " connected");
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			traceCallToServer(out, port);

		} finally {
			socket.close();
		}
	}

	private void traceCallToServer(ObjectOutputStream out, int port) throws IOException {
		OutgoingRemoteCall externalOutgoingRemoteCall = oneAgentAdk.createExternalOutgoingRemoteCall("myMethod", "myService", "endpoint", "localhost:" + port);
		externalOutgoingRemoteCall.start();
		try {
			String outgoingTag = externalOutgoingRemoteCall.getDynatraceStringTag();
			System.out.println("send tag to server: " + outgoingTag);
			invokeCallToServer(out, outgoingTag);
		} catch (Exception e) {
			externalOutgoingRemoteCall.error(e);
		} finally {
			externalOutgoingRemoteCall.end();
		}
	}

	private void invokeCallToServer(ObjectOutputStream out, String outgoingTag) throws IOException {
		// call the server and send the tag
		out.writeObject(outgoingTag);
	}
	
}