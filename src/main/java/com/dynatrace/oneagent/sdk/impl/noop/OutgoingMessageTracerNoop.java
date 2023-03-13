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

import com.dynatrace.oneagent.sdk.api.OutgoingMessageTracer;

public final class OutgoingMessageTracerNoop extends NodeNoop implements OutgoingMessageTracer {

	public static final OutgoingMessageTracerNoop INSTANCE = new OutgoingMessageTracerNoop();
	
	private OutgoingMessageTracerNoop() {
	}
	
	@Override
	public void start() {
	}

	@Override
	public void end() {
	}

	@Override
	public void error(String message) {
	}

	@Override
	public void error(Throwable throwable) {
	}

	@Override
	public String getDynatraceStringTag() {
		return NO_TAG_STRING;
	}

	@Override
	public byte[] getDynatraceByteTag() {
		return NO_TAG_BLOB;
	}

	@Override
	public void setVendorMessageId(String vendorMessageId) {
		
	}

	@Override
	public void setCorrelationId(String correlationId) {
	}

}
