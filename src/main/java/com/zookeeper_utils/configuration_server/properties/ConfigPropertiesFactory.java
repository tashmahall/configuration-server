package com.zookeeper_utils.configuration_server.properties;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperty;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.service.ZookeeperServiceScopeType;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesAppScoped;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesRequestScoped;

@RequestScoped
public class ConfigPropertiesFactory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	@ZKServicePropertiesAppScoped
	private	Instance<ZookeeperServicePropertiesInterface> zkServiceAppScoped;
	
	@Inject
	@ZKServicePropertiesRequestScoped
	private	Instance<ZookeeperServicePropertiesInterface> zkServiceReqScoped;
	
	@Produces
	@ConfigProperty(value = "")
	public String produce(InjectionPoint injectionPoint) throws ConfigPropertiesException {
		Annotated annotated = injectionPoint.getAnnotated();
		ConfigProperty configProperties = annotated.getAnnotation(ConfigProperty.class);
		String key = configProperties.value();
		boolean global = configProperties.global();
		ZookeeperServiceScopeType zkspt = configProperties.scopeType();
		switch (zkspt) {
		case APPLICATION_SCOPED:
			return zkServiceAppScoped.get().getPropertyValue(key, global);
		case REQUEST_SCOPED:
		default:
			return zkServiceReqScoped.get().getPropertyValue(key, global);
		}
	}
}
