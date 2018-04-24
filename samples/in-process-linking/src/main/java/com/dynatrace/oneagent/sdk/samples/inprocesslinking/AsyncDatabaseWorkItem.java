package com.dynatrace.oneagent.sdk.samples.inprocesslinking;

import com.dynatrace.oneagent.sdk.api.InProcessLink;

public class AsyncDatabaseWorkItem {

	public static final AsyncDatabaseWorkItem END = new AsyncDatabaseWorkItem("END", null);
	// async operation - eg. database operation:
	private String sqlStatement;

	// trace to link async execution with dynatrace
	private InProcessLink link;

	public AsyncDatabaseWorkItem(String sqlStatement, InProcessLink link) {
		this.sqlStatement = sqlStatement;
		this.link = link;
	}
	
	public String getSqlStatement() {
		return sqlStatement;
	}
	
	public InProcessLink getLink() {
		return link;
	}
}
