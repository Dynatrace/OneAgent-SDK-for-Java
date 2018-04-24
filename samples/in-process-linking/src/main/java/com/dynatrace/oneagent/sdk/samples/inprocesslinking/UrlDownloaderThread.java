package com.dynatrace.oneagent.sdk.samples.inprocesslinking;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.BlockingQueue;

import javax.net.ssl.HttpsURLConnection;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.InProcessLinkTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

public class UrlDownloaderThread extends Thread {

	private BlockingQueue<UrlDownloadItem> queue;
	private OneAgentSDK oneAgentSdk;

	UrlDownloaderThread(BlockingQueue<UrlDownloadItem> queue) {
		this.queue = queue;
		oneAgentSdk = OneAgentSDKFactory.createInstance();
		setDaemon(true);
	}

	@Override
	public void run() {
		try {
			while (true) {
				UrlDownloadItem item = queue.take();
				if (item == UrlDownloadItem.END) {
					System.out.println("[DBWorker] retrieved end - exiting");
					return;
				}
				System.out.println("[UrlDownloaderThread] retrieved item: " + item.getUrl());
				
				// trace the execution of retrieved SQL:
				InProcessLinkTracer inProcessLinkTracer = oneAgentSdk.traceInProcessLink(item.getLink());
				inProcessLinkTracer.start();
				HttpsURLConnection connection = (HttpsURLConnection) new URL(item.getUrl()).openConnection();
				try {
					System.out.println("[UrlDownloaderThread] HTTP status code: " + connection.getResponseCode());
					InputStream inputStream = connection.getInputStream();
					int bytesRetrieved = 0;
					try {
						byte buffer[] = new byte[1024*10];
						while(true) {
							int read = inputStream.read(buffer);
							if (read < 0) {
								break;
							}
							bytesRetrieved += read;
						}
						System.out.println("[UrlDownloaderThread] retrieved " + bytesRetrieved + " bytes");
					} finally {
						inputStream.close();
					}
				} catch (Exception e) {
					inProcessLinkTracer.error(e);
					System.out.println("[UrlDownloaderThread] *** download operation failed: " + e.getClass().getName() + ": " + e.getMessage());
				} finally {
					inProcessLinkTracer.end();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-2);
		}
	}

}
