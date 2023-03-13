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
package com.dynatrace.oneagent.sdk.api;

/**
 * Represents the client side of a remote call.
 *
 * This Interface extends {@link Tracer} - it is important to respect the
 * mentioned requirements when working with {@link OutgoingRemoteCallTracer}.
 *
 */
public interface OutgoingRemoteCallTracer extends Tracer, OutgoingTaggable {

	/**
	 * Sets the name of the used remoting protocol. Setting the protocol name is
	 * optional.
	 *
	 * @param protocolName
	 *            protocol name. null value is ignored.
	 * @since 1.0
	 */
	void setProtocolName(String protocolName);

}
