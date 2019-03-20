package com.zookeeper_utils.configuration_server.repositories;

import javax.inject.Inject;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderJbossGlobalBinds;
@ZKConfigurationLoaderJbossGlobalBinds
public class ZookeeperConfigurationLoaderJbossGlobalBinds implements ZookeeperConfigurationLoader{
	@Inject
	private ServletContext servletContext;	
	public String loadHostAndPort() throws ConfigPropertiesException {
		try {
			InitialContext context = ZookeeperPropertiesFileLoadConfiguration.getInitialContext();
			String host = (String)context.lookup("java:global/zookeeperHost");
			String port = (String)context.lookup("java:global/zookeeperPort");
			return host+":"+port;
		} catch (NamingException e) {
			throw new ConfigPropertiesException("Got the error ["+e.getMessage()+ "] while trying to get the zookeeper host and port",e);
		}
	}

	@Override
	public String getGlobalContextName() throws ConfigPropertiesException {
		try {
			InitialContext context = ZookeeperPropertiesFileLoadConfiguration.getInitialContext();
			return (String)context.lookup("java:global/ctxGlobalName");
		} catch (NamingException e) {
			throw new ConfigPropertiesException("Got the error ["+e.getMessage()+ "] while trying to get the Global Context Name",e);
		}
	}

	@Override
	public String getContextName(){
		return StringUtils.join("/",servletContext.getServletContextName());
	}
}
