# Dynatrace OneAgent SDK for Java

This SDK allows Dynatrace customers to instrument java applications. This is useful for technologies where no out of the box sensor
is provided or in customer application are monitored.
It provides the java implementation of the [Dynatrace OneAgent SDK](https://github.com/Dynatrace/OneAgent-SDK). 

## Package contents

- `samples`: contains sample application, which demonstrates the usage of the SDK. see readme inside the samples directory for more details
- `LICENSE`: license under which the whole SDK and sample applications are published

## Features
Dynatrace OneAgent SDK for Java adds support for following features (corresponding to features specified in [Dynatrace OneAgent SDK](https://github.com/Dynatrace/OneAgent-SDK)):
-  outgoing and incoming remote call

## Integrating into your application

### Dependencies
If you want to integrate the OneAgent SDK into your application, just add the following maven dependency:

	<dependency>
	  <groupId>com.dynatrace.oneagent.sdk.java</groupId>
	  <artifactId>oneagent-sdk</artifactId>
	  <version>1.0.1</version>
	  <scope>compile</scope>
	</dependency>

FIXME: provide gradle / ivy / SBT links 

If you prefer to integrate the SDK using plain jar file, just download them from mavenCentral: FIXME: add link to mavenCentral

The Dynatrace OneAgent SDK for Java has no further dependencies.

### Troubleshooting
As long as the SDK can't connect to agent (see usage of SDKState in samples), you might set the following system property to print debug information to standard out:
	
	-Dcom.dynatrace.oneagent.sdk.debug=true

As soon as SDK is active, but no paths are shown in Dynatrace UI, enable the agent debug flag:
	
	debugOneAgentSdkJava=true

This will provide SDK related debug information in agent log. Additionally ensure, that you have set an `LoggingCallback` in your application. For usage see class `StdErrLoggingCallback` in `remotecall-server` module (in samples/remotecall folder).

## OneAgent SDK for Java Requirements

- JRE 1.6 or higher
- Dynatrace OneAgent Java (supported versions see below)

### Compatibility OneAgent SDK for Java releases with OneAgent for Java releases
|OneAgent SDK for Java|Dynatrace OneAgent Java|
|:------|:--------|
|1.0.1  |>=1.135  |

## Feedback

In case of questions, issues or feature requests feel free to contact [Michael Kopp](https://github.com/mikopp) or [Alram Lechner](https://github.com/AlramLechnerDynatrace). Your feedback is welcome!


## OneAgent SDK for Java release notes
|Version|Date|Description|
|:------|:----------|:------------------|
|1.0.1  |01.2018    |Initial release    |
