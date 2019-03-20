package com.zookeeper_utils.configuration_server.repositories;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public interface ZookeeperConfigurationLoader {
	public String loadHostAndPort() throws ConfigPropertiesException;
	public String getGlobalContextName() throws ConfigPropertiesException;
	public String getContextName();
}
