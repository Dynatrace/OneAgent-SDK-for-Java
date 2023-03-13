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
 * Interface for outgoing database tracer.
 * <a href="https://github.com/Dynatrace/OneAgent-SDK#database">https://github.com/Dynatrace/OneAgent-SDK#database</a>
 * 
 * @since 1.7.0
 */
public interface DatabaseRequestTracer extends Tracer {

	/**
	 * Adds optional information about retrieved rows of the traced database request.
	 * 
	 * @param returnedRowCount number of rows returned by this traced database request. Only positive values are allowed. 
	 * @since 1.7.0
	 */
	public void setReturnedRowCount(int returnedRowCount);
	
	/**
	 * Adds optional information about round-trip count to database server.
	 * 
	 * @param roundTripCount count of round-trips that took place. Only positive values are allowed.
	 * @since 1.7.0
	 */
	public void setRoundTripCount(int roundTripCount);
	
}
