package com.dynatrace.oneagent.sdk.impl;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.dynatrace.oneagent.sdk.impl.SDKInstanceProvider;

public class SDKInstanceProviderTest {

	@Test
	public void testClassName() {
		assertThat("class name changed. incompatible with older agents.", SDKInstanceProvider.class.getName(),
				IsEqual.equalTo("com.dynatrace.oneagent.sdk.impl.SDKInstanceProvider"));
	}

}
