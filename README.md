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

If you prefer to integrate the SDK using plain jar file, just download them from mavenCentral - Dynatrace OneAgent SDK for Java [binary](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3.jar), [source](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-sources.jar) or [javadoc](https://search.maven.org/remotecontent?filepath=com/dynatrace/oneagent/sdk/java/oneagent-sdk/1.0.3/oneagent-sdk-1.0.3-javadoc.jar)

The Dynatrace OneAgent SDK for Java has no further dependencies.

### Troubleshooting
If the SDK can't connect to the OneAgent (see usage of SDKState in samples) or you you don't see the desired result in the Dynatrace UI, you can set the following system property to print debug information to standard out:
	
	-Dcom.dynatrace.oneagent.sdk.debug=true

Additionally you should/have to ensure, that you have set a `LoggingCallback`. For usage see class `StdErrLoggingCallback` in `remotecall-server` module (in samples/remotecall folder).

## OneAgent SDK for Java Requirements

- JRE 1.6 or higher
- Dynatrace OneAgent Java (supported versions see below)

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
