package com.zookeeper_utils.configuration_server.repositories;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.x.async.AsyncCuratorFramework;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKKeyPathTreeGeneratorWithWatcher;
import com.zookeeper_utils.configuration_server.repositories.watchers.ConfigurationEventWatcher;
import com.zookeeper_utils.configuration_server.repositories.watchers.ConfigurationTreeWatcher;
@ZKKeyPathTreeGeneratorWithWatcher
class ZookeeperKeyPathTreeGeneratorWithWatcher implements ZookeeperKeyPathGenerator{
	private String gotError = "Got the error ";
	private CuratorFramework clientZookeeper;
	private AsyncCuratorFramework async;
	
	public Map<String,String> getKeyPathTree(String contextName,CuratorFramework clientZookeeper) throws ConfigPropertiesException{
		this.clientZookeeper = clientZookeeper;
		loadClientZookeeper();
		Map<String,String> configurationMap = new TreeMap<>();
		this.treeGenerator(contextName, configurationMap);
		return configurationMap;
	}
	
	private void defineWatcher(String context, Map<String, String> configurationMap, String newCtx) throws ConfigPropertiesException {
		try {
			byte[] configuration = this.clientZookeeper.getData().forPath(newCtx);
			if(configuration != null && configuration.length>0) {
				String entry = new  String(configuration);
				configurationMap.put(newCtx, entry);
			}else {
				configurationMap.put(newCtx,null);
			}
			this.async.watched().checkExists().forPath(context).event().thenAccept(new ConfigurationEventWatcher(this.clientZookeeper,configurationMap));
			this.async.watched().getChildren().forPath(context).event().thenAccept(new ConfigurationTreeWatcher(this.clientZookeeper,configurationMap));
		} catch (Exception e) {
			throw new ConfigPropertiesException(gotError+e.getMessage()+" while load the properties to the 'keyPath' ["+newCtx+"]",e);
		} 
	}
	private void loadClientZookeeper() throws ConfigPropertiesException {
    	if(clientZookeeper==null||clientZookeeper.getState().equals(CuratorFrameworkState.STOPPED)) {
    		throw new ConfigPropertiesException("CuratorFramework null or CuratorFramework State is stopped");
    	}
		async = AsyncCuratorFramework.wrap(clientZookeeper);
    	clientZookeeper.start(); 
	}
	private void treeGenerator(String context, Map<String, String> configurationMap) throws ConfigPropertiesException {
		String newCtx = null;
		try {
			List<String> list = this.clientZookeeper.getChildren().forPath(context);
			for(String child:list) {
				newCtx = context+"/"+child;
				List<String> list2 = this.clientZookeeper.getChildren().forPath(newCtx);
				if(list2 == null || list2.isEmpty()) {
					defineWatcher(context, configurationMap, newCtx);
				}else {
					treeGenerator(newCtx, configurationMap);
					defineWatcher(context, configurationMap, newCtx);
				}
			}
		}catch(ConfigPropertiesException ce) {
			throw ce;
		}
		catch (Exception e1) {
			String path =StringUtils.isEmpty(newCtx)?context:newCtx;
			throw new ConfigPropertiesException(gotError+e1.getMessage()+" while getting children properties to the 'keyPath' ["+path+"]",e1);
		}
	}

}
