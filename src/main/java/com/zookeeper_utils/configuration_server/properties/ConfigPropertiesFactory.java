package com.zookeeper_utils.configuration_server.properties;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperties;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesAppScoped;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesGlobalRequestScoped;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesRequestScoped;

@RequestScoped
public class ConfigPropertiesFactory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	@ZKServicePropertiesAppScoped
	private ZookeeperServicePropertiesInterface zkServiceAppScoped;
	@Inject
	@ZKServicePropertiesRequestScoped
	private ZookeeperServicePropertiesInterface zkServiceReqScoped;
	@Inject
	@ZKServicePropertiesGlobalRequestScoped
	private ZookeeperServicePropertiesInterface zkServiceGlobalReqScoped;
	
	@Produces
	@ConfigProperties(keyPath="")
	public String produce(InjectionPoint injectionPoint) throws ConfigPropertiesException {
		Annotated annotated = injectionPoint.getAnnotated();
		ConfigProperties configProperties = annotated.getAnnotation(ConfigProperties.class);
		String key = configProperties.keyPath();
		ZookeeperServicePropertyType zkspt = configProperties.configPropertyType();
		switch (zkspt) {
		case GLOBAL_CONTEXT_NO_WATCHER:
			return getKeyValueGlobalContextNoWatcher(key);
		case APPLICATION_SCOPED_WITH_WATCHER:
			return getKeyValueAppScopedWithWatcher(key);
		case REQUEST_SCOPED_NO_WATCHER:
			return getKeyValueReqScopedWithoutWatcher(key);
		default:
			throw new ConfigPropertiesException("Wrong ZookeeperServicePropertyType informed to 'keyPath' ["+key+"]");
		}
	}

	private String getKeyValueGlobalContextNoWatcher(String key) throws ConfigPropertiesException {
		String value = zkServiceGlobalReqScoped.getPropertyValue(key);
		return value;
	}
	private String getKeyValueAppScopedWithWatcher(String key) throws ConfigPropertiesException {
		String value = zkServiceAppScoped.getPropertyValue(key);
		return value;
	}
	private String getKeyValueReqScopedWithoutWatcher(String key) throws ConfigPropertiesException {
		String value = zkServiceReqScoped.getPropertyValue(key);
		return value;
	}
}
