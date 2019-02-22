package com.zookeeper_utils.configuration_server.repositories.validations;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class SanitizeWhiteSpaceValidation extends AbstractSanitizeValidation {
	public SanitizeWhiteSpaceValidation(AbstractSanitizeValidation asv) {
		super(asv);
	}

	@Override
	public void validationRule(String keyPath) throws ConfigPropertiesException {
		if(StringUtils.contains(keyPath, ' ')) {
			throw new ConfigPropertiesException("The 'keyPath' ["+keyPath+"] must not have WHITSPACE characters");
		}
	}
}
