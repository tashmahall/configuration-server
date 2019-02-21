package com.zookeeper_utils.configuration_server.repositories;

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

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.watchers.ConfigurationEventWatcher;
import com.zookeeper_utils.configuration_server.watchers.ConfigurationTreeWatcher;

public class ZookeeperRepository implements Serializable {
	/**
	 * 
	 */
	public static final String CONFIGURATION_TREE = "configurationTree";
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger(this.getClass());
	
	private CuratorFramework clientZookeeper;
	private AsyncCuratorFramework async;
	
    public ZookeeperRepository (@NotNull String host,@NotNull String port) {
    	org.apache.log4j.BasicConfigurator.configure();
    	RetryPolicy retryPolicy = new RetryNTimes(0, 60000);
    	clientZookeeper = CuratorFrameworkFactory.newClient(host+":"+port, retryPolicy);
    	async = AsyncCuratorFramework.wrap(clientZookeeper);
    	clientZookeeper.start(); 
   		
    }
	public String getConfigurationValueUnwatchedWay(@NotNull String key) throws ConfigPropertiesException  {
		String value;
		try {
			value = new String (this.clientZookeeper.getData().forPath(key));
			log.debug("Key Path ["+key+"] - Configuration Data ["+new String(this.clientZookeeper.getData().forPath(key))+"]");
			return value;
		} catch (Exception e) {
			throw new ConfigPropertiesException("Erro ao tentar carregar a propriedade com 'keyPath' ["+key+"]");
		}

	}

	public Map<String,String> getNewConfigurationMap(@NotNull String... keys) throws Exception {
		Map<String,String> configurationMap = new TreeMap<String,String>();
		for(String key: keys) {
			configurationMap.put(key, new String (this.clientZookeeper.getData().forPath(key)));
			log.debug("Key Path ["+key+"] - Configuration Data ["+new String(this.clientZookeeper.getData().forPath(key))+"]");
			this.async.watched().checkExists().forPath(key).event().thenAccept(new ConfigurationEventWatcher(this.clientZookeeper,configurationMap));
		}
		return configurationMap;
	}
	public Map<String,String> getConfigurationTree(@NotNull String contextName) throws ConfigPropertiesException{
		Map<String,String> configurationMap = new TreeMap<String, String>();
		this.treeGenerator(contextName, configurationMap);
		return configurationMap;
	}
	private void treeGenerator(String context, Map<String, String> configurationMap) throws ConfigPropertiesException {
		
		try {
			List<String> list = this.clientZookeeper.getChildren().forPath(context);
			for(String child:list) {
				String newCtx = context+"/"+child;
				List<String> list2 = this.clientZookeeper.getChildren().forPath(newCtx);
				if(list2 == null || list2.isEmpty()) {
					defineWatcher(context, configurationMap, newCtx);
				}else {
					treeGenerator(newCtx, configurationMap);
					defineWatcher(context, configurationMap, newCtx);
				}
			}
		} catch (Exception e1) {
			throw new ConfigPropertiesException(e1.getMessage(),e1);
		}

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
				throw new ConfigPropertiesException("Erro ao definir o valor da propriedade para o 'keyPath' ["+newCtx+"]",e);
			} 
}
}


