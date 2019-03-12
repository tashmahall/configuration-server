package com.zookeeper_utils.configuration_server.repositories;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderJbossGlobalBinds;
@ZKConfigurationLoaderJbossGlobalBinds
public class ZookeeperConfigurationLoaderJbossGlobalBinds implements ZookeeperConfigurationLoader{
	public String loadHostAndPort() throws ConfigPropertiesException {
		try {
			InitialContext context = ZookeeperLoadConfiguration.getInitialContext();
			String host = (String)context.lookup("java:global/zookeeperHost");
			String port = (String)context.lookup("java:global/zookeeperPort");
			return host+":"+port;
		} catch (NamingException e) {
			throw new ConfigPropertiesException("Got the error ["+e.getMessage()+ "] while trying to get the zookeeper host and port",e);
		}
	}
}
