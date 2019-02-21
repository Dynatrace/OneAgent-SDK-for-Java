package com.dynatrace.oneagent.sdk.samples.messaging;

import java.util.HashMap;
import java.util.Map;

public class Message {

	private Map<String, String> properties = new HashMap<String, String>();
	private String vendorMessageId = null;
	private String correlationId = null;
	
	public void addProperty(String propertyName, String propertyValue) {
		properties.put(propertyName, propertyValue);
	}
	
	public String getProperty(String propertyName) {
		return properties.get(propertyName);
	}

	public String getVendorMessageId() {
		return vendorMessageId;
	}

	public void setVendorMessageId(String vendorMessageId) {
		this.vendorMessageId = vendorMessageId;
	}

	public String getCorrelationId() {
		return correlationId;
	}

	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}

	
}
