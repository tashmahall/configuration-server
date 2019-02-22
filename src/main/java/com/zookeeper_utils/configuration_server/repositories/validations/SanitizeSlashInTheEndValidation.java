package com.zookeeper_utils.configuration_server.repositories.validations;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class SanitizeSlashInTheEndValidation extends AbstractSanitizeValidation {
	public SanitizeSlashInTheEndValidation(AbstractSanitizeValidation asv) {
		super(asv);
	}

	@Override
	public void validationRule(String keyPath) throws ConfigPropertiesException {
		Integer i = keyPath.length();
		if(keyPath.lastIndexOf('/')==i-1) {
			throw new ConfigPropertiesException("Can not use '/' as last character in the 'keyPath' ["+keyPath+"]");
		}
	}
}
