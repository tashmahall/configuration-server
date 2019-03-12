package com.zookeeper_utils.configuration_server.repositories.validations;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public abstract class AbstractSanitizeValidation {
	
	private AbstractSanitizeValidation next;
	
	public AbstractSanitizeValidation() {}
	
	public AbstractSanitizeValidation(AbstractSanitizeValidation asv) {
		this.next = asv;
	}
	public String validateKeyPath(String keyPath) throws ConfigPropertiesException{
		this.validationRule(keyPath);
		if (next!=null) {
			return next.validateKeyPath(keyPath);
		}else {
			return keyPath;
		}
	}
	
	public abstract void validationRule(String keyPath) throws ConfigPropertiesException;

}
