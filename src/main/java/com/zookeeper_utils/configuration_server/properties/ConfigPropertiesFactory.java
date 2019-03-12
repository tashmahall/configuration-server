package com.zookeeper_utils.configuration_server.properties;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperties;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertyType;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesAppScoped;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesGlobalRequestScoped;
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
	
	@Inject
	@ZKServicePropertiesGlobalRequestScoped
	private	Instance<ZookeeperServicePropertiesInterface>  zkServiceGlobalReqScoped;
	
	@Produces
	@ConfigProperties(value = "")
	public String produce(InjectionPoint injectionPoint) throws ConfigPropertiesException {
		Annotated annotated = injectionPoint.getAnnotated();
		ConfigProperties configProperties = annotated.getAnnotation(ConfigProperties.class);
		String key = configProperties.value();
		ZookeeperServicePropertyType zkspt = configProperties.configPropertyType();
		switch (zkspt) {
		case GLOBAL_CONTEXT_NO_WATCHER:
			return getKeyValueGlobalContextNoWatcher(key);
		case APPLICATION_SCOPED_WITH_WATCHER:
			return getKeyValueAppScopedWithWatcher(key);
		case REQUEST_SCOPED_NO_WATCHER:
		default:
			return getKeyValueReqScopedWithoutWatcher(key);
		}
	}

	private String getKeyValueGlobalContextNoWatcher(String key) throws ConfigPropertiesException {
		return zkServiceGlobalReqScoped.get().getPropertyValue(key);
	}
	private String getKeyValueAppScopedWithWatcher(String key) throws ConfigPropertiesException {
		return zkServiceAppScoped.get().getPropertyValue(key);
	}
	private String getKeyValueReqScopedWithoutWatcher(String key) throws ConfigPropertiesException {
		return zkServiceReqScoped.get().getPropertyValue(key);
	}
}
