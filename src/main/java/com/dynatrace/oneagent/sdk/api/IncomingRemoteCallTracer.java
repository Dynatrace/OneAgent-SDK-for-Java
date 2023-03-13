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
 * Represents the server side of a remote call. This Interface extends
 * {@link Tracer} - it is important to respect the mentioned requirements when
 * working with {@link IncomingRemoteCallTracer}.
 *
 */

public interface IncomingRemoteCallTracer extends Tracer, IncomingTaggable {

	/**
	 * Sets the name of the used remoting protocol.
	 *
	 * @param protocolName
	 *            protocol name
	 * @since 1.0
	 */
	void setProtocolName(String protocolName);

}
