package com.zookeeper_utils.configuration_server.repositories;

import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.RetryNTimes;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKConfigurationLoaderJbossGlobalBinds;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKKeyPathTreeGeneratorWithWatcher;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryWithWatcher;
@ZKReopositoryWithWatcher
public class ZookeeperRepositoryWithWhatcher implements ZookeeperRepositoryInterface {
	/**
	 * 
	 */
	public static final RetryPolicy RETRY_POLICY = new RetryNTimes(0, 60000);
	public static final String CONFIGURATION_TREE = "configurationTree";
	@Inject
	@ZKConfigurationLoaderJbossGlobalBinds
	private ZookeeperConfigurationLoader zcl;
	@Inject
	@ZKKeyPathTreeGeneratorWithWatcher
	private ZookeeperKeyPathGenerator zkpg;
	private CuratorFramework clientZookeeper;
	private Map<String,String> configurationMapAppContext;
	private Map<String,String> configurationMapGlobalContext;
	
    @Override
	public String getValueFromKeyPathApplicationContext(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException {
    	return getValueFromKey(keyPath,configurationMapAppContext,false);
    }
    @Override
	public Map<String, String> getKeyPathTreeApplicationContext() throws ConfigPropertiesException {
    	configurationMapAppContext = getKeyPathTree(false);
		return configurationMapAppContext;
	}
    
	private String getValueFromKey(@SanitizeKeyPath String keyPath, Map<String,String> confMap, boolean isGlobal) throws ConfigPropertiesException {
		loadClientZookeeper();
		String contextTemp = getContext(isGlobal);
		if(MapUtils.isEmpty(confMap)||StringUtils.isEmpty(confMap.get(keyPath))) {
			confMap = zkpg.getKeyPathTree(contextTemp, clientZookeeper);
		}
		String realKeyPath = StringUtils.join(contextTemp,keyPath);
		String value = confMap.get(realKeyPath);
		if(StringUtils.isEmpty(value)) {
			throw new ConfigPropertiesException("Does not exist entries with the 'keyPath' ["+realKeyPath+"]");
		}
		return value;
	}
	private Map<String,String> getKeyPathTree(boolean isGlobal) throws ConfigPropertiesException {
    	loadClientZookeeper();
    	String contextTemp = getContext(isGlobal);
    	return zkpg.getKeyPathTree(contextTemp, clientZookeeper);
	}
    private void loadClientZookeeper() throws ConfigPropertiesException {
    	if(clientZookeeper==null||clientZookeeper.getState().equals(CuratorFrameworkState.STOPPED)) {
    		clientZookeeper = CuratorFrameworkFactory.newClient(zcl.loadHostAndPort(), RETRY_POLICY);
    	}
	}
    private String getContext(boolean isGlobal) throws ConfigPropertiesException {
		if(isGlobal) {
			return zcl.getGlobalContextName();
		}
		return zcl.getContextName();
    }
	@Override
	public String getValueFromKeyPathGlobalContext(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException {
		return getValueFromKey(keyPath, configurationMapGlobalContext, true);
	}
	@Override
	public Map<String, String> getKeyPathTreeGlobalContext() throws ConfigPropertiesException {
		configurationMapGlobalContext = getKeyPathTree(true);
		return configurationMapGlobalContext;
	}
}


