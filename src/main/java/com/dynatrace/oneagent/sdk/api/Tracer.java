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
 * Common interface for timing-related methods. Not to be directly used by SDK
 * user.
 */
public interface Tracer {

	/**
	 * starts timing of a node. Every node that has been started, must be ended with
	 * {@link #end()} method. Consider using the following pattern:
	 *
	 * <pre>
	 * {@code
	 *   tracer.start();
	 *   try {
	 *     // do your work
	 *   } catch (Exception e) {
	 *     tracer.error(e);
	 *   } finally {
	 *     tracer.end();
	 *   }
	 * }
	 * </pre>
	 *
	 * {@link #start()}, {@link #end()}, {@link #error(String)},
	 * {@link #error(Throwable)} are not thread-safe. They must be called from the
	 * same thread where {@link #start()} has been invoked.
	 *
	 * @since 1.0
	 */
	void start();

	/**
	 * Ends timing of a node. Typically this method is called via finally block.
	 * <br>
	 * {@link #start()}, {@link #end()}, {@link #error(String)},
	 * {@link #error(Throwable)} are not thread-safe. They must be called from the
	 * same thread where {@link #start()} has been invoked.
	 *
	 * @since 1.0
	 */
	void end();

	/**
	 * Marks the node as 'exited by exception'. An additional error message can be
	 * provided as String. <br>
	 * {@link #start()}, {@link #end()}, {@link #error(String)},
	 * {@link #error(Throwable)} are not thread-safe. They must be called from the
	 * same thread where {@link #start()} has been invoked.
	 *
	 * @param message
	 *            error message with details about occurred error (eg. return code).
	 *            must not be null.
	 * @since 1.0
	 */
	void error(String message);

	/**
	 * Marks the node as 'exited by exception'.Additional information can be
	 * provided as Throwable. <br>
	 * {@link #start()}, {@link #end()}, {@link #error(String)},
	 * {@link #error(Throwable)} are not thread-safe. They must be called from the
	 * same thread where {@link #start()} has been invoked.
	 *
	 * @param throwable
	 *            exception, that occurred. must not null.
	 * @since 1.0
	 */
	void error(Throwable throwable);
}
