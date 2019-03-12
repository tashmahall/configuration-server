package com.zookeeper_utils.configuration_server.repositories.validations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeLowcaseValidation;
@RunWith(MockitoJUnitRunner.class)
public class SanitizeLowcaseValidationTest {
	@InjectMocks
	@Spy
	private SanitizeLowcaseValidation sbv;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
	@Test
	public void testValidationRuleOk() throws ConfigPropertiesException {
		String keyPathTest = "/keypath/test";
		sbv.validationRule(keyPathTest);
		verify(sbv,times(1)).validationRule(keyPathTest);
		
	}
	@Test
	public void testThrowedMessage() throws ConfigPropertiesException {
		String keyPath = "/Teste/T";
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("The 'keyPath' ["+keyPath+"] must not have UPERCASE characters");
        sbv.validationRule(keyPath);
	}


}
