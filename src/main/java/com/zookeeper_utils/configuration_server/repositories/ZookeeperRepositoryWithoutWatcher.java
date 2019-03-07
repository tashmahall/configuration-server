package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKNoWatcherKeyPathTreeGenerator;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryNoWatcher;

@ZKReopositoryNoWatcher
public class ZookeeperRepositoryWithoutWatcher implements ZookeeperRepositoryInterface {

	/**
	 * 
	 */
	public static final RetryPolicy RETRY_POLICY = new RetryNTimes(0, 60000);
	private String gotError = "Got the error ";
	@Inject
	private ServletContext context;	
	@Inject
	@ZKNoWatcherKeyPathTreeGenerator
	private ZookeeperKeyPathGenerator zri;
	private CuratorFramework clientZookeeper;
	public ZookeeperRepositoryWithoutWatcher () throws ConfigPropertiesException {
    	clientZookeeper = CuratorFrameworkFactory.newClient(loadHostAndPort(), RETRY_POLICY);
    	clientZookeeper.start(); 
	}
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException  {
		String realKeyPath =StringUtils.join("/",context.getServletContextName(),keyPath);
		try {
			return new String (this.clientZookeeper.getData().forPath(realKeyPath));
		} catch (Exception e) {
			throw new ConfigPropertiesException(gotError+e.getMessage()+" while load the properties to the 'keyPath' ["+keyPath+"]",e);
		}
	}
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException{
		String realcontextName ="/"+context.getServletContextName();
		return zri.getKeyPathTree(realcontextName, clientZookeeper);
	}
}
