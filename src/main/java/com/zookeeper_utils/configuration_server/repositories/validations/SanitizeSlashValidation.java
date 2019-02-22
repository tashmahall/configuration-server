package com.zookeeper_utils.configuration_server.repositories.validations;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class SanitizeSlashValidation extends AbstractSanitizeValidation {
	public SanitizeSlashValidation(AbstractSanitizeValidation asv) {
		super(asv);
	}

	@Override
	public void validationRule(String keyPath) throws ConfigPropertiesException {
		String[] arrayString = keyPath.split("/");
		Integer i = StringUtils.countMatches(keyPath, '/');
		if((i+1)!=arrayString.length) {
			throw new ConfigPropertiesException("Wrong number of slashs in the 'keyPath' ["+keyPath+"]");
		}
	}
}
