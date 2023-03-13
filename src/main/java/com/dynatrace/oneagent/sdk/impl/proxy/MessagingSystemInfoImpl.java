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
package com.dynatrace.oneagent.sdk.impl.proxy;

import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.api.enums.MessageDestinationType;

final class MessagingSystemInfoImpl implements com.dynatrace.oneagent.sdk.api.infos.MessagingSystemInfo {

	private final String vendorName;
	private final String destinationName;
	private final MessageDestinationType destinationType;
	private final ChannelType channelType;
	private final String channelEndpoint;

	MessagingSystemInfoImpl(String vendorName, String destinationName, MessageDestinationType destinationType,
			ChannelType channelType, String channelEndpoint) {
				this.vendorName = vendorName;
				this.destinationName = destinationName;
				this.destinationType = destinationType;
				this.channelType = channelType;
				this.channelEndpoint = channelEndpoint;
	}

	String getVendorName() {
		return vendorName;
	}
	
	String getDestinationName() {
		return destinationName;
	}
	
	MessageDestinationType getDestinationType() {
		return destinationType;
	}
	
	ChannelType getChannelType() {
		return channelType;
	}
	
	String getChannelEndpoint() {
		return channelEndpoint;
	}
}
