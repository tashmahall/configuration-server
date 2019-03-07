package com.zookeeper_utils.configuration_server.repositories;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.curator.framework.CuratorFramework;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

class ZookeeperNoWatcherKeyPathTreeGenerator implements ZookeeperKeyPathGenerator{
	private String gotError = "Got the error ";
	public Map<String,String> getKeyPathTree(String contextName,CuratorFramework clientZookeeper) throws ConfigPropertiesException{
		Map<String,String> configurationMap = new TreeMap<>();
		this.treeGenerator(contextName, configurationMap,clientZookeeper);
		return configurationMap;
	}
	
	private void treeGenerator(String context, Map<String, String> configurationMap,CuratorFramework clientZookeeper) throws ConfigPropertiesException {
		try {
			List<String> list = clientZookeeper.getChildren().forPath(context);
			for(String child:list) {
				String newCtx = context+"/"+child;
				List<String> list2 = clientZookeeper.getChildren().forPath(newCtx);
				if(list2 == null || list2.isEmpty()) {
					defineWatcher( configurationMap, newCtx,clientZookeeper);
				}else {
					treeGenerator(newCtx, configurationMap,clientZookeeper);
					defineWatcher( configurationMap, newCtx,clientZookeeper);
				}
			}
		}catch(ConfigPropertiesException ce) {
			throw ce;
		}
		catch (Exception e1) {
			throw new ConfigPropertiesException(gotError+e1.getMessage()+" while getting children properties to the 'keyPath' ["+context+"]",e1);
		}
	}
	private void defineWatcher(Map<String, String> configurationMap, String newCtx,CuratorFramework clientZookeeper) throws ConfigPropertiesException {
			try {
				byte[] configuration = clientZookeeper.getData().forPath(newCtx);
				if(configuration != null && configuration.length>0) {
					String entry = new  String(configuration);
					configurationMap.put(newCtx, entry);
				}else {
					configurationMap.put(newCtx,null);
				}
			} catch (Exception e) {
				throw new ConfigPropertiesException(gotError+e.getMessage()+" while load the properties to the 'keyPath' ["+newCtx+"]",e);
			} 
	}

}
