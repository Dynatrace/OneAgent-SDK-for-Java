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
 * Interface for incoming webrequest tracer. <a href=
 * "https://github.com/Dynatrace/OneAgent-SDK#webrequests">https://github.com/Dynatrace/OneAgent-SDK#webrequests</a>
 * 
 * @since 1.3
 */
public interface IncomingWebRequestTracer extends Tracer, IncomingTaggable {

	/**
	 * Validates and sets the remote IP address of the incoming web request. This
	 * information is very useful to gain information about Load balancers, Proxies
	 * and ultimately the end user that is sending the request.
	 *
	 * @param remoteAddress
	 *            remote IP address
	 */
	void setRemoteAddress(String remoteAddress);

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
	 * All HTTP parameters should be provided to this method. Selective capturing
	 * will be done based on sensor configuration.
	 *
	 * @param name
	 *            HTTP parameter name
	 * @param value
	 *            HTTP parameter value
	 */
	void addParameter(String name, String value);

	/**
	 * All HTTP response headers should be provided to this method. Selective
	 * capturing will be done based on sensor configuration.
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
	 *            HTTP status code returned to client
	 */
	void setStatusCode(int statusCode);

}
