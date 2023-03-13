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
package com.dynatrace.oneagent.sdk.impl.noop;

import com.dynatrace.oneagent.sdk.api.infos.TraceContextInfo;

public final class TraceContextInfoNoop implements TraceContextInfo {
    public static final TraceContextInfoNoop INSTANCE = new TraceContextInfoNoop();

    private TraceContextInfoNoop() {}

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getTraceId() {
        return INVALID_TRACE_ID;
    }

    @Override
    public String getSpanId() {
        return INVALID_SPAN_ID;
    }
}
