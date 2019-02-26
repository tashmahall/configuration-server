package com.zookeeper_utils.configuration_server.properties;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

@RequestScoped
public class ConfigPropertiesFactory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private ZookeeperConfigPropertiesApplicationScoped zookeeperConfigProperties;
	
	@Produces
	@ConfigProperties(keyPath="")
	public String produce(InjectionPoint injectionPoint) throws ConfigPropertiesException, Exception {
		Annotated annotated = injectionPoint.getAnnotated();
		ConfigProperties configProperties = annotated.getAnnotation(ConfigProperties.class);
		String key = configProperties.keyPath();
		return getKeyValue(key);
	}

	public String getKeyValue(String key) throws ConfigPropertiesException {
//		String value = zookeeperConfigProperties.getPropertyValueUnwatchedWay(key);
//		return value;
		return null;
	}
}
