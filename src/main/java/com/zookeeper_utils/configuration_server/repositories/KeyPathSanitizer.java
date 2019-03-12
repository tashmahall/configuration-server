package com.zookeeper_utils.configuration_server.repositories;

import java.io.Serializable;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.validations.AbstractSanitizeValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitezeFirstSlashValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeCharactersValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeLowcaseValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeSlashInTheEndValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeSlashValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeWhiteSpaceValidation;

@Interceptor
@SanitizeKeyPath
public class KeyPathSanitizer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@AroundInvoke
	public Object valid(InvocationContext ctx) throws ConfigPropertiesException {
		return validate((String)ctx.getTarget());
	}

	private String validate(String keyPath) throws ConfigPropertiesException {
		AbstractSanitizeValidation validate = new SanitezeFirstSlashValidation(
				new SanitizeLowcaseValidation(
						new SanitizeWhiteSpaceValidation(
								new SanitizeSlashInTheEndValidation(
										new SanitizeCharactersValidation(
												new SanitizeSlashValidation(null))))));
		return validate.validateKeyPath(keyPath);
	}
}
