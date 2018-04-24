package com.dynatrace.oneagent.sdk.samples.inprocesslinking;

import java.sql.Statement;
import java.util.concurrent.BlockingQueue;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.InProcessLinkTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

public class DatabaseWorkerThread extends Thread {

	private BlockingQueue<AsyncDatabaseWorkItem> queue;
	private Statement dbStatement = new NoopStatement();
	private OneAgentSDK oneAgentSdk;

	DatabaseWorkerThread(BlockingQueue<AsyncDatabaseWorkItem> queue) {
		this.queue = queue;
		oneAgentSdk = OneAgentSDKFactory.createInstance();
		setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				AsyncDatabaseWorkItem item = queue.take();
				if (item == AsyncDatabaseWorkItem.END) {
					System.out.println("[DBWorker] retrieved end - exiting");
					return;
				}
				System.out.println("[DBWorker] retrieved item: " + item.getSqlStatement());
				
				// trace the execution of retrieved SQL:
				InProcessLinkTracer inProcessLinkTracer = oneAgentSdk.traceInProcessLink(item.getLink());
				inProcessLinkTracer.start();
				try {
					dbStatement.execute(item.getSqlStatement());
				} catch (Exception e) {
					inProcessLinkTracer.error(e);
					System.out.println("[DBWorker] *** DB operation failed: " + e.getClass().getName() + ": " + e.getMessage());
				} finally {
					inProcessLinkTracer.end();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(-2);
		}
	}

}
