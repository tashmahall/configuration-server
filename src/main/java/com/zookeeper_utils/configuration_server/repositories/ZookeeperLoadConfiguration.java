package com.zookeeper_utils.configuration_server.repositories;

import java.io.IOException;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ZookeeperLoadConfiguration {
	private ZookeeperLoadConfiguration() {}
	public static InitialContext getInitialContext() throws NamingException {
		return new InitialContext(); 
	}
	public static Properties getProperties() throws IOException {
		String fileName = "zookeeper.properties";
		Properties properties = new Properties();
		properties.load(ZookeeperRepositoryInterface.class.getClassLoader().getResourceAsStream(fileName));
		return properties; 
	}

}
