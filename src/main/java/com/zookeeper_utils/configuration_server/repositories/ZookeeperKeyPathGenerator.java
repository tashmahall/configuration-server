package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import org.apache.curator.framework.CuratorFramework;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

interface ZookeeperKeyPathGenerator {
	public Map<String,String> getKeyPathTree(String contextName,CuratorFramework clientZookeeper) throws ConfigPropertiesException ;
}
