package com.zookeeper_utils.configuration_server.repositories;

import java.io.IOException;
import java.util.Properties;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderZookeeperPropertiesFile;
@ZKConfigurationLoaderZookeeperPropertiesFile
public class ZookeeperConfigurationLoaderZookeeperPropertiesFile implements ZookeeperConfigurationLoader{
	public String loadHostAndPort() throws ConfigPropertiesException {

		try {
			Properties properties = ZookeeperLoadConfiguration.getProperties();
			String host = properties.getProperty("zookeeper.host");
			String port = properties.getProperty("zookeeper.port");
			return host+":"+port;
		} catch (IOException e) {
			throw new ConfigPropertiesException("Error while trying to get the zookeeper properties in the file zookeeper.properties",e);
		}
	}

}
