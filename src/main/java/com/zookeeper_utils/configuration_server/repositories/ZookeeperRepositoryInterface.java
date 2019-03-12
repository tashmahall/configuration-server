package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;


public interface ZookeeperRepositoryInterface{
	
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException;
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException;
}
