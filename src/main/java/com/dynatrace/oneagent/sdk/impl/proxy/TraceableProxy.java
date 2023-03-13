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

import com.dynatrace.oneagent.sdk.api.Tracer;

abstract class TraceableProxy implements Tracer {

	protected final SDK2AgentInternalApiProxy apiProxy;
	protected final Object agentsNodeObject;

	TraceableProxy(SDK2AgentInternalApiProxy apiProxy, Object agentsNodeObject) {
		this.apiProxy = apiProxy;
		this.agentsNodeObject = agentsNodeObject;
	}

	@Override
	public void start() {
		apiProxy.tracer_start(agentsNodeObject);

	}

	@Override
	public void end() {
		apiProxy.tracer_end(agentsNodeObject);

	}

	@Override
	public void error(String message) {
		apiProxy.tracer_error(agentsNodeObject, message);

	}

	@Override
	public void error(Throwable throwable) {
		apiProxy.tracer_error(agentsNodeObject, throwable);
	}

}
