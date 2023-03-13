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

import java.lang.reflect.Method;
import java.util.Map;

import com.dynatrace.oneagent.sdk.impl.OneAgentSDKFactoryImpl;

/**
 * class forwards every API call to OneAgent impl via pre loaded reflection
 * calls.
 *
 * @author Alram.Lechner
 *
 */
public final class SDK2AgentInternalApiProxy {

	private Object agentImpl;
	private final Method oneAgentSDKFactory_createSdk;
	private final Method oneAgentSDK_traceIncomingRemoteCall;
	private final Method oneAgentSDK_traceOutgoingRemoteCall;
	private final Method oneAgentSDK_createInProcessLink;
	private final Method oneAgentSDK_traceInProcessLink;
	private final Method oneAgentSDK_setLoggingCallback;
	private final Method oneAgentSDK_isCapturing;
	private final Method oneAgentSDK_addCustomRequestAttribute_1; // String, String
	private final Method oneAgentSDK_addCustomRequestAttribute_2; // String, long
	private final Method oneAgentSDK_addCustomRequestAttribute_3; // String, double
	private final Method oneAgentSDK_traceIncomingWebRequest;
	private final Method oneAgentSDK_traceOutgoingWebRequest;
	private final Method oneAgentSDK_traceIncomingMessageReceive;
	private final Method oneAgentSDK_traceIncomingMessageProcess;
	private final Method oneAgentSDK_traceOutgoingMessage;
	private final Method oneAgentSDK_traceSQLDatabaseRequest;
	private final Method oneAgentSDK_traceCustomService;
	private final Method tracer_start;
	private final Method tracer_end;
	private final Method tracer_error_1; // string
	private final Method tracer_error_2; // throwable
	private final Method outgoingTaggable_getDynatraceStringTag;
	private final Method outgoingTaggable_getDynatraceByteTag;
	private final Method incomingTaggable_setDynatraceStringTag;
	private final Method incomingTaggable_setDynatraceByteTag;
	private final Method outgoingRemoteCallTracer_setProtocolName;
	private final Method incomingRemoteCallTracer_setProtocolName;
	private final Method webRequestTracer_setStatusCode;
	private final Method webRequestTracer_addResponseHeader;
	private final Method webRequestTracer_addRequestHeader;
	private final Method incomingWebRequestTracer_setRemoteAddress;
	private final Method incomingWebRequestTracer_addParameter;
	private final Method messageTracer_setVendorMessageId;
	private final Method messageTracer_setCorrelationId;
	private final Method databaseRequestTracer_setRowsReturned;
	private final Method databaseRequestTracer_setRoundTripCount;

    private final Method oneAgentSDK_getCurrentTraceAndSpanId;

	public SDK2AgentInternalApiProxy(Object agentImpl) throws NoSuchMethodException, SecurityException {
		this.agentImpl = agentImpl;
		oneAgentSDKFactory_createSdk = findMethod("oneAgentSDKFactory_createSDK", new Class[] {});
		oneAgentSDK_traceIncomingRemoteCall = findMethod("oneAgentSDK_traceIncomingRemoteCall",
				new Class[] { Object.class, String.class, String.class, String.class });
		oneAgentSDK_traceOutgoingRemoteCall = findMethod("oneAgentSDK_traceOutgoingRemoteCall",
				new Class[] { Object.class, String.class, String.class, String.class, Integer.TYPE, String.class });
		oneAgentSDK_createInProcessLink = findMethod("oneAgentSDK_createInProcessLink", new Class[] { Object.class });
		oneAgentSDK_traceInProcessLink = findMethod("oneAgentSDK_traceInProcessLink",
				new Class[] { Object.class, Object.class });
		oneAgentSDK_setLoggingCallback = findMethod("oneAgentSDK_setLoggingCallback",
				new Class[] { Object.class, Object.class });
		oneAgentSDK_isCapturing = findMethod("oneAgentSDK_isCapturing", new Class[] { Object.class });
		oneAgentSDK_addCustomRequestAttribute_1 = findMethod("oneAgentSDK_addCustomRequestAttribute",
				new Class[] { Object.class, String.class, String.class });
		oneAgentSDK_addCustomRequestAttribute_2 = findMethod("oneAgentSDK_addCustomRequestAttribute",
				new Class[] { Object.class, String.class, Long.TYPE });
		oneAgentSDK_addCustomRequestAttribute_3 = findMethod("oneAgentSDK_addCustomRequestAttribute",
				new Class[] { Object.class, String.class, Double.TYPE });
		oneAgentSDK_traceIncomingWebRequest = findMethod("oneAgentSDK_traceIncomingWebRequest",
				new Class[] { Object.class, String.class, String.class, String.class, String.class, String.class });
		oneAgentSDK_traceOutgoingWebRequest = findMethod("oneAgentSDK_traceOutgoingWebRequest",
				new Class[] { Object.class, String.class, String.class });
		oneAgentSDK_traceIncomingMessageReceive = findMethod("oneAgentSDK_traceIncomingMessageReceive",
				new Class[] { Object.class, String.class, String.class, String.class, Integer.TYPE, String.class });
		oneAgentSDK_traceIncomingMessageProcess = findMethod("oneAgentSDK_traceIncomingMessageProcess",
				new Class[] { Object.class, String.class, String.class, String.class, Integer.TYPE, String.class });
		oneAgentSDK_traceOutgoingMessage = findMethod("oneAgentSDK_traceOutgoingMessage",
				new Class[] { Object.class, String.class, String.class, String.class, Integer.TYPE, String.class });
		oneAgentSDK_traceSQLDatabaseRequest = findMethod("oneAgentSDK_traceSQLDatabaseRequest",
				new Class[] { Object.class, String.class, String.class, Integer.TYPE, String.class, String.class });
		oneAgentSDK_traceCustomService  = findMethod("oneAgentSDK_traceCustomService",
				new Class[] { Object.class, String.class, String.class });
		tracer_start = findMethod("tracer_start", new Class[] { Object.class });
		tracer_end = findMethod("tracer_end", new Class[] { Object.class });
		tracer_error_1 = findMethod("tracer_error", new Class[] { Object.class, String.class });
		tracer_error_2 = findMethod("tracer_error", new Class[] { Object.class, Throwable.class });
		outgoingTaggable_getDynatraceStringTag = findMethod("outgoingTaggable_getDynatraceStringTag",
				new Class[] { Object.class });
		outgoingTaggable_getDynatraceByteTag = findMethod("outgoingTaggable_getDynatraceByteTag",
				new Class[] { Object.class });
		incomingTaggable_setDynatraceStringTag = findMethod("incomingTaggable_setDynatraceStringTag",
				new Class[] { Object.class, String.class });
		incomingTaggable_setDynatraceByteTag = findMethod("incomingTaggable_setDynatraceByteTag",
				new Class[] { Object.class, byte[].class });
		outgoingRemoteCallTracer_setProtocolName = findMethod("outgoingRemoteCallTracer_setProtocolName",
				new Class[] { Object.class, String.class });
		incomingRemoteCallTracer_setProtocolName = findMethod("incomingRemoteCallTracer_setProtocolName",
				new Class[] { Object.class, String.class });
		webRequestTracer_setStatusCode = findMethod("webRequestTracer_setStatusCode",
				new Class[] { Object.class, Integer.TYPE });
		webRequestTracer_addResponseHeader = findMethod("webRequestTracer_addResponseHeader",
				new Class[] { Object.class, String.class, String.class });
		webRequestTracer_addRequestHeader = findMethod("webRequestTracer_addRequestHeader",
				new Class[] { Object.class, String.class, String.class });
		incomingWebRequestTracer_setRemoteAddress = findMethod("incomingWebRequestTracer_setRemoteAddress",
				new Class[] { Object.class, String.class });
		incomingWebRequestTracer_addParameter = findMethod("incomingWebRequestTracer_addParameter",
				new Class[] { Object.class, String.class, String.class });
		messageTracer_setVendorMessageId = findMethod("messageTracer_setVendorMessageId",
				new Class[] { Object.class, String.class });
		messageTracer_setCorrelationId = findMethod("messageTracer_setCorrelationId",
				new Class[] { Object.class, String.class });
		databaseRequestTracer_setRowsReturned = findMethod("databaseRequestTracer_setRowsReturned",
				new Class[] { Object.class, Integer.TYPE });
		databaseRequestTracer_setRoundTripCount = findMethod("databaseRequestTracer_setRoundTripCount",
				new Class[] { Object.class, Integer.TYPE });

        oneAgentSDK_getCurrentTraceAndSpanId = findMethod("oneAgentSDK_getCurrentTraceAndSpanId",
                new Class[] {Object.class});
	}

	private Method findMethod(String name, Class<?>... args) throws NoSuchMethodException, SecurityException {
		return agentImpl.getClass().getMethod(name, args);
	}

	private Object invoke(Method m, Object... args) {
		try {
			return m.invoke(agentImpl, args);
		} catch (Exception e) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public Object oneAgentSDKFactory_createSdk() {
		return invoke(oneAgentSDKFactory_createSdk);
	}

	Object oneAgentSDK_traceIncomingRemoteCall(Object sdk, String remoteMethod, String remoteService,
			String serviceEndpoint) {
		return invoke(oneAgentSDK_traceIncomingRemoteCall, sdk, remoteMethod, remoteService, serviceEndpoint);
	}

	Object oneAgentSDK_traceOutgoingRemoteCall(Object sdk, String remoteMethod, String remoteService,
			String serverEndpoint, int channelType, String channelEndpoint) {
		return invoke(oneAgentSDK_traceOutgoingRemoteCall, sdk, remoteMethod, remoteService, serverEndpoint,
				channelType, channelEndpoint);
	}

	Object oneAgentSDK_createInProcessLink(Object agentSdkImpl) {
		return invoke(oneAgentSDK_createInProcessLink, agentSdkImpl);
	}

	Object oneAgentSDK_traceInProcessLink(Object agentSdkImpl, InProcessLinkImpl inProcessLink) {
		return invoke(oneAgentSDK_traceInProcessLink, agentSdkImpl, inProcessLink.getAgentProvidedLink());
	}

	Object oneAgentSDK_traceIncomingWebRequest(Object agentSdkImpl, WebApplicationInfoImpl webApplicationInfo,
			String url, String method) {
		return invoke(oneAgentSDK_traceIncomingWebRequest, agentSdkImpl, webApplicationInfo.getWebServerName(),
				webApplicationInfo.getApplicationID(), webApplicationInfo.getContextRoot(), url, method);
	}

	Object oneAgentSDK_traceOutgoingWebRequest(Object agentSdkImpl, String url, String method) {
		return invoke(oneAgentSDK_traceOutgoingWebRequest, agentSdkImpl, url, method);
	}
	
	Object oneAgentSDK_traceOutgoingMessage(Object agentSdkImpl, MessagingSystemInfoImpl messagingSystem) {
		return invoke(oneAgentSDK_traceOutgoingMessage, agentSdkImpl, messagingSystem.getVendorName(), 
				messagingSystem.getDestinationName(), messagingSystem.getDestinationType().getName(), 
				messagingSystem.getChannelType() == null ? -1 : messagingSystem.getChannelType().getSDKConstant(),
				messagingSystem.getChannelEndpoint());
	}

	Object oneAgentSDK_traceIncomingMessageReceive(Object agentSdkImpl, MessagingSystemInfoImpl messagingSystem) {
		return invoke(oneAgentSDK_traceIncomingMessageReceive, agentSdkImpl, messagingSystem.getVendorName(), 
				messagingSystem.getDestinationName(), messagingSystem.getDestinationType().getName(), 
				messagingSystem.getChannelType() == null ? -1 : messagingSystem.getChannelType().getSDKConstant(),
				messagingSystem.getChannelEndpoint());
	}

	Object oneAgentSDK_traceIncomingMessageProcess(Object agentSdkImpl,	MessagingSystemInfoImpl messagingSystem) {
		return invoke(oneAgentSDK_traceIncomingMessageProcess, agentSdkImpl, messagingSystem.getVendorName(), 
				messagingSystem.getDestinationName(), messagingSystem.getDestinationType().getName(), 
				messagingSystem.getChannelType() == null ? -1 : messagingSystem.getChannelType().getSDKConstant(),
				messagingSystem.getChannelEndpoint());
	}

	Object oneAgentSDK_traceSQLDatabaseRequest(Object agentSdkImpl, DatabaseInfoImpl databaseInfo, String sql) {
		return invoke(oneAgentSDK_traceSQLDatabaseRequest, agentSdkImpl, databaseInfo.getName(), databaseInfo.getVendor(), 
				databaseInfo.getChannelType() == null ? -1 : databaseInfo.getChannelType().getSDKConstant(),
				databaseInfo.getChannelEndpoint(), sql);
	}

	Object oneAgentSDK_traceCustomService(Object agentSdkImpl, String serviceMethod, String serviceName) {
		return invoke(oneAgentSDK_traceCustomService, agentSdkImpl, serviceMethod, serviceName);
	}

	void oneAgentSDK_setLoggingCallback(Object sdk, Object loggingCallback) {
		invoke(oneAgentSDK_setLoggingCallback, sdk, loggingCallback);
	}

	Boolean oneAgentSDK_isCapturing(Object sdk) {
		return (Boolean) invoke(oneAgentSDK_isCapturing, sdk);
	}

	public void oneAgentSDK_addCustomRequestAttribute(Object agentSdkImpl, String key, String value) {
		invoke(oneAgentSDK_addCustomRequestAttribute_1, agentSdkImpl, key, value);
	}

	public void oneAgentSDK_addCustomRequestAttribute(Object agentSdkImpl, String key, long value) {
		invoke(oneAgentSDK_addCustomRequestAttribute_2, agentSdkImpl, key, value);
	}

	public void oneAgentSDK_addCustomRequestAttribute(Object agentSdkImpl, String key, double value) {
		invoke(oneAgentSDK_addCustomRequestAttribute_3, agentSdkImpl, key, value);
	}

	void tracer_start(Object node) {
		invoke(tracer_start, node);
	}

	void tracer_end(Object node) {
		invoke(tracer_end, node);
	}

	void tracer_error(Object node, String message) {
		invoke(tracer_error_1, node, message);
	}

	void tracer_error(Object node, Throwable error) {
		invoke(tracer_error_2, node, error);
	}

	String outgoingTaggable_getDynatraceStringTag(Object taggableClient) {
		return (String) invoke(outgoingTaggable_getDynatraceStringTag, taggableClient);
	}

	byte[] outgoingTaggable_getDynatraceByteTag(Object taggableClient) {
		return (byte[]) invoke(outgoingTaggable_getDynatraceByteTag, taggableClient);
	}

	void incomingTaggable_setDynatraceStringTag(Object taggableServer, String tag) {
		invoke(incomingTaggable_setDynatraceStringTag, taggableServer, tag);
	}

	void incomingTaggable_setDynatraceByteTag(Object taggableServer, byte[] tag) {
		invoke(incomingTaggable_setDynatraceByteTag, taggableServer, tag);
	}

	void outgoingRemoteCallTracer_setProtocolName(Object remoteCallClient, String protocolname) {
		invoke(outgoingRemoteCallTracer_setProtocolName, remoteCallClient, protocolname);
	}

	void incomingRemoteCallTracer_setProtocolName(Object remoteCallServer, String protocolname) {
		invoke(incomingRemoteCallTracer_setProtocolName, remoteCallServer, protocolname);
	}

	void webRequestTracer_setStatusCode(Object webRequestTracer, int statusCode) {
		invoke(webRequestTracer_setStatusCode, webRequestTracer, statusCode);
	}

	void webRequestTracer_addResponseHeader(Object webRequestTracer, String name, String value) {
		invoke(webRequestTracer_addResponseHeader, webRequestTracer, name, value);
	}

	void webRequestTracer_addRequestHeader(Object webRequestTracer, String name, String value) {
		invoke(webRequestTracer_addRequestHeader, webRequestTracer, name, value);
	}

	void incomingWebRequestTracer_setRemoteAddress(Object incomingWebRequestTracer, String remoteAddress) {
		invoke(incomingWebRequestTracer_setRemoteAddress, incomingWebRequestTracer, remoteAddress);
	}

	void incomingWebRequestTracer_addParameter(Object incomingWebRequestTracer, String name, String value) {
		invoke(incomingWebRequestTracer_addParameter, incomingWebRequestTracer, name, value);
	}

	void messageTracer_setVendorMessageId(Object messageTracer, String vendorMessageId) {
		invoke(messageTracer_setVendorMessageId, messageTracer, vendorMessageId);
	}

	void messageTracer_setCorrelationId(Object messageTracer, String correlationId) {
		invoke(messageTracer_setCorrelationId, messageTracer, correlationId);
	}

	void databaseRequestTracer_setRowsReturned(Object databaseRequestTracer, int rowsReturned) {
		invoke(databaseRequestTracer_setRowsReturned, databaseRequestTracer, rowsReturned);
	}

	void databaseRequestTracer_setRoundTripCount(Object databaseRequestTracer, int roundTripCount) {
		invoke(databaseRequestTracer_setRoundTripCount, databaseRequestTracer, roundTripCount);
	}

    @SuppressWarnings("unchecked")
    public Map.Entry<String, String> oneAgentSDK_getCurrentTraceAndSpanId(Object sdk) {
        return (Map.Entry<String, String>) invoke(oneAgentSDK_getCurrentTraceAndSpanId, sdk);
    }
}
