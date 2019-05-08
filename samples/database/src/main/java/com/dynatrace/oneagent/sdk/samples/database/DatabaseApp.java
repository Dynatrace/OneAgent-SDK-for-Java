package com.dynatrace.oneagent.sdk.samples.database;

import com.dynatrace.oneagent.sdk.OneAgentSDKFactory;
import com.dynatrace.oneagent.sdk.api.DatabaseRequestTracer;
import com.dynatrace.oneagent.sdk.api.OneAgentSDK;
import com.dynatrace.oneagent.sdk.api.enums.ChannelType;
import com.dynatrace.oneagent.sdk.api.infos.DatabaseInfo;

/**
 * Sample application shows how database requests should be traced.
 * 
 * @author Alram.Lechner
 *
 */
public class DatabaseApp {

	private final OneAgentSDK oneAgentSdk;
	
	static DatabaseApp instance;
	
	private DatabaseApp() {
		oneAgentSdk = OneAgentSDKFactory.createInstance();
		oneAgentSdk.setLoggingCallback(new StdErrLoggingCallback());
		switch (oneAgentSdk.getCurrentState()) {
		case ACTIVE:
			System.out.println("SDK is active and capturing.");
			break;
		case PERMANENTLY_INACTIVE:
			System.err.println(
					"SDK is PERMANENTLY_INACTIVE; Probably no OneAgent injected or OneAgent is incompatible with SDK.");
			break;
		case TEMPORARILY_INACTIVE:
			System.err.println(
					"SDK is TEMPORARILY_INACTIVE; OneAgent has been deactivated - check OneAgent configuration.");
			break;
		default:
			System.err.println("SDK is in unknown state.");
			break;
		}
		instance = this;
	}

	public static void main(String args[]) {
		System.out.println("*************************************************************");
		System.out.println("**         Running database request sample                 **");
		System.out.println("*************************************************************");
		try {
			DatabaseApp app = new DatabaseApp();
			
			app.traceSqlRequest("Select * from anyTable");
			
			System.out.println("sample application stopped. sleeping a while, so OneAgent is able to send data to server ...");
			Thread.sleep(15000 * 3); // we have to wait - so OneAgent is able to send data to server
		} catch (Exception e) {
			System.err.println("database request sample failed: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * You need to add a custom service for this method. a database request will only be captured, if it s part of an already
	 * started transaction.
	 * see <a href="https://www.dynatrace.com/support/help/how-to-use-dynatrace/transactions-and-services/configuration/define-custom-services/">Dynatrace help</a> 
	 * for more information about a custom service.
	 */
	private void traceSqlRequest(String sql) {
		DatabaseInfo databaseInfo = oneAgentSdk.createDatabaseInfo("myCoolDatabase", "UnsupportedDatabaseVendor", ChannelType.TCP_IP, "theDbHost.localdomain:3434");
		DatabaseRequestTracer databaseRequestTracer = oneAgentSdk.traceSqlDatabaseRequest(databaseInfo, sql);
		databaseRequestTracer.start();
		try {
			executeRequest(sql);
		} catch(Exception e) {
			databaseRequestTracer.error(e);
			// handle or re-throw exception!
		} finally {
			databaseRequestTracer.end();
		}
	}

	private void executeRequest(String sql) {
		// run the sql against the db ...
	}
	
}
