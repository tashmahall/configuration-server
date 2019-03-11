package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderJbossGlobalBinds;
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
	@ZKConfigurationLoaderJbossGlobalBinds
	private ZookeeperConfigurationLoader zcl;
	@Inject
	@ZKNoWatcherKeyPathTreeGenerator
	private ZookeeperKeyPathGenerator zri;
	private CuratorFramework clientZookeeper;
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException  {
		loadClientZookeeper();
		String realKeyPath =StringUtils.join("/",context.getServletContextName(),keyPath);
		try {
			return new String (this.clientZookeeper.getData().forPath(realKeyPath));
		} catch (Exception e) {
			throw new ConfigPropertiesException(gotError+e.getMessage()+" while load the properties to the 'keyPath' ["+keyPath+"]",e);
		}
	}
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException{
		loadClientZookeeper();
		String realcontextName ="/"+context.getServletContextName();
		return zri.getKeyPathTree(realcontextName, clientZookeeper);
	}
	private void loadClientZookeeper() throws ConfigPropertiesException {
    	if(clientZookeeper==null||clientZookeeper.getState().equals(CuratorFrameworkState.STOPPED)) {
    		clientZookeeper = CuratorFrameworkFactory.newClient(zcl.loadHostAndPort(), RETRY_POLICY);
        	clientZookeeper.start(); 
    	}
	}
}
