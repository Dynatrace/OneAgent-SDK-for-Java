**Disclaimer: This SDK is currently in beta and still work in progress.**

# Dynatrace OneAgent SDK for Java

This SDK allows Dynatrace customers to instrument java applications. This is useful to enhance the visibility for proprietary frameworks or custom frameworks not directly supported by Dynatrace OneAgent out-of-the-box.

This is the official Java implementation of the [Dynatrace OneAgent SDK](https://github.com/Dynatrace/OneAgent-SDK). 

## Package contents

- `samples`: contains sample application, which demonstrates the usage of the SDK. see readme inside the samples directory for more details
- `docs`: contains the reference documentation (javadoc). The most recent version is also available online at [https://dynatrace.github.io/OneAgent-SDK-for-Java/](https://dynatrace.github.io/OneAgent-SDK-for-Java/).
- `LICENSE`: license under which the whole SDK and sample applications are published

## Requirements

- JRE 1.6 or higher
- Dynatrace OneAgent (required versions see below)

|OneAgent SDK for Java|Dynatrace OneAgent Java|
|:------|:--------|
|1.1.0  |>=1.143  |
|1.0.3  |>=1.135  |


## Features
Dynatrace OneAgent SDK for Java currently implements support for the following features (corresponding to features specified in [Dynatrace OneAgent SDK](https://github.com/Dynatrace/OneAgent-SDK)):
-  outgoing and incoming remote calls

## Integrating into your application

### Dependencies
If you want to integrate the OneAgent SDK into your application, just add the following maven dependency:

	<dependency>
	  <groupId>com.dynatrace.oneagent.sdk.java</groupId>
	  <artifactId>oneagent-sdk</artifactId>
	  <version>1.1.0</version>
	  <scope>compile</scope>
	</dependency>

If you prefer to integrate the SDK using plain jar file, just download them from mavenCentral - Dynatrace OneAgent SDK for Java [binary](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3.jar), [source](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-sources.jar) and [javadoc](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-javadoc.jar)

The Dynatrace OneAgent SDK for Java has no further dependencies.

### Troubleshooting
If the SDK can't connect to the OneAgent (see usage of SDKState in samples) or you you don't see the desired result in the Dynatrace UI, you can set the following system property to print debug information to standard out:
	
	-Dcom.dynatrace.oneagent.sdk.debug=true

Additionally you should/have to ensure, that you have set a `LoggingCallback`. For usage see class `StdErrLoggingCallback` in `remotecall-server` module (in samples/remotecall folder).



# API Concepts

Common concepts of the Dynatrace OneAgent SDK are explained the [Dynatrace OneAgent SDK repository](https://github.com/Dynatrace/OneAgent-SDK).

## Get an Api object

Use OneAgentSDKFactory.createInstance() to obtain an OneAgentSDK instance. You should reuse this object over the whole application 
and if possible JVM lifetime:

```Java
OneAgentSDK oneAgentSdk = OneAgentSDKFactory.createInstance();
switch (oneAgentSdk.getCurrentState()) {
case ACTIVE:
	break;
case PERMANENTLY_INACTIVE:
	break;
case TEMPORARILY_INACTIVE:
	break;
default:
	break;
}
```

It is good practice to check the SDK state regularly as it may change at every point of time (except PERMANENTLY_INACTIVE never changes over JVM lifetime).

## Common concepts: Tracers

To trace any kind of call you first need to create a Tracer. The Tracer object represents the logical and physical endpoint that you want to call. A Tracer serves two purposes. First to time the call (duraction, cpu and more) and report errors. That is why each Tracer has these three methods. The error method must be called only once, and it must be in between start and end.

```Java
void start();

void error(String message);

void end();
```
The second purpose of a Tracer is to allow tracing across process boundaries. To achieve that these kind of traces supply so called tags. Tags are strings or byte arrays that enable Dynatrace to trace a transaction end to end. As such the tag is the one information that you need to transport across these calls yourselfs.


## Trace incoming and outgoing remote calls

You can use the SDK to trace proprietary IPC communication from one process to the other. This will enable you to see full Service Flow, PurePath and Smartscape topology for remoting technologies that Dynatrace is not aware of.

To trace any kind of remote call you first need to create a Tracer. The Tracer object represents the endpoint that you want to call, as such you need to supply the name of the remote service and remote method. In addition you need to transport the tag in your remote call to the server side if you want to trace it end to end.

```Java
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
```

On the server side you need to wrap the handling and processing of your remote call as well. This will not only trace the server side call and everything that happens, it will also connect it to the calling side.

```Java
OneAgentSDK oneAgentSdk = OneAgentSDKFactory.createInstance();
IncomingRemoteCallTracer incomingRemoteCall = oneAgentSdk.traceIncomingRemoteCall("remoteMethodToCall", "RemoteServiceName", "rmi://Endpoint/service");
incomingRemoteCall.setDynatraceStringTag(tag);
incomingRemoteCall.start();
try {
	incomingRemoteCall.setProtocolName("RMI/custom");
	doSomeWork(); // process the remoteCall
} catch (Exception e) {
	incomingRemoteCall.error(e);
	// rethrow or add your exception handling
} finally{
	incomingRemoteCall.end();
}
```

## Link in process

You can use the SDK to link inside a single process. To link for eg. an asynchronous execution, you need the following code:
```Java
OneAgentSDK oneAgentSdk = OneAgentSDKFactory.createInstance();
InProcessLink inProcessLink = sdk.createInProcessLink();
```

Provide the returned inProcessLink to the code, that does the asynchronous execution:

```Java
OneAgentSDK sdk = OneAgentSDKFactory.createInstance();
InProcessLinkTracer inProcessLinkTracer = sdk.traceInProcessLink(inProcessLink);
inProcessLinkTracer.start();
try {
	// do the work ...
} catch (Exception e) {
	inProcessLinkTracer.error(e);
	// rethrow or add your exception handling
} finally {
	inProcessLinkTracer.end();
}
```



## Feedback

In case of questions, issues or feature requests feel free to contact the maintainer by dynatrace.oneagent.sdk(at)dynatrace(dot)com or file an issue. Your feedback is welcome!

## OneAgent SDK for Java release notes
|Version|Description|Links|
|:------|:--------------------------------------|:----------------------------------------|
|1.1.0  |Added support for in-process-linking   |[binary](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.1.0/oneagent-sdk-1.1.0.jar) [source](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-sources.jar) [javadoc](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.1.0-javadoc.jar)|
|1.0.3  |Initial release                        |[binary](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3.jar) [source](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-sources.jar) [javadoc](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-javadoc.jar)|
