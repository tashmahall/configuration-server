package com.zookeeper_utils.configuration_server.repositories.validations;

import javax.validation.constraints.NotNull;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public abstract class AbstractSanitizeValidation {
	
	private AbstractSanitizeValidation next;
	
	public AbstractSanitizeValidation() {}
	
	public AbstractSanitizeValidation(AbstractSanitizeValidation asv) {
		this.next = asv;
	}
	public String validateKeyPath(@NotNull String keyPath) throws ConfigPropertiesException{
		this.validationRule(keyPath);
		if (next!=null) {
			return next.validateKeyPath(keyPath);
		}else {
			return keyPath;
		}
	}
	
	public abstract void validationRule(@NotNull String keyPath) throws ConfigPropertiesException;

}
