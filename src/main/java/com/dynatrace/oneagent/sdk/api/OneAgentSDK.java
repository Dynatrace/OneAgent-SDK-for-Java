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
package com.dynatrace.oneagent.sdk.api;

import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.api.enums.SDKState;
import com.dynatrace.oneagent.sdk.api.infos.WebApplicationInfo;

/**
 * Root interface contains provided Agent SDK API calls. Basically the whole API
 * is designed according to following rules:
 * <ul>
 * <li>API calls never throw any exception
 * <li>API calls never return null values. e. g. they returning NOOP Objects in case of any issue or required parameters are null.
 * </ul>
 * Single API calls might differ from that rules. Those rules are explicitly documented.<br>
 */
public interface OneAgentSDK {

	/**
	 * Using this headername to transport Dynatrace tag inside an outgoing http request ensures compatibility to Dynatrace built-in sensors.
	 */
	public static final String DYNATRACE_HTTP_HEADERNAME = "X-dynaTrace";
	
	// ***** Web Requests (incoming) *****
	
	/**
	 * Initializes a WebApplicationInfo instance that is required for tracing incoming web requests. This information determines the identity and name of the resulting Web Request service in dynatrace.
	 * Also see https://www.dynatrace.com/support/help/server-side-services/introduction/how-does-dynatrace-detect-and-name-services/#web-request-services for detail description of the meaning of the parameters.
	 *
	 * @param webServerName		logical name of the web server. In case of a cluster every node in the cluster must report the same name here.
	 * 							Attention: Make sure not to use the host header for this parameter. Host headers are often spoofed and contain things like google or baidoo which do not reflect your setup.
	 * @param applicationID		application ID of the web application
	 * @param contextRoot		context root of the application.
	 * 							All URLs traced with the returned WebApplicationInfo, should start with provided context root. 
	 * @return					{@link WebApplicationInfo} instance to work with
	 * @since 1.3
	 */
	WebApplicationInfo createWebApplicationInfo(String webServerName, String applicationID, String contextRoot);

	/**
	 * Traces an incoming web request.
	 *
	 * @param webApplicationInfo	information about web application
	 * @param url					(parts of a) URL, which will be parsed into: scheme, hostname/port, path & query
	 * 								Note: the hostname will be resolved by the Agent at start() call
	 * @param method				HTTP request method
	 * @return						{@link IncomingWebRequestTracer} to work with
	 * @since 1.3
	 */
	IncomingWebRequestTracer traceIncomingWebRequest(WebApplicationInfo webApplicationInfo, String url, String method);

	/**
	 * Traces an outgoing web request.
	 *
	 * @param url					URL, which will be parsed into: scheme, hostname/port, path & query
	 * 								Note: the hostname will be resolved by the Agent at start() call
	 * @param method				HTTP request method
	 * @return						{@link OutgoingWebRequestTracer} to work with
	 * @since 1.4
	 */
	OutgoingWebRequestTracer traceOutgoingWebRequest(String url, String method);

    // ***** Remote Calls (outgoing & incoming) *****

    /**
     * Traces an incoming remote call.
     *
     * @param serviceMethod
     *            name of the called remote method. (required)
     * @param serviceName
     *            name of the remote service. (required)
     * @param serviceEndpoint
     *            endpoint on the server side. (required)
     * @return {@link IncomingRemoteCallTracer} instance to work with
     * @since 1.0
     */
    IncomingRemoteCallTracer traceIncomingRemoteCall(String serviceMethod, String serviceName, String serviceEndpoint);

    /**
     * Traces an outgoing remote call.
     *
     * @param serviceMethod
     *            name of the called remote method. (required)
     * @param serviceName
     *            name of the remote service. (required)
     * @param serviceEndpoint
     *            endpoint on the server side. (required)
     * @param channelType
     *            communication protocol used by remote call. See {@link ChannelType} for 
     *            available types. (required)
     * @param channelEndpoint
     *            optional and depending on channelType:
     *            <ul>
     *            <li> for TCP/IP: host name/IP of the server-side (can include port)
     *            <li> for UNIX domain sockets: path of domain socket file
     *            <li> for named pipes: name of pipe
     *            </ul>
     * @return {@link OutgoingRemoteCallTracer} instance to work with
     * @since 1.0
     */
    OutgoingRemoteCallTracer traceOutgoingRemoteCall(String serviceMethod, String serviceName, String serviceEndpoint,
			ChannelType channelType, String channelEndpoint);

	// ***** in-process-linking *****
	/**
	 * Creates a link for in-process-linking.
	 *  
	 * @return 			{@link InProcessLink} instance to work with. Use it with {@link #traceInProcessLink(InProcessLink)}
	 * @since 1.1
	 */
	InProcessLink createInProcessLink();
	
	/**
	 * Traces the start of in-process-linking.
	 * 
	 * @param inProcessLink a InProcessLink received via {@link #createInProcessLink()}
	 * @return			{@link InProcessLinkTracer} to work with.
	 * @since 1.1
	 */
	InProcessLinkTracer traceInProcessLink(InProcessLink inProcessLink);

	// ***** Custom request attributes *****
	/**
	 * Adds a custom request attribute to currently traced service call. Might be called multiple times, to add more than one attribute.
	 * Check via {@link #setLoggingCallback(LoggingCallback)} if error happened. If two attributes with same key are set, both 
	 * attribute-values are captured. 
	 * 
	 * @param key		key of the attribute. required parameter. 
	 * @param value		value of the attribute. required parameter.
	 * @since 1.2
	 */
	void addCustomRequestAttribute(String key, String value);

	/**
	 * Does exactly the same as {@link #addCustomRequestAttribute(String, String)}, but request-attribute type long.
	 * @since 1.2
	 */
	void addCustomRequestAttribute(String key, long value);
	
	/**
	 * Does exactly the same as {@link #addCustomRequestAttribute(String, String)}, but request-attribute type double.
	 * @since 1.2
	 */
	void addCustomRequestAttribute(String key, double value);

    // ***** various *****

    /**
     * Installs a callback that gets informed, if any SDK action has failed.
     * For details see {@link LoggingCallback} interface. The provided callback must
     * be thread-safe, when using this {@link OneAgentSDK} instance in multi-threaded
     * environments.
     *
     * @param loggingCallback
     *            may be null, to remove current callback. provided callback
     *            replaces any previously set callback.
     * @since 1.0
     */
    void setLoggingCallback(LoggingCallback loggingCallback);

    /**
     * Returns the current SDKState. See {@link SDKState} for details.
     *
     * @return current state - never null.
     * @since 1.0
     */
    SDKState getCurrentState();
}
