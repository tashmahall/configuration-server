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
@RunWith(MockitoJUnitRunner.class)
public class SanitizeCharactersValidationTest {
	@InjectMocks
	@Spy
	private SanitizeCharactersValidation sbv;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
	@Test
	public void testValidationRuleOk() throws ConfigPropertiesException {
		String keyPathTest = "/keypath/test";
		sbv.validationRule(keyPathTest);
		verify(sbv,times(1)).validationRule(keyPathTest);
		
	}
	@Test
	public void testThrowedMessageSpecialCharacters() throws ConfigPropertiesException {
		String keyPath = "/teste@/t/";
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("The 'keyPath' ["+keyPath+"] has unallowed characters. Just [a-z], '/', [0-9] are allowed");
        sbv.validationRule(keyPath);
	}
	@Test
	public void testThrowedMessageAccentuatedCharacters() throws ConfigPropertiesException {
		String keyPath = "/testeá/t/";
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("The 'keyPath' ["+keyPath+"] has unallowed characters. Just [a-z], '/', [0-9] are allowed");
        sbv.validationRule(keyPath);
	}

}
