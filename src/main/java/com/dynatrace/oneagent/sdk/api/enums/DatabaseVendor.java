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

import com.dynatrace.oneagent.sdk.api.OneAgentSDK;

/**
 * Enumerates all well-known database vendors. See {@link OneAgentSDK#createDatabaseInfo(String, String, ChannelType, String)}.
 * Using these constants ensures that services captured by OneAgentSDK are handled the same way as traced via built-in sensors.
 * @since 1.7.0 
 */
public enum DatabaseVendor {

	APACHE_HIVE("ApacheHive"),
	CLOUDSCAPE("Cloudscape"),
	HSQLDB("HSQLDB"),
	PROGRESS("Progress"),
	MAXDB("MaxDB"),
	HANADB("HanaDB"),
	INGRES("Ingres"),
	FIRST_SQL("FirstSQL"),
	ENTERPRISE_DB("EnterpriseDB"),
	CACHE("Cache"),
	ADABAS("Adabas"),
	FIREBIRD("Firebird"),
	DB2("DB2"),
	DERBY_CLIENT("Derby Client"),
	DERBY_EMBEDDED("Derby Embedded"),
	FILEMAKER("Filemaker"),
	INFORMIX("Informix"),
	INSTANT_DB("InstantDb"),
	INTERBASE("Interbase"),
	MYSQL("MySQL"),
	MARIADB("MariaDB"),
	NETEZZA("Netezza"),
	ORACLE("Oracle"),
	PERVASIVE("Pervasive"),
	POINTBASE("Pointbase"),
	POSTGRESQL("PostgreSQL"),
	SQLSERVER("SQL Server"),
	SQLITE("sqlite"),
	SYBASE("Sybase"),
	TERADATA("Teradata"),
	VERTICA("Vertica"),
	CASSANDRA("Cassandra"),
	H2("H2"),
	COLDFUSION_IMQ("ColdFusion IMQ"),
	REDSHIFT("Amazon Redshift"),
	COUCHBASE("Couchbase");
	
	private final String vendorName;
	
	private DatabaseVendor(String vendorName) {
		this.vendorName = vendorName;
	}
	
	public String getVendorName() {
		return vendorName;
	}

	@Override
	public String toString() {
		return vendorName;
	}
}
