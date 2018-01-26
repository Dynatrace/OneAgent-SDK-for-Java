**Disclaimer: This SDK is currently in early access and still work in progress.**

# Dynatrace OneAgent SDK for Java

This SDK allows Dynatrace customers to instrument java applications. This is useful to enhance the visibility for proprietary frameworks or custom frameworks not directly supported by Dynatrace OneAgent out-of-the-box.

It provides the Java implementation of the [Dynatrace OneAgent SDK](https://github.com/Dynatrace/OneAgent-SDK). 

## Package contents

- `samples`: contains sample application, which demonstrates the usage of the SDK. see readme inside the samples directory for more details
- `LICENSE`: license under which the whole SDK and sample applications are published

## Features
Dynatrace OneAgent SDK for Java currently implements support for the following features (corresponding to features specified in [Dynatrace OneAgent SDK](https://github.com/Dynatrace/OneAgent-SDK)):
-  outgoing and incoming remote calls

## Integrating into your application

### Dependencies
If you want to integrate the OneAgent SDK into your application, just add the following maven dependency:

	<dependency>
	  <groupId>com.dynatrace.oneagent.sdk.java</groupId>
	  <artifactId>oneagent-sdk</artifactId>
	  <version>1.0.3</version>
	  <scope>compile</scope>
	</dependency>

If you prefer to integrate the SDK using plain jar file, just download them from mavenCentral - Dynatrace OneAgent SDK for Java [binary](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3.jar), [source](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-sources.jar) and [javadoc](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-javadoc.jar)

The Dynatrace OneAgent SDK for Java has no further dependencies.

### Troubleshooting
If the SDK can't connect to the OneAgent (see usage of SDKState in samples) or you you don't see the desired result in the Dynatrace UI, you can set the following system property to print debug information to standard out:
	
	-Dcom.dynatrace.oneagent.sdk.debug=true

Additionally you should/have to ensure, that you have set a `LoggingCallback`. For usage see class `StdErrLoggingCallback` in `remotecall-server` module (in samples/remotecall folder).

## OneAgent SDK for Java Requirements

- JRE 1.6 or higher
- Dynatrace OneAgent Java (supported versions see below)

# API Concepts

## Common concepts: Tracers

To trace any kind of call you first need to create a Tracer. The Tracer object represents the logical and physical endpoint that you want to call. A Tracer serves two purposes. First to time the call (duraction, cpu and more) and report errors. That is why each Tracer has these three methods. The error method must be called only once, and it must be in between start and end.

{code}
	void start();

	void error(String message);

	void end();
{code}

The second purpose of a Tracer is to allow tracing across process boundaries. To achieve that these kind of traces supply so called tags. Tags are strings or byte arrays that enable Dynatrace to trace a transaction end to end. As such the tag is the one information that you need to transport across these calls yourselfs.


## Using the Dynatrace OneAgent SDK to trace remote calls

You can use the SDK to trace proprietary IPC communication from one process to the other. This will enable you to see full Service Flow, PurePath and Smartscape topology for remoting technologies that Dynatrace is not aware of.

To trace any kind of remote call you first need to create a Tracer. The Tracer object represents the endpoint that you want to call, as such you need to supply the name of the remote service and remote method. In addition you need to transport the tag in your remote call to the server side if you want to trace it end to end.

{code}
	OutgoingRemoteCallTracer outgoingRemoteCall = OneAgentSDK.traceOutgoingRemoteCall("remoteMethodToCall", "RemoteServiceName", "rmi://Endpoint/service", ChannelType.TCP_IP, "remoteHost:1234");
	outgoingRemoteCall.setProtocolName("RMI/custom");
	outgoingRemoteCall.start();
		try {
			String tag = outgoingRemoteCall.getDynatraceStringTag();
			// make the call and transport the tag across to server
		} catch (Throwable e) {
			outgoingRemoteCall.error(e);
		} finally {
			outgoingRemoteCall.end();
		}
{code}

On the server side you need to wrap the handling and processing of your remote call as well. This will not only trace the server side call and everything that happens, it will also connect it to the calling side.

{code}
	IncomingRemoteCallTracer incomingRemoteCall = OneAgentSDK.traceIncomingRemoteCall("remoteMethodToCall", "RemoteServiceName", "rmi://Endpoint/service");
	incomingRemoteCall.setDynatraceStringTag(tag);
	incomingRemoteCall.start();
	try {
		incomingRemoteCall.setProtocolName("RMI/custom");
		doSomeWork(); // process the remoteCall
	} catch (Exception e) {
		incomingRemoteCall.error(e);
	}finally{
		incomingRemoteCall.end();
	}
{code}

### Compatibility OneAgent SDK for Java releases with OneAgent for Java releases
|OneAgent SDK for Java|Dynatrace OneAgent Java|
|:------|:--------|
|1.0.3  |>=1.135  |

## Feedback

In case of questions, issues or feature requests feel free to contact [Michael Kopp](https://github.com/mikopp), [Alram Lechner](https://github.com/AlramLechnerDynatrace) or file an issue. Your feedback is welcome!


## OneAgent SDK for Java release notes
|Version|Date|Description|
|:------|:----------|:------------------|
|1.0.3  |01.2018    |Initial release    |
