package com.dynatrace.oneagent.sdk.samples.messaging;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FakedQueueManager {

	private BlockingQueue<Message> queue = new ArrayBlockingQueue<Message>(10);
	private MessageListener messageListener;
	
	public FakedQueueManager() {
	}
	
	private class QueueManagerThread extends Thread {
		private QueueManagerThread() {
			super("QueueManager");
			setDaemon(true);
		}
		
		@Override
		public void run() {
			while (true) {
				Message incomingMessage;
				try {
					incomingMessage = queue.take();
					messageListener.onMessage(incomingMessage);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	public void registerMessageListener(MessageListener listener) {
		this.messageListener = listener;
		new QueueManagerThread().start();
	}

	
	public void send(String queueName, Message message) {
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Message receive(String queueName) {
		try {
			return queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
