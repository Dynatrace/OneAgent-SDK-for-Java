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
package com.dynatrace.oneagent.sdk.api.infos;

/**
 * Provides information about a PurePath node using the TraceContext (Trace-Id, Span-Id) model as defined in
 * <a href="https://www.w3.org/TR/trace-context>the W3C recommendation</a>.
 *
 * <p>The Span-Id represents the currently active PurePath node (tracer).
 * This Trace-Id and Span-Id information is not intended for tagging and context-propagation scenarios and primarily designed for
 * log-enrichment use cases.</p>
 *
 * @since 1.9
 */
public interface TraceContextInfo {

    /**
     * All-zero (invalid) W3C trace ID.
     */
    String INVALID_TRACE_ID = "00000000000000000000000000000000";

    /**
     * All-zero (invalid) W3C span ID.
     */
    String INVALID_SPAN_ID = "0000000000000000";

    /** If true, the trace & span ID are both valid (i.e., non-zero).
     *
     * <p>These are a few common reasons for why you may be unable to get a valid trace context: <ul>
     *   <li>No OneAgent is present, or it is incompatible with this SDK version
     *     (check {@link com.dynatrace.oneagent.sdk.api.OneAgentSDK#getCurrentState()})</li>
     *  <li>There is no currently active PurePath context.</li>
     *  <li>Some other reason; more information may be available through {@link com.dynatrace.oneagent.sdk.api.OneAgentSDK#setLoggingCallback(com.dynatrace.oneagent.sdk.api.LoggingCallback)}</li>
     * </ul>
     * </p>
     * */
    boolean isValid();

    /** The W3C trace ID hex string (never empty or null, but might be all-zero if {@link #isValid()}) is false) */
     String getTraceId();

    /** The W3C span ID hex string (never empty or null, but might be all-zero if {@link #isValid()}) is false) */
    String getSpanId();
}
