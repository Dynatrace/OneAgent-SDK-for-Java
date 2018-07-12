/*
 * Copyright 2018 Dynatrace LLC
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

import com.dynatrace.oneagent.sdk.api.InProcessLink;
import com.dynatrace.oneagent.sdk.api.InProcessLinkTracer;
import com.dynatrace.oneagent.sdk.api.IncomingRemoteCallTracer;
import com.dynatrace.oneagent.sdk.api.LoggingCallback;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.OutgoingRemoteCallTracer;
import com.dynatrace.oneagent.sdk.api.enums.SDKState;
import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.impl.OneAgentSDKFactoryImpl;
import com.dynatrace.oneagent.sdk.impl.noop.InProcessLinkNoop;
import com.dynatrace.oneagent.sdk.impl.noop.InProcessLinkTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.RemoteCallClientTracerNoop;
import com.dynatrace.oneagent.sdk.impl.noop.RemoteCallServerTracerNoop;

/** TODO: check if/how class could be generated */
public class OneAgentSDKProxy implements OneAgentSDK {

    private final SDK2AgentInternalApiProxy apiProxy;
    private final Object agentSdkImpl;

    public OneAgentSDKProxy(SDK2AgentInternalApiProxy apiProxy, Object agentSdkImpl) {
        this.apiProxy = apiProxy;
        this.agentSdkImpl = agentSdkImpl;
    }

    @Override
    public IncomingRemoteCallTracer traceIncomingRemoteCall(String remoteMethod, String remoteService, String serviceEndpoint) {
        Object agentObject = apiProxy.oneAgentSDK_traceIncomingRemoteCall(agentSdkImpl, remoteMethod, remoteService, serviceEndpoint);
        if (agentObject == null) {
            if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
                OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide provide object");
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
        Object agentObject = apiProxy.oneAgentSDK_traceOutgoingRemoteCall(agentSdkImpl, remoteMethod, remoteService, serverEndpoint, iChannelType, remoteHost);
        if (agentObject == null) {
            if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
                OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide provide object");
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
                OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide provide object");
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
				OneAgentSDKFactoryImpl.logDebug("- invalid InProcessLink object provided: " + (inProcessLink == null ? "null" : inProcessLink.getClass().getName()));
			}
			return InProcessLinkTracerNoop.INSTANCE;
		}
		
		Object agentObject = apiProxy.oneAgentSDK_traceInProcessLink(agentSdkImpl, (InProcessLinkImpl) inProcessLink);
		if (agentObject == null) {
			if (OneAgentSDKFactoryImpl.debugOneAgentSdkStub) {
				OneAgentSDKFactoryImpl.logDebug("- OneAgent failed to provide provide InProcessLinkTracer");
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

}
