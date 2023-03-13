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
package com.dynatrace.oneagent.sdk.impl;

import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.impl.noop.OneAgentSDKNoop;
import com.dynatrace.oneagent.sdk.impl.proxy.OneAgentSDKProxy;
import com.dynatrace.oneagent.sdk.impl.proxy.SDK2AgentInternalApiProxy;

/**
 * Entry point for customer application.
 */
public class OneAgentSDKFactoryImpl {

	/*
	 * increase version with every change. in case of non breaking change (to
	 * OneAgent), increase oneSdkFix only.
	 */
	static final int oneSdkMajor = 1;
	static final int oneSdkMinor = 9;
	static final int oneSdkFix = 0;

	public static final boolean debugOneAgentSdkStub = Boolean.parseBoolean(System.getProperty("com.dynatrace.oneagent.sdk.debug", "false"));

	private static OneAgentSDK createOneSDK() {
		Object agentApiImpl = SDKInstanceProvider.create(oneSdkMajor, oneSdkMinor, oneSdkFix);
		if (agentApiImpl == null) {
			// OneAgent not present or not compatible
			if (debugOneAgentSdkStub) {
				logDebug("- no OneAgent present or OneAgent declined to work with OneAgentSdk version " + oneSdkMajor
						+ "." + oneSdkMinor + "." + oneSdkFix);
			}
			return new OneAgentSDKNoop();
		}
		try {
			SDK2AgentInternalApiProxy agentApi = new SDK2AgentInternalApiProxy(agentApiImpl);
			Object agentSdkImpl = agentApi.oneAgentSDKFactory_createSdk();
			if (agentSdkImpl != null) {
				return new OneAgentSDKProxy(agentApi, agentSdkImpl);
			}
			if (debugOneAgentSdkStub) {
				logDebug("- OneAgent failed to provide sdk object.");
			}
			return new OneAgentSDKNoop();
		} catch (Throwable e) {
			if (debugOneAgentSdkStub) {
				logDebug("- failed to contact OneAgent: " + e.getClass().getName() + ": " + e.getMessage());
			}
			return new OneAgentSDKNoop();
		}
	}

	public static OneAgentSDK createInstance() {
		return createOneSDK();
	}

	public static void logDebug(String msg) {
		System.out.println("[onesdk    ] " + msg);
	}
}
