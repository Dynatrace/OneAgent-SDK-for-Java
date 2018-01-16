package com.dynatrace.sample.oneagent.sdk;

import com.dynatrace.oneagent.sdk.api.LoggingCallback;

/**
 * Implementation of OneAgent Logging Callback. Just printing output messages to
 * std err.
 */
public class StdErrLoggingCallback implements LoggingCallback {

	@Override
	public void error(String message) {
		System.err.println("[OneAgent SDK ERROR]: " + message);
	}

	@Override
	public void warn(String message) {
		System.err.println("[OneAgent SDK WARNING]: " + message);
	}

}
