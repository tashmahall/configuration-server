package com.zookeeper_utils.configuration_server.repositories;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public interface ZookeeperConfigurationLoader {
	public String loadHostAndPort() throws ConfigPropertiesException ;
}
