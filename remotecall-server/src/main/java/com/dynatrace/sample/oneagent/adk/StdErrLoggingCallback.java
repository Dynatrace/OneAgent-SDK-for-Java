package com.dynatrace.sample.oneagent.adk;

import com.dynatrace.oneagent.adk.api.LoggingCallback;

/**
 * Implementation of OneAgent Logging Callback. Just printing output messages to
 * std err.
 */
public class StdErrLoggingCallback implements LoggingCallback {

	@Override
	public void error(String message) {
		System.err.println("[OneAgent ADK ERROR]: " + message);
	}

	@Override
	public void warn(String message) {
		System.err.println("[OneAgent ADK WARNING]: " + message);
	}

}
