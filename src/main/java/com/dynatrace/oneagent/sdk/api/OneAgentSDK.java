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

import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.api.enums.MessageDestinationType;
import com.dynatrace.oneagent.sdk.api.enums.SDKState;
import com.dynatrace.oneagent.sdk.api.infos.DatabaseInfo;
import com.dynatrace.oneagent.sdk.api.infos.MessagingSystemInfo;
import com.dynatrace.oneagent.sdk.api.infos.TraceContextInfo;
import com.dynatrace.oneagent.sdk.api.infos.WebApplicationInfo;

/**
 * Root interface contains provided Agent SDK API calls. Basically the whole API
 * is designed according to following rules:
 * <ul>
 * <li>API calls never throw any exception
 * <li>API calls never return null values. e. g. they returning NOOP Objects in
 * case of any issue or required parameters are null.
 * </ul>
 * Single API calls might differ from that rules. Those rules are explicitly
 * documented.<br>
 */
public interface OneAgentSDK {

	/**
	 * Using this headername to transport Dynatrace tag inside an outgoing http
	 * request ensures compatibility to Dynatrace built-in sensors.
	 */
	public static final String DYNATRACE_HTTP_HEADERNAME = "X-dynaTrace";
	
	/**
	 * Using this propertyname to transport Dynatrace tag along with the message, ensures compatibility to Dynatrace built-in sensors.
	 */
	public static final String DYNATRACE_MESSAGE_PROPERTYNAME = "dtdTraceTagInfo";

	// ***** outgoing Database *****
	
	/**
	 * Initializes a DatabaseInfo instance that is required for tracing database requests.
	 *
	 * @param name				name of the database
	 * @param vendor			database vendor name (e.g. Oracle, MySQL, ...), can be a user defined name
	 *                          If possible use a constant defined in {@link com.dynatrace.oneagent.sdk.api.enums.DatabaseVendor}
	 * @param channelType		communication protocol used to communicate with the database.
	 * @param channelEndpoint	this represents the communication endpoint for the database. This information allows Dynatrace to tie the database requests to a specific process or cloud service. It is optional.
	 * 							* for TCP/IP: host name/IP of the server-side (can include port in the form of "host:port") 
	 * 							* for UNIX domain sockets: name of domain socket file
	 * 							* for named pipes: name of pipe
	 * @return					{@link DatabaseInfo} instance to work with
	 * @since 1.7.0
	 */
	DatabaseInfo createDatabaseInfo(String name, String vendor, ChannelType channelType, String channelEndpoint);

	/**
	 * Creates a tracer for tracing outgoing SQL database requests.
	 *
	 * @param databaseInfo			information about database
	 * @param statement				database SQL statement
	 * @return						{@link DatabaseRequestTracer} to work with
	 * @since 1.7.0
	 */
	DatabaseRequestTracer traceSqlDatabaseRequest(DatabaseInfo databaseInfo, String statement);
	
	// ***** Web Requests (incoming) *****

	/**
	 * Initializes a WebApplicationInfo instance that is required for tracing
	 * incoming web requests. This information determines the identity and name of
	 * the resulting Web Request service in dynatrace. Also see
	 * https://www.dynatrace.com/support/help/server-side-services/introduction/how-does-dynatrace-detect-and-name-services/#web-request-services
	 * for detail description of the meaning of the parameters.
	 *
	 * @param webServerName
	 *            logical name of the web server. In case of a cluster every node in
	 *            the cluster must report the same name here. Attention: Make sure
	 *            not to use the host header for this parameter. Host headers are
	 *            often spoofed and contain things like google or baidoo which do
	 *            not reflect your setup.
	 * @param applicationID
	 *            application ID of the web application
	 * @param contextRoot
	 *            context root of the application. All URLs traced with the returned
	 *            WebApplicationInfo, should start with provided context root.
	 * @return {@link WebApplicationInfo} instance to work with
	 * @since 1.3
	 */
	WebApplicationInfo createWebApplicationInfo(String webServerName, String applicationID, String contextRoot);

	/**
	 * Traces an incoming web request.
	 *
	 * @param webApplicationInfo
	 *            information about web application
	 * @param url
	 *            (parts of a) URL, which will be parsed into: scheme,
	 *            hostname/port, path & query Note: the hostname will be resolved by
	 *            the Agent at start() call
	 * @param method
	 *            HTTP request method
	 * @return {@link IncomingWebRequestTracer} to work with
	 * @since 1.3
	 */
	IncomingWebRequestTracer traceIncomingWebRequest(WebApplicationInfo webApplicationInfo, String url, String method);

	/**
	 * Traces an outgoing web request.
	 *
	 * @param url
	 *            URL, which will be parsed into: scheme, hostname/port, path &
	 *            query Note: the hostname will be resolved by the Agent at start()
	 *            call
	 * @param method
	 *            HTTP request method
	 * @return {@link OutgoingWebRequestTracer} to work with
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
	 *            communication protocol used by remote call. See
	 *            {@link ChannelType} for available types. (required)
	 * @param channelEndpoint
	 *            optional and depending on channelType:
	 *            <ul>
	 *            <li>for TCP/IP: host name/IP of the server-side (can include port)
	 *            <li>for UNIX domain sockets: path of domain socket file
	 *            <li>for named pipes: name of pipe
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
	 * @return {@link InProcessLink} instance to work with. Use it with
	 *         {@link #traceInProcessLink(InProcessLink)}
	 * @since 1.1
	 */
	InProcessLink createInProcessLink();

	/**
	 * Traces the start of in-process-linking.
	 * 
	 * @param inProcessLink
	 *            a InProcessLink received via {@link #createInProcessLink()}
	 * @return {@link InProcessLinkTracer} to work with.
	 * @since 1.1
	 */
	InProcessLinkTracer traceInProcessLink(InProcessLink inProcessLink);

	// ***** Custom request attributes *****
	/**
	 * Adds a custom request attribute to currently traced service call. Might be
	 * called multiple times, to add more than one attribute. Check via
	 * {@link #setLoggingCallback(LoggingCallback)} if error happened. If two
	 * attributes with same key are set, both attribute-values are captured.
	 * 
	 * @param key
	 *            key of the attribute. required parameter.
	 * @param value
	 *            value of the attribute. required parameter.
	 * @since 1.2
	 */
	void addCustomRequestAttribute(String key, String value);

	/**
	 * Does exactly the same as {@link #addCustomRequestAttribute(String, String)},
	 * but request-attribute type long.
	 * 
	 * @since 1.2
	 */
	void addCustomRequestAttribute(String key, long value);

	/**
	 * Does exactly the same as {@link #addCustomRequestAttribute(String, String)},
	 * but request-attribute type double.
	 * 
	 * @since 1.2
	 */
	void addCustomRequestAttribute(String key, double value);

	// ***** Messaging (outgoing & incoming) *****

	/**
	 * Initializes a MessagingSystemInfo instance that is required for tracing messages.
	 *
	 * @param vendorName	one of {@link com.dynatrace.oneagent.sdk.api.enums.MessageSystemVendor} if well known vendor. Custom provided in any other case.
	 * @param destinationName	destination name (e.g. queue name, topic name)
	 * @param destinationType	destination type - see {@link MessageDestinationType}.
	 * @param channelType		communication protocol used
	 * @param channelEndpoint	optional and depending on protocol:
	 * 							* for TCP/IP: host name/IP of the server-side (can include port)
	 * 							* for UNIX domain sockets: name of domain socket file
	 * 							* for named pipes: name of pipe
	 * @return					{@link MessagingSystemInfo} instance to work with
	 * 
	 * @since 1.5
	 */
	MessagingSystemInfo createMessagingSystemInfo(String vendorName, String destinationName, MessageDestinationType destinationType, ChannelType channelType, String channelEndpoint);

	/**
	 * Creates a tracer for an outgoing asynchronous message (send).
	 * 
	 * @param messagingSystem	information about the messaging system (see createMessagingSystemInfo methods).
	 * @return {@link OutgoingMessageTracer} to work with
	 * 
	 * @since 1.5
	 */
	OutgoingMessageTracer traceOutgoingMessage(MessagingSystemInfo messagingSystem);

	/**
	 * Creates a tracer for an incoming asynchronous message (blocking receive).
	 * 
	 * @param messagingSystem	information about the messaging system (see createMessagingSystemInfo methods).
	 * @return {@link IncomingMessageReceiveTracer} to work with
	 * 
	 * @since 1.5
	 */
	IncomingMessageReceiveTracer traceIncomingMessageReceive(MessagingSystemInfo messagingSystem);
	
	/**
	 * Creates a tracer for processing (consuming) a received message (onMessage).
	 * 
	 * @param messagingSystem	information about the messaging system (see createMessagingSystemInfo methods).
	 * @return {@link IncomingMessageProcessTracer} to work with
	 * 
	 * @since 1.5
	 */
	IncomingMessageProcessTracer traceIncomingMessageProcess(MessagingSystemInfo messagingSystem);
	
	// ***** various *****

	/**
	 * Installs a callback that gets informed, if any SDK action has failed. For
	 * details see {@link LoggingCallback} interface. The provided callback must be
	 * thread-safe, when using this {@link OneAgentSDK} instance in multi-threaded
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

	/**
	 * Creates a tracer for a custom transaction (Dynatrace calls them Custom service). Used whenever a transaction 
	 * should be traced, that does not match any of the specialised transaction types (e. g. DB-request, webrequest, ...).
	 * 
	 * @param serviceMethod service method being used for service creation.
	 * @param serviceName service name being used for service creation.
	 * 
	 * @return {@link CustomServiceTracer} to work with
	 * @since 1.8
	 */
	CustomServiceTracer traceCustomService(String serviceMethod, String serviceName);

    /**
     * Returns the current W3C trace context for log enrichment
     * (not meant for tagging and context-propagation but for log-enrichment).
     * See {@link TraceContextInfo} for details.
     *
     * @return current trace context at the time of the call - never null (but may contain all-zero ID).
     * @since 1.9
     */
    TraceContextInfo getTraceContextInfo();

}
