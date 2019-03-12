package com.zookeeper_utils.configuration_server.service;

import java.util.Map;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public interface ZookeeperServicePropertiesInterface {
	public String getPropertyValue(String keyPath) throws ConfigPropertiesException ;
	public Map<String, String> getPropertiesMap() throws ConfigPropertiesException;
	
}
