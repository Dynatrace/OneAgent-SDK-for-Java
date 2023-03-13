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
package com.dynatrace.oneagent.sdk.impl.noop;

import com.dynatrace.oneagent.sdk.api.CustomServiceTracer;
import com.dynatrace.oneagent.sdk.api.DatabaseRequestTracer;
import com.dynatrace.oneagent.sdk.api.InProcessLink;
import com.dynatrace.oneagent.sdk.api.InProcessLinkTracer;
import com.dynatrace.oneagent.sdk.api.IncomingMessageProcessTracer;
import com.dynatrace.oneagent.sdk.api.IncomingMessageReceiveTracer;
import com.dynatrace.oneagent.sdk.api.IncomingRemoteCallTracer;
import com.dynatrace.oneagent.sdk.api.IncomingWebRequestTracer;
import com.dynatrace.oneagent.sdk.api.LoggingCallback;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.OutgoingMessageTracer;
import com.dynatrace.oneagent.sdk.api.OutgoingRemoteCallTracer;
import com.dynatrace.oneagent.sdk.api.OutgoingWebRequestTracer;
import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.api.enums.MessageDestinationType;
import com.dynatrace.oneagent.sdk.api.enums.SDKState;
import com.dynatrace.oneagent.sdk.api.infos.DatabaseInfo;
import com.dynatrace.oneagent.sdk.api.infos.MessagingSystemInfo;
import com.dynatrace.oneagent.sdk.api.infos.TraceContextInfo;
import com.dynatrace.oneagent.sdk.api.infos.WebApplicationInfo;

/**
 * This class provides an empty (NOOP) implementation of the {@link OneAgentSDK}
 * interface.
 *
 * @author Alram.Lechner
 *
 */
public final class OneAgentSDKNoop implements OneAgentSDK {
	
	// ***** Webrequests (incoming) *****
	@Override
	public WebApplicationInfo createWebApplicationInfo(String webServerName, String applicationID, String contextRoot) {
		return WebApplicationInfoNoop.INSTANCE;
	}

	@Override
	public IncomingWebRequestTracer traceIncomingWebRequest(WebApplicationInfo webApplicationInfo, String url,
			String method) {
		return IncomingWebRequestTracerNoop.INSTANCE;
	}

	// ***** Remote Calls (outgoing & incoming) *****
	@Override
	public IncomingRemoteCallTracer traceIncomingRemoteCall(String remoteMethod, String remoteService,
			String clientEndpoint) {
		return RemoteCallServerTracerNoop.INSTANCE;
	}

	@Override
	public OutgoingRemoteCallTracer traceOutgoingRemoteCall(String remoteMethod, String remoteService,
			String serviceEndpoint, ChannelType channelType, String channelEndpoint) {
		return RemoteCallClientTracerNoop.INSTANCE;
	}

	// ***** Common *****

	@Override
	public void setLoggingCallback(LoggingCallback loggingCallback) {
	}

	@Override
	public SDKState getCurrentState() {
		return SDKState.PERMANENTLY_INACTIVE;
	}

	@Override
	public InProcessLink createInProcessLink() {
		return InProcessLinkNoop.INSTANCE;
	}

	@Override
	public InProcessLinkTracer traceInProcessLink(InProcessLink inProcessLink) {
		return InProcessLinkTracerNoop.INSTANCE;
	}

	@Override
	public void addCustomRequestAttribute(String key, String value) {
	}

	@Override
	public void addCustomRequestAttribute(String key, long value) {
	}

	@Override
	public void addCustomRequestAttribute(String key, double value) {
	}

	@Override
	public OutgoingWebRequestTracer traceOutgoingWebRequest(String url, String method) {
		return OutgoingWebRequestTracerNoop.INSTANCE;
	}

	@Override
	public MessagingSystemInfo createMessagingSystemInfo(String vendorName, String destinationName,
			MessageDestinationType destinationType, ChannelType channelType, String channelEndpoint) {
		return MessagingSystemInfoNoop.INSTANCE;
	}

	@Override
	public OutgoingMessageTracer traceOutgoingMessage(MessagingSystemInfo messagingSystem) {
		return OutgoingMessageTracerNoop.INSTANCE;
	}

	@Override
	public IncomingMessageReceiveTracer traceIncomingMessageReceive(MessagingSystemInfo messagingSystem) {
		return IncomingMessageReceiveTracerNoop.INSTANCE;
	}

	@Override
	public IncomingMessageProcessTracer traceIncomingMessageProcess(MessagingSystemInfo messagingSystem) {
		return IncomingMessageProcessTracerNoop.INSTANCE;
	}

	@Override
	public DatabaseInfo createDatabaseInfo(String name, String vendor, ChannelType channelType,
			String channelEndpoint) {
		return DatabaseInfoNoop.INSTANCE;
	}

	@Override
	public DatabaseRequestTracer traceSqlDatabaseRequest(DatabaseInfo databaseInfo, String statement) {
		return DatabaseRequestTracerNoop.INSTANCE;
	}

	@Override
	public CustomServiceTracer traceCustomService(String serviceMethod, String serviceName) {
		return CustomServiceTracerNoop.INSTANCE;
	}

    @Override
    public TraceContextInfo getTraceContextInfo() {
        return TraceContextInfoNoop.INSTANCE;
    }
}
