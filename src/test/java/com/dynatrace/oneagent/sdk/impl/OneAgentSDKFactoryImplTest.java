package com.dynatrace.oneagent.sdk.impl;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.dynatrace.oneagent.sdk.impl.OneAgentSDKFactoryImpl;

public class OneAgentSDKFactoryImplTest {

	@Test
	public void versionCheckTest() {
		// *** public version of SDK
		String buildVersion = System.getProperty("sdk.version");
		Assertions.assertThat(buildVersion).isEqualTo("1.9.0");

		// *** internal version (between SDK and agent)
		String[] splitted = buildVersion.split("\\.");
		int major = Integer.parseInt(splitted[0]);
		int minor = Integer.parseInt(splitted[1]);
		int fix = Integer.parseInt(splitted[2]);

		Assertions.assertThat(major).isEqualTo(OneAgentSDKFactoryImpl.oneSdkMajor);
		Assertions.assertThat(minor).isEqualTo(OneAgentSDKFactoryImpl.oneSdkMinor);
		Assertions.assertThat(fix).isEqualTo(OneAgentSDKFactoryImpl.oneSdkFix);
	}
}
