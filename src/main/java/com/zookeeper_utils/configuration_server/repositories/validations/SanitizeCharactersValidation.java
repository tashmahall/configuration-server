package com.zookeeper_utils.configuration_server.repositories.validations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class SanitizeCharactersValidation extends AbstractSanitizeValidation {
	public SanitizeCharactersValidation(AbstractSanitizeValidation asv) {
		super(asv);
	}

	@Override
	public void validationRule(String keyPath) throws ConfigPropertiesException {
		String[] chars = keyPath.split("");
		Pattern pattern = Pattern.compile("([a-z]|/|[0-9])");
		for(String character:chars) {
			Matcher matcher = pattern.matcher(character);
			if(!matcher.matches()) {
				throw new ConfigPropertiesException("The 'keyPath' ["+keyPath+"] has unallowed characters. Just [a-z], '/', [0-9] are allowed");
			}
		}
		

	}
}
