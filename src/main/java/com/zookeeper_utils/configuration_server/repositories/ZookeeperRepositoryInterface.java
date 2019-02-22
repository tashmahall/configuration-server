package com.zookeeper_utils.configuration_server.repositories;

import javax.validation.constraints.NotNull;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public interface ZookeeperRepositoryInterface {
	public String getValueFromKeyPath(@NotNull String keyPath) throws ConfigPropertiesException;

}
