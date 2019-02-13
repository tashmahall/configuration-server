package com.zookeeper_utils.configuration_server.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.validation.constraints.NotNull;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.log4j.Logger;

import com.zookeeper_utils.configuration_server.watchers.ConfigurationEventWatcher;
import com.zookeeper_utils.configuration_server.watchers.ConfigurationTreeWatcher;

public class ZookeeperService implements Serializable {
	/**
	 * 
	 */
	public static final String CONFIGURATION_TREE = "configurationTree";
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(this.getClass());
	
	private CuratorFramework clientZookeeper;
	private AsyncCuratorFramework async;
	private static ZookeeperService instance;
	

    public ZookeeperService() {
    	org.apache.log4j.BasicConfigurator.configure();
    }
    private ZookeeperService(@NotNull String host,@NotNull String port) {
    	org.apache.log4j.BasicConfigurator.configure();
    	instance = new ZookeeperService();
    	RetryPolicy retryPolicy = new RetryNTimes(0, 60000);
    	instance.clientZookeeper = CuratorFrameworkFactory.newClient(host+":"+port, retryPolicy);
    	instance.async = AsyncCuratorFramework.wrap(instance.clientZookeeper);
    	instance.clientZookeeper.start();
    }
    public static ZookeeperService getInstance(@NotNull String host,@NotNull String port) {
    	if(instance==null) {
    		return new ZookeeperService(host,port);
    	}
		return instance;
    }

	public Map<String,String> getNewConfigurationMap(@NotNull String... keys) throws Exception {
		Map<String,String> configurationMap = new TreeMap<String,String>();
		for(String key: keys) {
			configurationMap.put(key, new String (instance.clientZookeeper.getData().forPath(key)));
			log.debug("Key Path ["+key+"] - Configuration Data ["+new String(instance.clientZookeeper.getData().forPath(key))+"]");
			instance.async.watched().checkExists().forPath(key).event().thenAccept(new ConfigurationEventWatcher(instance.clientZookeeper,configurationMap));
		}
		return configurationMap;
	}
	public Map<String,String> getConfigurationTree(@NotNull String contextName) throws Exception{
		Map<String,String> configurationMap = new TreeMap<String, String>();
		instance.treeGenerator(contextName, configurationMap);
		return configurationMap;
	}
	private void treeGenerator(String context, Map<String, String> configurationMap) throws Exception {
		List<String> list = instance.clientZookeeper.getChildren().forPath(context);
		for(String child:list) {
			String newCtx = context+"/"+child;
			List<String> list2 = instance.clientZookeeper.getChildren().forPath(newCtx);
			if(list2 == null || list2.isEmpty()) {
				defineWatcher(context, configurationMap, newCtx);
			}else {
				try {
					treeGenerator(newCtx, configurationMap);
					defineWatcher(context, configurationMap, newCtx);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	private void defineWatcher(String context, Map<String, String> configurationMap, String newCtx) {
		try {
			byte[] configuration = instance.clientZookeeper.getData().forPath(newCtx); 
			if(configuration != null && configuration.length>0) {
				String entry = new  String(configuration);
				configurationMap.put(newCtx, entry);
			}else {
				configurationMap.put(newCtx,null);
			}
			instance.async.watched().checkExists().forPath(context).event().thenAccept(new ConfigurationEventWatcher(instance.clientZookeeper,configurationMap));
			instance.async.watched().getChildren().forPath(context).event().thenAccept(new ConfigurationTreeWatcher(instance.clientZookeeper,configurationMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


