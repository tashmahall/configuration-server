package com.zookeeper_utils.configuration_server.repositories;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;


public interface ZookeeperRepositoryInterface{
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException;
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException;
	default String loadHostAndPort() throws ConfigPropertiesException {
		String fileName = "zookeeper.properties";
		Properties properties = new Properties();
		try {
			properties.load(ZookeeperRepositoryInterface.class.getClassLoader().getResourceAsStream(fileName));
			String host = properties.getProperty("zookeeper.host");
			String port = properties.getProperty("zookeeper.port");
			return host+":"+port;
		} catch (IOException e) {
			throw new ConfigPropertiesException("Error while trying to get the zookeeper properties in the file zookeeper.properties",e);
		}
	}
}
