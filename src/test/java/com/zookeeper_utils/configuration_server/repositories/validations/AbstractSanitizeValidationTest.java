package com.zookeeper_utils.configuration_server.repositories.validations;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.validations.AbstractSanitizeValidation;
@RunWith(MockitoJUnitRunner.class)
public class AbstractSanitizeValidationTest {
	private AbstractSanitizeValidation sbv = new AbstractSanitizeValidation() {
		public void validationRule(String keyPath) throws ConfigPropertiesException {}
	};
	private AbstractSanitizeValidation sbv2 = new AbstractSanitizeValidation(sbv) {
		public void validationRule(String keyPath) throws ConfigPropertiesException {}
	};
	
	String testString = "/test";
	@Test
	public void testAbstractSanitizeValidationNextNull() throws ConfigPropertiesException {
		String test = sbv.validateKeyPath(testString);
		assertEquals(testString, test);
	}
	
	@Test
	public void testAbstractSanitizeValidationNextNotNull() throws ConfigPropertiesException {
		
		String test = sbv2.validateKeyPath(testString);
		assertEquals(testString, test);
	}
}
