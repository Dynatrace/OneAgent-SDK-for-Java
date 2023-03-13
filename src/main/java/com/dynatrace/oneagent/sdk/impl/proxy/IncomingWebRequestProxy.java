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

import com.dynatrace.oneagent.sdk.api.IncomingWebRequestTracer;

final class IncomingWebRequestProxy extends TraceableProxy implements IncomingWebRequestTracer {

	IncomingWebRequestProxy(SDK2AgentInternalApiProxy apiProxy, Object agentObject) {
		super(apiProxy, agentObject);
	}

	@Override
	public void setDynatraceStringTag(String tag) {
		apiProxy.incomingTaggable_setDynatraceStringTag(agentsNodeObject, tag);
	}

	@Override
	public void setDynatraceByteTag(byte[] tag) {
		apiProxy.incomingTaggable_setDynatraceByteTag(agentsNodeObject, tag);
	}

	@Override
	public void setRemoteAddress(String remoteAddress) {
		apiProxy.incomingWebRequestTracer_setRemoteAddress(agentsNodeObject, remoteAddress);

	}

	@Override
	public void addRequestHeader(String name, String value) {
		apiProxy.webRequestTracer_addRequestHeader(agentsNodeObject, name, value);
	}

	@Override
	public void addParameter(String name, String value) {
		apiProxy.incomingWebRequestTracer_addParameter(agentsNodeObject, name, value);
	}

	@Override
	public void addResponseHeader(String name, String value) {
		apiProxy.webRequestTracer_addResponseHeader(agentsNodeObject, name, value);
	}

	@Override
	public void setStatusCode(int statusCode) {
		apiProxy.webRequestTracer_setStatusCode(agentsNodeObject, statusCode);
	}

}
