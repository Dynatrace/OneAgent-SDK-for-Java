package com.dynatrace.oneagent.sdk.samples.messaging;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.IncomingMessageProcessTracer;
import com.dynatrace.oneagent.sdk.api.IncomingMessageReceiveTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.OutgoingMessageTracer;
import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.api.enums.MessageDestinationType;
import com.dynatrace.oneagent.sdk.api.infos.MessagingSystemInfo;

/**
 * Sample application shows how incoming/outgoing messages should be traced.
 * 
 * @author Alram.Lechner
 *
 */
public class MessagingApp {

	private final OneAgentSDK oneAgentSdk;
	final FakedQueueManager queueManager;
	
	static MessagingApp instance;
	
	
	private MessagingApp() {
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
		queueManager = new FakedQueueManager();
		instance = this;
	}

	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**             Running messaging sample                    **");
		System.out.println("*************************************************************");
		try {
			MessagingApp app = new MessagingApp();
			
			// sending and blocking receive ...
			app.sendMessage();
			app.receiveMessage();
			
			// or sending and event based receive: 
			app.registerMessageListener();
			app.sendMessage();
			
			System.out.println("sample application stopped. sleeping a while, so OneAgent is able to send data to server ...");
			Thread.sleep(15000 * 3); // we have to wait - so OneAgent is able to send data to server
		} catch (Exception e) {
			System.err.println("messaging sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void registerMessageListener() {
		queueManager.registerMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				MessagingSystemInfo messagingSystemInfo = oneAgentSdk.createMessagingSystemInfo("DynatraceSample", "theQueue", MessageDestinationType.QUEUE, ChannelType.IN_PROCESS, null);
				IncomingMessageProcessTracer incomingMessageProcessTracer = oneAgentSdk.traceIncomingMessageProcess(messagingSystemInfo);
				// store incoming tag to tracer:
				incomingMessageProcessTracer.setDynatraceStringTag(message.getProperty(OneAgentSDK.DYNATRACE_MESSAGE_PROPERTYNAME));
				incomingMessageProcessTracer.setVendorMessageId(message.getVendorMessageId());
				incomingMessageProcessTracer.start();
				try {
					// do something with the message ...
					System.out.println("Message received: " + message.toString());
				} catch (Exception e) {
					incomingMessageProcessTracer.error(e);
				} finally {
					incomingMessageProcessTracer.end();
				}
			}
		});
		
	}

	private void sendMessage() {
		MessagingSystemInfo messagingSystemInfo = oneAgentSdk.createMessagingSystemInfo("DynatraceSample", "theQueue", MessageDestinationType.QUEUE, ChannelType.IN_PROCESS, null);
		OutgoingMessageTracer outgoingMessageTracer = oneAgentSdk.traceOutgoingMessage(messagingSystemInfo);
		
		outgoingMessageTracer.start();
		try {
			Message messageToSend = new Message();
			
			// add dynatrace tag as property to message ...
			messageToSend.addProperty(OneAgentSDK.DYNATRACE_MESSAGE_PROPERTYNAME, outgoingMessageTracer.getDynatraceStringTag());

			queueManager.send("theQueue", messageToSend);
			
			// in case queueManager provided messageId:
			if (messageToSend.getVendorMessageId() != null) {
				outgoingMessageTracer.setVendorMessageId(messageToSend.getVendorMessageId());
			}
			
		} catch (Exception e) {
			outgoingMessageTracer.error(e);
		} finally {
			outgoingMessageTracer.end();
		}
	}
	
	/** shows how to trace a blocking receive of a message */
	private void receiveMessage() {
		MessagingSystemInfo messagingSystemInfo = oneAgentSdk.createMessagingSystemInfo("DynatraceSample", "theQueue", MessageDestinationType.QUEUE, ChannelType.IN_PROCESS, null);
		IncomingMessageReceiveTracer incomingMessageReceiveTracer = oneAgentSdk.traceIncomingMessageReceive(messagingSystemInfo);
		incomingMessageReceiveTracer.start();
		try {
			Message msg = queueManager.receive("theQeue");
			if (msg == null) {
				return; // no message received
			}
			IncomingMessageProcessTracer incomingMessageProcessTracer = oneAgentSdk.traceIncomingMessageProcess(messagingSystemInfo);
			// store incoming tag to tracer:
			incomingMessageProcessTracer.setDynatraceStringTag(msg.getProperty(OneAgentSDK.DYNATRACE_MESSAGE_PROPERTYNAME));
			incomingMessageProcessTracer.setVendorMessageId(msg.getVendorMessageId());
						incomingMessageProcessTracer.start();
			try {
				// do something with the message ...
				System.out.println("Message received: " + msg.toString());
			} catch (Exception e) {
				incomingMessageProcessTracer.error(e);
			} finally {
				incomingMessageProcessTracer.end();
			}
			
			
		} catch (Exception e) {
			incomingMessageReceiveTracer.error(e);
		} finally {
			incomingMessageReceiveTracer.end();
		}
	}
	
}
