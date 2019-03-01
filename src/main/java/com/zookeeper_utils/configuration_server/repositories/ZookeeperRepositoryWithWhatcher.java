package com.zookeeper_utils.configuration_server.repositories;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.async.AsyncCuratorFramework;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryWatcher;
import com.zookeeper_utils.configuration_server.watchers.ConfigurationEventWatcher;
import com.zookeeper_utils.configuration_server.watchers.ConfigurationTreeWatcher;
@ZKReopositoryWatcher
public class ZookeeperRepositoryWithWhatcher implements ZookeeperRepositoryInterface {
	/**
	 * 
	 */
	public static final RetryPolicy RETRY_POLICY = new RetryNTimes(0, 60000);
	public static final String CONFIGURATION_TREE = "configurationTree";
	
	@Inject
	private ServletContext context;	
	private CuratorFramework clientZookeeper;
	private AsyncCuratorFramework async;
	private Map<String,String> configurationMap;
	
    public ZookeeperRepositoryWithWhatcher () throws ConfigPropertiesException {
    	clientZookeeper = CuratorFrameworkFactory.newClient(loadHostAndPort(), RETRY_POLICY);
    	async = AsyncCuratorFramework.wrap(clientZookeeper);
    	clientZookeeper.start(); 
    }

    @Override
	public Map<String, String> getKeyPathTree() throws ConfigPropertiesException {
		configurationMap = new TreeMap<>();
		String realContext = "/"+context.getServletContextName();
		this.treeGenerator(realContext, configurationMap);
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
		}catch(ConfigPropertiesException ce) {
			throw ce;
		}
		catch (Exception e1) {
			throw new ConfigPropertiesException("Got the error "+e1.getMessage()+" while getting children properties to the 'keyPath' ["+context+"]",e1);
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
				throw new ConfigPropertiesException("Got the error "+e.getMessage()+" while load the properties to the 'keyPath' ["+newCtx+"]",e);
			} 
	}

	@Override
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException {
		String realKeyPath = StringUtils.join("/",context.getServletContextName(),keyPath);
		if(configurationMap ==null ) {
			getKeyPathTree();
		} 
		return configurationMap.get(realKeyPath);
	}

}


