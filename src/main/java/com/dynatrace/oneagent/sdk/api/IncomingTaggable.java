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
 * Common interface for server-tagging-related methods. Not to be directly used
 * by SDK user.
 */
public interface IncomingTaggable {

	/**
	 * Consumes a tag to continue a pure path. Must be set before a node is being
	 * started.<br>
	 * See {@link OutgoingTaggable} to determine how to create a tag.
	 *
	 * @param tag
	 *            the tag in String representation - must not be null.
	 * @since 1.0
	 */
	void setDynatraceStringTag(String tag);

	/**
	 * Same as {@link #setDynatraceStringTag(String)} but consumes binary
	 * representation of tag.
	 *
	 * @param tag
	 *            the tag in binary representation - must not be null.
	 * @since 1.0
	 */
	void setDynatraceByteTag(byte[] tag);

}
