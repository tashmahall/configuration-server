package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;


public interface ZookeeperRepositoryInterface{
	
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException;
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException;
	default String loadHostAndPort() throws ConfigPropertiesException {
		try {
			InitialContext context = new InitialContext();
			String host = (String)context.lookup("java:global/zookeeperHost");
			String port = (String)context.lookup("java:global/zookeeperPort");
			return host+":"+port;
		} catch (NamingException e) {
			throw new ConfigPropertiesException("Got the error ["+e.getMessage()+ "] while trying to get the zookeeper host and port",e);
		}
	}
}
