# OneAgent SDK for Java sample applications

Sample applications showing how to use Dynatrace OneAgent SDK for Java to create custom specific PurePaths.

## contents

- remotecall shows usage of remote call API. allows you to tag remote calls in same or between different JVM's.  

## Build and prepare running sample applications

- ensure you have Apache Maven 3.5 installed, see: [Apache Maven](https://maven.apache.org/)
- ensure Dynatrace OneAgent is installed. if not see our [free Trial](https://www.dynatrace.com/)
- clone this repository
- run `mvn package` in root directory of the desired sample

### Run RemoteCall sample application
This Application shows how to trace remote calls and tag them. To run this sample you need to start a server and client sample application - both with Dynatrace agent injected.

- Server: `mvn -pl remotecall-server exec:exec`

- Client: `mvn -pl remotecall-client exec:exec`

Check your Dynatrace environment for newly created services.

## Integrating into your application
If you want to integrate the OneAgent SDK into your application, just add the following maven dependency:

	<dependency>
	  <groupId>com.dynatrace.oneagent.sdk.java</groupId>
	  <artifactId>oneagent-sdk</artifactId>
	  <version>1.0.1</version>
	  <scope>compile</scope>
	</dependency>

If you prefer to integrate the SDK using plain jar file, just download them from mavenCentral: FIXME: add link to mavenCentral 

## Troubleshooting
As long as the SDK can't connect to agent (see output of sample), you might set the following system property to print debug information to standard out:
	
	-Dcom.dynatrace.oneagent.sdk.debug=true

As soon as SDK is active, but no paths are shown in Dynatrace UI, enable the agent debug flag:
	
	debugOneAgentSdkJava=true

This will provide additional debug information in agent log.

Additionally ensure, that you have set an `LoggingCallback` in your application. For usage see class `StdErrLoggingCallback` in `remotecall-server` module.

## OneAgent SDK for Java Requirements
- JRE 1.6 or higher
- Dynatrace OneAgent Java (supported versions see below)

## Compatibility OneAgent SDK for Java releases with OneAgent for Java releases
|OneAgent SDK for Java|Dynatrace OneAgent Java|
|:------|:--------|
|1.0.1  |>=1.135  |

## Release Notes (OneAgent SDK for Java)
|Version|Date|Description|
|:------|:----------|:--------------|
|1.0.1  |01.2018    |Initial release|
