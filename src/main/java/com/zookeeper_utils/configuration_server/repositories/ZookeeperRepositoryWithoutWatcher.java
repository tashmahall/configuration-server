package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderJbossGlobalBinds;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKKeyPathTreeGeneratorNoWatcher;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryNoWatcher;

@ZKReopositoryNoWatcher
public class ZookeeperRepositoryWithoutWatcher implements ZookeeperRepositoryInterface {

	/**
	 * 
	 */
	public static final RetryPolicy RETRY_POLICY = new RetryNTimes(0, 60000);
	private String gotError = "Got the error ";
	@Inject
	@ZKConfigurationLoaderJbossGlobalBinds
	private ZookeeperConfigurationLoader zcl;
	@Inject
	@ZKKeyPathTreeGeneratorNoWatcher
	private ZookeeperKeyPathGenerator zri;
	private CuratorFramework clientZookeeper;
	public String getValueFromKeyPathApplicationContext(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException  {
		return getValueFromKeyPath(keyPath,false);
	}
	public Map<String,String> getKeyPathTreeApplicationContext() throws ConfigPropertiesException{
		return getKeyPathTree(false);
	}
	
	public Map<String,String> getKeyPathTree(boolean isGlobal) throws ConfigPropertiesException{
		loadClientZookeeper();
		String contextTemp = getContext(isGlobal);
		return zri.getKeyPathTree(contextTemp, clientZookeeper);
	}
	private String getValueFromKeyPath(@SanitizeKeyPath String keyPath,boolean isGlobal) throws ConfigPropertiesException  {
		loadClientZookeeper();
		String contextTemp = getContext(isGlobal);
		String realKeyPath =StringUtils.join(contextTemp,keyPath);
		try {
			return new String (this.clientZookeeper.getData().forPath(realKeyPath));
		} catch (Exception e) {
			throw new ConfigPropertiesException(gotError+e.getMessage()+" while load the properties to the 'keyPath' ["+keyPath+"]",e);
		}
	}
    private String getContext(boolean isGlobal) throws ConfigPropertiesException {
		if(isGlobal) {
			return zcl.getGlobalContextName();
		}
		return zcl.getContextName();
    }
	private void loadClientZookeeper() throws ConfigPropertiesException {
    	if(clientZookeeper==null||clientZookeeper.getState().equals(CuratorFrameworkState.STOPPED)) {
    		clientZookeeper = CuratorFrameworkFactory.newClient(zcl.loadHostAndPort(), RETRY_POLICY);
        	clientZookeeper.start(); 
    	}
	}
	@Override
	public String getValueFromKeyPathGlobalContext(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException {
		return getValueFromKeyPath(keyPath,true);
	}
	@Override
	public Map<String, String> getKeyPathTreeGlobalContext() throws ConfigPropertiesException {
		return getKeyPathTree(true);
	}
}
