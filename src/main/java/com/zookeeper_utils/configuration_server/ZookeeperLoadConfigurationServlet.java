package com.zookeeper_utils.configuration_server;

import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import com.zookeeper_utils.configuration_server.properties.ZookeeperConfigPropertiesApplicationScoped;

public class ZookeeperLoadConfigurationServlet extends HttpServlet implements ServletContextListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ZookeeperConfigPropertiesApplicationScoped zookeeperConfigProperties;
	
	
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
