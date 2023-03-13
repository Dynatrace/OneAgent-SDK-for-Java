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
package com.dynatrace.oneagent.sdk.api.enums;

/**
 * Defines the possible states of the SDK.
 */
public enum SDKState {

	/**
	 * SDK is connected to OneAgent and capturing data.
	 * 
	 * @since 1.0
	 */
	ACTIVE,

	/**
	 * SDK is connected to OneAgent, but capturing is disabled.It is good practice
	 * to skip creating SDK transactions to save resources. The SDK state should be
	 * checked regularly as it may change at every point in time.
	 * 
	 * @since 1.0
	 */
	TEMPORARILY_INACTIVE,

	/**
	 * SDK isn't connected to OneAgent, so it will never capture data. This SDK
	 * state will never change during the lifetime of a JVM. It is good practice to
	 * never call any SDK API to save resources.
	 * 
	 * @since 1.0
	 */
	PERMANENTLY_INACTIVE;

}
