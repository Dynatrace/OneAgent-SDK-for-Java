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
 * Represents client side of an outgoing webrequest.
 * 
 * @since 1.4
 *
 */
public interface OutgoingWebRequestTracer extends Tracer, OutgoingTaggable {

	/**
	 * All HTTP request headers should be provided to this method. Selective
	 * capturing will be done based on sensor configuration.
	 *
	 * @param name
	 *            HTTP request header field name
	 * @param value
	 *            HTTP request header field value
	 */
	void addRequestHeader(String name, String value);

	/**
	 * All HTTP response headers returned by the server should be provided to this
	 * method. Selective capturing will be done based on sensor configuration.
	 *
	 * @param name
	 *            HTTP response header field name
	 * @param value
	 *            HTTP response header field value
	 */
	void addResponseHeader(String name, String value);

	/**
	 * Sets the HTTP response status code.
	 *
	 * @param statusCode
	 *            HTTP status code retrieved from server
	 */
	void setStatusCode(int statusCode);

}
