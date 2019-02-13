package com.zookeeper_utils.configuration_server;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.zookeeper_utils.configuration_server.properties.ZookeeperConfigProperties;
import com.zookeeper_utils.configuration_server.properties.ZookeeperLoadConfiguration;

public class ZookeeperLoadConfigurationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	@ZookeeperLoadConfiguration(value="zookeeper.properties")
	private ZookeeperConfigProperties zookeeperProperties;
	@Inject
	private ServletContext context;
	@Override
	public void init() throws ServletException {
		super.init();
		try {
			zookeeperProperties.getConfigurationTree(context.getServletContextName());
		} catch (Exception e) {
			throw new ServletException(e.getMessage(),e);
		}
	}
	

}
