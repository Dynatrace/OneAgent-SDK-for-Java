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
package com.dynatrace.oneagent.sdk.api;

/**
 * LoggingCallback gets called only inside a OneAgentSDK API call when an
 * error/warning has occurred. <br>
 * Never call any SDK API inside one of these callback methods.
 */
public interface LoggingCallback {

	/**
	 * Just a warning. Something is missing, but OneAgent is working normal.
	 *
	 * @param message
	 *            message text. never null.
	 * @since 1.0
	 */
	void warn(String message);

	/**
	 * Something that should be done can't be done. (e. g. PurePath could not be
	 * started)
	 *
	 * @param message
	 *            message text. never null.
	 * @since 1.0
	 */
	void error(String message);
}
