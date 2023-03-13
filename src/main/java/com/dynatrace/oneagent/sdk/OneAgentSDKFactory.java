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
package com.dynatrace.oneagent.sdk;

import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.impl.OneAgentSDKFactoryImpl;

/**
 * Entry point for customer application.
 */
public class OneAgentSDKFactory {

	/**
	 * Provides a {@link OneAgentSDK} instance, that has to be used to create
	 * transactions. It is safe to use returned {@link OneAgentSDK} instance in
	 * multiple threads. Every application should only create one single SDK
	 * instance during its lifetime.
	 *
	 * @return never null. if no OneAgent present, NOOP implementation gets
	 *         returned.
	 */
	public static OneAgentSDK createInstance() {
		return OneAgentSDKFactoryImpl.createInstance();
	}
}
