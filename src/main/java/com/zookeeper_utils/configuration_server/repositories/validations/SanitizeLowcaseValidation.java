package com.zookeeper_utils.configuration_server.repositories.validations;

import java.util.regex.Pattern;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class SanitizeLowcaseValidation extends AbstractSanitizeValidation {
	public SanitizeLowcaseValidation(AbstractSanitizeValidation asv) {
		super(asv);
	}

	@Override
	public void validationRule(String keyPath) throws ConfigPropertiesException {
		Pattern patter = Pattern.compile("[A-Z]");
		if(patter.matcher(keyPath).find()) {
			throw new ConfigPropertiesException("The 'keyPath' ["+keyPath+"] must not have UPERCASE characters");
		}
	}
}
