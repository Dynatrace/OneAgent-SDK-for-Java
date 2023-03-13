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
 * Interface for outgoing message tracer.
 * <a href="https://github.com/Dynatrace/OneAgent-SDK#messaging">https://github.com/Dynatrace/OneAgent-SDK#messaging</a>
 * 
 * @since 1.5
 */
public interface OutgoingMessageTracer extends Tracer, OutgoingTaggable {

	/**
	 * Adds optional information about a traced message: message id provided by messaging system.
	 *  
	 * @param vendorMessageId the messageId
	 */
	public void setVendorMessageId(String vendorMessageId);

	/**
	 * Adds optional information about a traced message: correlation id used by messaging system.
	 *  
	 * @param correlationId correlationId
	 */
	public void setCorrelationId(String correlationId);

}
