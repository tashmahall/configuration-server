package com.zookeeper_utils.configuration_server.repositories;

import java.io.IOException;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderZookeeperPropertiesFile;
@ZKConfigurationLoaderZookeeperPropertiesFile
public class ZookeeperConfigurationLoaderZookeeperPropertiesFile implements ZookeeperConfigurationLoader{
	@Inject
	private ServletContext context;	
	public String loadHostAndPort() throws ConfigPropertiesException {
		try {
			Properties properties = ZookeeperPropertiesFileLoadConfiguration.getProperties();
			String host = properties.getProperty("zookeeper.host");
			String port = properties.getProperty("zookeeper.port");
			return host+":"+port;
		} catch (IOException e) {
			throw new ConfigPropertiesException("Error while trying to get the zookeeper properties in the file zookeeper.properties",e);
		}
	}

	@Override
	public String getContextName() {
		return StringUtils.join("/",context.getServletContextName());
	}

	@Override
	public String getGlobalContextName() throws ConfigPropertiesException {
		try {
			Properties properties = ZookeeperPropertiesFileLoadConfiguration.getProperties();
			return properties.getProperty("global.context.name");
			
		} catch (IOException e) {
			throw new ConfigPropertiesException("Error while trying to get the zookeeper properties in the file zookeeper.properties",e);
		}
	}

}
