package com.zookeeper_utils.configuration_server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesApplicationScoped;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesAppScoped;

public class ZookeeperLoadConfigurationServlet extends HttpServlet implements ServletContextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ZKServicePropertiesAppScoped
	private ZookeeperServicePropertiesApplicationScoped zookeeperConfigProperties;
	
	
//	@Override
//	public void init() throws ServletException {
//		try {
//			zookeeperProperties.getConfigurationTree("/showcase-rest");
//			System.out.println(context.getContextPath());
//		} catch (Exception e) {
//			throw new ServletException(e.getMessage(),e);
//		}
//	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
//			zookeeperConfigProperties.getConfigurationTree();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
	

}
