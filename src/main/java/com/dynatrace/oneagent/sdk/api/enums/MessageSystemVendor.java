/*
 * Copyright 2023 Dynatrace LLC
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
package com.dynatrace.oneagent.sdk.api.enums;

import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

/**
 * Enumerates all well-known messaging systems. See {@link OneAgentSDK#createMessagingSystemInfo(String, String, MessageDestinationType, ChannelType, String)}.
 * Using these constants ensures that services captured by OneAgentSDK are handled the same way as traced via built-in sensors.
 * 
 * @since 1.5 
 */
public enum MessageSystemVendor {

	HORNETQ("HornetQ"),
	ACTIVE_MQ("ActiveMQ"),
	RABBIT_MQ("RabbitMQ"),
	ARTEMIS("Artemis"),
	WEBSPHERE("WebSphere"),
	MQSERIES_JMS("MQSeries JMS"),
	MQSERIES("MQSeries"),
	TIBCO("Tibco");
	
	private final String vendorName;
	
	private MessageSystemVendor(String vendorName) {
		this.vendorName = vendorName;
	}
	
	public String getVendorName() {
		return vendorName;
	}

	@Override
	public String toString() {
		return vendorName;
	}

}
