package com.zookeeper_utils.configuration_server.repositories;

import java.io.Serializable;
import java.lang.reflect.Field;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;

import org.jboss.weld.injection.FieldInjectionPoint;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.validations.AbstractSanitizeValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitezeFirstSlashValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeCharactersValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeLowcaseValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeSlashInTheEndValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeSlashValidation;
import com.zookeeper_utils.configuration_server.repositories.validations.SanitizeWhiteSpaceValidation;

@RequestScoped
public class KeyPathSanitizer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	@Inject
//	private ZookeeperConfigProperties zookeeperConfigProperties;
	
	@Produces
	@SanitizeKeyPath
	public void produce(FieldInjectionPoint<String, Field> obj) throws ConfigPropertiesException {
		System.out.println("LOGGGGGGGG");
//		obj.getAnnotated().getJavaMember().get
//		obj.getAnnotated().getDeclaringCallable().
//        Field[] fields = obj.getClass().getDeclaredFields();
//        for( int i = 0; i < fields.length; i++ ){
//        	SanitizeKeyPath notNullannotation = (SanitizeKeyPath)fields[i].getAnnotation(SanitizeKeyPath.class);
//            if(notNullannotation != null ){
//            	fields[i].setAccessible(true);
//            	try {
//					String keyPath = fields[i].get(obj).toString();
//					validate(keyPath);
//				} catch (IllegalArgumentException | IllegalAccessException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//            }
//        }
	}

	private void validate(String keyPath) throws ConfigPropertiesException {
		AbstractSanitizeValidation validate = new SanitezeFirstSlashValidation(
				new SanitizeLowcaseValidation(
						new SanitizeWhiteSpaceValidation(
								new SanitizeSlashInTheEndValidation(
										new SanitizeCharactersValidation(
												new SanitizeSlashValidation(null))))));
		validate.validateKeyPath(keyPath);
	}
}
