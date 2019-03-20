package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.interceptor.InvocationContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
@RunWith(MockitoJUnitRunner.class)
public class KeyPathSanitizerTest {
	@InjectMocks
	@Spy
	private KeyPathSanitizer sbv;
	
	@Mock
	private InvocationContext ctx;
	private String testExpect ="/test";
	@Before
	public void load() {
		when(ctx.getTarget()).thenReturn(testExpect);
	}
	@Test
	public void testValid() throws ConfigPropertiesException {
		String testReceived = (String) sbv.valid(ctx);
		assertEquals(testExpect, testReceived);
	}

}
