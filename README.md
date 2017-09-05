# OneAgent ADK for Java sample applications

Sample applications showing how to use Dynatrace OneAgent ADK for Java to create custom specific PurePaths.

## Build and prepare running the sample applications
- ensure you have Apache Maven 3.5 installed, see: [Apache Maven](https://maven.apache.org/)
- ensure Dynatrace OneAgent is installed. if not see [our free Trial](https://www.dynatrace.com/)
- clone this repository
- edit the parent/pom.xml: adapt the agent.* properties to match your OneAgent environment
- run `mvn package` in root directory of the cloned repository

## Run RemoteCall sample application
This Application shows how to trace remote calls and tag them. To run this sample you need to start a server and client sample application - both with Dynatrace agent injected.

- Server: `mvn -pl remotecall-server exec:exec`

- Client: `mvn -pl remotecall-client exec:exec`

Check your Dynatrace / AppMon environment for created services / paths.

## Integrate in your application
If you want to integrate the OneAgent ADK into your application, just add the following maven dependency:

	<groupId>com.dynatrace.oneagent</groupId>
	<artifactId>oneagent.adk</artifactId>
	<version>1.0.0</version>
	<scope>compile</scope>

If you prefer plain jar's just download them from mavenCentral: FIXME: add link to mavenCentral 

## OneAgent ADK Requirements
- JRE 1.6 or higher
- OneAgent Java (supported versions see below)

## Compatibility OneAgent ADK Releases
|OneAgent ADK Java|Dynatrace OneAgent Java|Appmon OneAgent Java|
|:------|:--------|:--------|
|1.0.0  |>=7.1    |>=1.131  |

## Release Notes (OneAgent ADK sample applications)
|Version|Date|Description|
|:------|:----------|:--------------|
|1.0.0  |09.2017    |Initial version|
