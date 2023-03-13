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

import com.dynatrace.oneagent.sdk.api.OutgoingRemoteCallTracer;

final class RemoteCallClientProxy extends TraceableProxy implements OutgoingRemoteCallTracer {

	RemoteCallClientProxy(SDK2AgentInternalApiProxy apiProxy, Object oneAgentSDK_createLocalOutgoingRemoteCall) {
		super(apiProxy, oneAgentSDK_createLocalOutgoingRemoteCall);
	}

	@Override
	public String getDynatraceStringTag() {
		return apiProxy.outgoingTaggable_getDynatraceStringTag(agentsNodeObject);
	}

	@Override
	public byte[] getDynatraceByteTag() {
		return apiProxy.outgoingTaggable_getDynatraceByteTag(agentsNodeObject);
	}

	@Override
	public void setProtocolName(String protocolName) {
		apiProxy.outgoingRemoteCallTracer_setProtocolName(agentsNodeObject, protocolName);
	}

}
