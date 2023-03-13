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
 * Defines the type of communication channel being used.
 * 
 * @author Alram.Lechner
 *
 */
public enum ChannelType {

	OTHER(0), TCP_IP(1), UNIX_DOMAIN_SOCKET(2), NAMED_PIPE(3), IN_PROCESS(4);

	/** constant is being used in API call to OneAgent. don't change it! */
	private final int sdkConstant;

	private ChannelType(int sdkConstant) {
		this.sdkConstant = sdkConstant;
	}

	public int getSDKConstant() {
		return sdkConstant;
	}
}
