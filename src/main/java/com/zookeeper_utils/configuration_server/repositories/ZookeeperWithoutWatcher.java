package com.zookeeper_utils.configuration_server.repositories;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.validation.constraints.NotNull;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.log4j.Logger;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;

public class ZookeeperWithoutWatcher implements ZookeeperRepositoryInterface {

	private final Logger log = Logger.getLogger(this.getClass());

	@Inject
	private ServletContext context;	
	private CuratorFramework clientZookeeper;
	public ZookeeperWithoutWatcher (@NotNull String host,@NotNull String port) {
    	org.apache.log4j.BasicConfigurator.configure();
    	RetryPolicy retryPolicy = new RetryNTimes(0, 60000);
    	clientZookeeper = CuratorFrameworkFactory.newClient(host+":"+port, retryPolicy);
    	clientZookeeper.start(); 
	}
	public String getValueFromKeyPath(@NotNull String keyPath) throws ConfigPropertiesException  {
		String realKeyPath ="/"+context.getServletContextName()+keyPath;
		String value;
		try {
			value = new String (this.clientZookeeper.getData().forPath(realKeyPath));
			log.debug("Key Path ["+keyPath+"] - Configuration Data ["+new String(this.clientZookeeper.getData().forPath(keyPath))+"]");
			return value;
		} catch (Exception e) {
			throw new ConfigPropertiesException("Erro ao tentar carregar a propriedade com 'keyPath' ["+keyPath+"]",e);
		}
	}
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException{
		String realcontextName ="/"+context.getServletContextName();
		Map<String,String> configurationMap = new TreeMap<String, String>();
		this.treeGenerator(realcontextName, configurationMap);
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
			} catch (Exception e) {
				throw new ConfigPropertiesException("Erro ao definir o valor da propriedade para o 'keyPath' ["+newCtx+"]",e);
			} 
	}
}
