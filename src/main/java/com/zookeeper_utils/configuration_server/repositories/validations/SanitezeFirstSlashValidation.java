package com.zookeeper_utils.configuration_server.repositories.validations;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class SanitezeFirstSlashValidation extends AbstractSanitizeValidation{
	
	public SanitezeFirstSlashValidation(AbstractSanitizeValidation validation) {
		super(validation);
	}

	@Override
	public void validationRule(String keyPath) throws ConfigPropertiesException {
		int i = StringUtils.indexOf(keyPath, '/');
		if(i!=0) {
			throw new ConfigPropertiesException("The 'keyPath' ["+keyPath+"] must begin with '/'");
		}
		
	}

}
