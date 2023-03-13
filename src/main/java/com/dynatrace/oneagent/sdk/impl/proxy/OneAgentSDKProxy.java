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

import java.util.Map;

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
import com.dynatrace.oneagent.sdk.impl.OneAgentSDKFactoryImpl;
import com.dynatrace.oneagent.sdk.impl.noop.CustomServiceTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.DatabaseInfoNoop;
import com.dynatrace.oneagent.sdk.impl.noop.DatabaseRequestTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.InProcessLinkNoop;
import com.dynatrace.oneagent.sdk.impl.noop.InProcessLinkTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.IncomingMessageProcessTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.IncomingMessageReceiveTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.IncomingWebRequestTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.MessagingSystemInfoNoop;
import com.dynatrace.oneagent.sdk.impl.noop.OutgoingMessageTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.OutgoingWebRequestTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.RemoteCallClientTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.RemoteCallServerTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.TraceContextInfoNoop;
import com.dynatrace.oneagent.sdk.impl.noop.WebApplicationInfoNoop;

/** TODO: check if/how class could be generated */
public final class OneAgentSDKProxy implements OneAgentSDK {

	private final SDK2AgentInternalApiProxy apiProxy;
	private final Object agentSdkImpl;

	public OneAgentSDKProxy(SDK2AgentInternalApiProxy apiProxy, Object agentSdkImpl) {
		this.apiProxy = apiProxy;
		this.agentSdkImpl = agentSdkImpl;
	}

	@Override
	public IncomingRemoteCallTracer traceIncomingRemoteCall(String remoteMethod, String remoteService,
			String serviceEndpoint) {
		Object agentObject = apiProxy.oneAgentSDK_traceIncomingRemoteCall(agentSdkImpl, remoteMethod, remoteService,
				serviceEndpoint);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return RemoteCallServerTracerNoop.INSTANCE;
		}
		return new RemoteCallServerProxy(apiProxy, agentObject);
	}

	@Override
	public OutgoingRemoteCallTracer traceOutgoingRemoteCall(String remoteMethod, String remoteService,
			String serverEndpoint, ChannelType channelType, String remoteHost) {
		int iChannelType = -1;
		if (channelType != null) {
			iChannelType = channelType.getSDKConstant();
		}
		Object agentObject = apiProxy.oneAgentSDK_traceOutgoingRemoteCall(agentSdkImpl, remoteMethod, remoteService,
				serverEndpoint, iChannelType, remoteHost);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return RemoteCallClientTracerNoop.INSTANCE;
		}
		return new RemoteCallClientProxy(apiProxy, agentObject);
	}

	@Override
	public void setLoggingCallback(LoggingCallback loggingCallback) {
		apiProxy.oneAgentSDK_setLoggingCallback(agentSdkImpl, loggingCallback);
	}

	@Override
	public SDKState getCurrentState() {
		Boolean isCapturing = apiProxy.oneAgentSDK_isCapturing(agentSdkImpl);
		if (isCapturing == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return SDKState.PERMANENTLY_INACTIVE;
		}
		if (isCapturing.booleanValue()) {
			return SDKState.ACTIVE;
		} else {
			return SDKState.TEMPORARILY_INACTIVE;
		}
	}

	@Override
	public InProcessLink createInProcessLink() {
		Object agentProvidedLink = apiProxy.oneAgentSDK_createInProcessLink(agentSdkImpl);
		if (agentProvidedLink == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide InProcessLink");
			}
			return InProcessLinkNoop.INSTANCE;
		}
		return new InProcessLinkImpl(agentProvidedLink);
	}

	@Override
	public InProcessLinkTracer traceInProcessLink(InProcessLink inProcessLink) {
		if (inProcessLink instanceof InProcessLinkNoop) {
			return InProcessLinkTracerNoop.INSTANCE;
		} else if (!(inProcessLink instanceof InProcessLinkImpl)) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- invalid InProcessLink object provided: "
						+ (inProcessLink == null ? "null" : inProcessLink.getClass().getName()));
			}
			return InProcessLinkTracerNoop.INSTANCE;
		}

		Object agentObject = apiProxy.oneAgentSDK_traceInProcessLink(agentSdkImpl, (InProcessLinkImpl) inProcessLink);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide InProcessLinkTracer");
			}
			return InProcessLinkTracerNoop.INSTANCE;
		}
		return new InProcessLinkTracerProxy(apiProxy, agentObject);
	}

	@Override
	public void addCustomRequestAttribute(String key, String value) {
		apiProxy.oneAgentSDK_addCustomRequestAttribute(agentSdkImpl, key, value);
	}

	@Override
	public void addCustomRequestAttribute(String key, long value) {
		apiProxy.oneAgentSDK_addCustomRequestAttribute(agentSdkImpl, key, value);
	}

	@Override
	public void addCustomRequestAttribute(String key, double value) {
		apiProxy.oneAgentSDK_addCustomRequestAttribute(agentSdkImpl, key, value);
	}

	@Override
	public WebApplicationInfo createWebApplicationInfo(String webServerName, String applicationID, String contextRoot) {
		return new WebApplicationInfoImpl(webServerName, applicationID, contextRoot);
	}

	@Override
	public IncomingWebRequestTracer traceIncomingWebRequest(WebApplicationInfo webApplicationInfo, String url,
			String method) {
		if (webApplicationInfo instanceof WebApplicationInfoNoop) {
			return IncomingWebRequestTracerNoop.INSTANCE;
		} else if (!(webApplicationInfo instanceof WebApplicationInfoImpl)) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- invalid WebApplicationInfo object provided: "
						+ (webApplicationInfo == null ? "null" : webApplicationInfo.getClass().getName()));
			}
			return IncomingWebRequestTracerNoop.INSTANCE;
		}

		Object agentObject = apiProxy.oneAgentSDK_traceIncomingWebRequest(agentSdkImpl,
				(WebApplicationInfoImpl) webApplicationInfo, url, method);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return IncomingWebRequestTracerNoop.INSTANCE;
		}
		return new IncomingWebRequestProxy(apiProxy, agentObject);
	}

	@Override
	public OutgoingWebRequestTracer traceOutgoingWebRequest(String url, String method) {
		Object agentObject = apiProxy.oneAgentSDK_traceOutgoingWebRequest(agentSdkImpl, url, method);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return OutgoingWebRequestTracerNoop.INSTANCE;
		}
		return new OutgoingWebRequestTracerProxy(apiProxy, agentObject);
	}

	@Override
	public MessagingSystemInfo createMessagingSystemInfo(String vendorName, String destinationName,
			MessageDestinationType destinationType, ChannelType channelType, String channelEndpoint) {
		return new MessagingSystemInfoImpl(vendorName, destinationName, destinationType, channelType, channelEndpoint);
	}

	@Override
	public OutgoingMessageTracer traceOutgoingMessage(MessagingSystemInfo messagingSystem) {
		if (messagingSystem instanceof MessagingSystemInfoNoop) {
			return OutgoingMessageTracerNoop.INSTANCE;
		} else if (!(messagingSystem instanceof MessagingSystemInfoImpl)) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- invalid MessagingSystemInfo object provided: "
						+ (messagingSystem == null ? "null" : messagingSystem.getClass().getName()));
			}
			return OutgoingMessageTracerNoop.INSTANCE;
		}

		Object agentObject = apiProxy.oneAgentSDK_traceOutgoingMessage(agentSdkImpl,
				(MessagingSystemInfoImpl) messagingSystem);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return OutgoingMessageTracerNoop.INSTANCE;
		}
		return new OutgoingMessageTracerProxy(apiProxy, agentObject);
	}

	@Override
	public IncomingMessageReceiveTracer traceIncomingMessageReceive(MessagingSystemInfo messagingSystem) {
		if (messagingSystem instanceof MessagingSystemInfoNoop) {
			return IncomingMessageReceiveTracerNoop.INSTANCE;
		} else if (!(messagingSystem instanceof MessagingSystemInfoImpl)) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- invalid MessagingSystemInfo object provided: "
						+ (messagingSystem == null ? "null" : messagingSystem.getClass().getName()));
			}
			return IncomingMessageReceiveTracerNoop.INSTANCE;
		}
		Object agentObject = apiProxy.oneAgentSDK_traceIncomingMessageReceive(agentSdkImpl,
				(MessagingSystemInfoImpl) messagingSystem);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return IncomingMessageReceiveTracerNoop.INSTANCE;
		}
		return new IncomingMessageReceiveTracerProxy(apiProxy, agentObject);
	}

	@Override
	public IncomingMessageProcessTracer traceIncomingMessageProcess(MessagingSystemInfo messagingSystem) {
		if (messagingSystem instanceof MessagingSystemInfoNoop) {
			return IncomingMessageProcessTracerNoop.INSTANCE;
		} else if (!(messagingSystem instanceof MessagingSystemInfoImpl)) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- invalid MessagingSystemInfo object provided: "
						+ (messagingSystem == null ? "null" : messagingSystem.getClass().getName()));
			}
			return IncomingMessageProcessTracerNoop.INSTANCE;
		}
		Object agentObject = apiProxy.oneAgentSDK_traceIncomingMessageProcess(agentSdkImpl,
				(MessagingSystemInfoImpl) messagingSystem);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return IncomingMessageProcessTracerNoop.INSTANCE;
		}
		return new IncomingMessageProcessTracerProxy(apiProxy, agentObject);
	}

	@Override
	public DatabaseInfo createDatabaseInfo(String name, String vendor, ChannelType channelType,
			String channelEndpoint) {
		return new DatabaseInfoImpl(name, vendor, channelType, channelEndpoint);
	}

	@Override
	public DatabaseRequestTracer traceSqlDatabaseRequest(DatabaseInfo databaseInfo, String statement) {
		if (databaseInfo instanceof DatabaseInfoNoop) {
			return DatabaseRequestTracerNoop.INSTANCE;
		} else if (!(databaseInfo instanceof DatabaseInfoImpl)) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- invalid DatabaseInfo object provided: "
						+ (databaseInfo == null ? "null" : databaseInfo.getClass().getName()));
			}
			return DatabaseRequestTracerNoop.INSTANCE;
		}

		Object agentObject = apiProxy.oneAgentSDK_traceSQLDatabaseRequest(agentSdkImpl, (DatabaseInfoImpl) databaseInfo,
				statement);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return DatabaseRequestTracerNoop.INSTANCE;
		}
		return new DatabaseRequestTracerProxy(apiProxy, agentObject);
	}

	@Override
	public CustomServiceTracer traceCustomService(String serviceMethod, String serviceName) {
		Object agentObject = apiProxy.oneAgentSDK_traceCustomService(agentSdkImpl, serviceMethod, serviceName);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide object");
			}
			return CustomServiceTracerNoop.INSTANCE;
		}
		return new CustomServiceTracerProxy(apiProxy, agentObject);
	}

    @Override
    public TraceContextInfo getTraceContextInfo() {
        Map.Entry<String, String> traceAndSpanId = apiProxy.oneAgentSDK_getCurrentTraceAndSpanId(agentSdkImpl);
        return traceAndSpanId == null ? TraceContextInfoNoop.INSTANCE : new TraceContextInfoImpl(
                traceAndSpanId.getKey(), traceAndSpanId.getValue());
    }
}
