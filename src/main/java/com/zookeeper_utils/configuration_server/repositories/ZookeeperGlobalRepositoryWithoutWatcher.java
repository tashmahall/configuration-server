package com.zookeeper_utils.configuration_server.repositories;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.annotations.SanitizeKeyPath;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKGlobalReopositoryNoWatcher;


@ZKGlobalReopositoryNoWatcher
public class ZookeeperGlobalRepositoryWithoutWatcher implements ZookeeperRepositoryInterface {

	/**
	 * 
	 */
	public static final RetryPolicy RETRY_POLICY = new RetryNTimes(0, 60000);
	private String gotError = "Got the error ";
	//TODO verificar com Samuel qual vai ser o repositorio global
	private String globalRepository = "ans";
	private CuratorFramework clientZookeeper;
	public ZookeeperGlobalRepositoryWithoutWatcher () throws ConfigPropertiesException {
    	clientZookeeper = CuratorFrameworkFactory.newClient(loadHostAndPort(), RETRY_POLICY);
    	clientZookeeper.start(); 
	}
	public String getValueFromKeyPath(@SanitizeKeyPath String keyPath) throws ConfigPropertiesException  {
		String realKeyPath = StringUtils.join("/",globalRepository,keyPath);
		String value;
		try {
			value = new String (this.clientZookeeper.getData().forPath(realKeyPath));
			return value;
		} catch (Exception e) {
			throw new ConfigPropertiesException(gotError+e.getMessage()+" while load the properties to the 'keyPath' ["+keyPath+"]",e);
		}
	}
	public Map<String,String> getKeyPathTree() throws ConfigPropertiesException{
		String realcontextName =StringUtils.join("/",globalRepository);
		Map<String,String> configurationMap = new TreeMap<>();
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
					defineWatcher( configurationMap, newCtx);
				}else {
					treeGenerator(newCtx, configurationMap);
					defineWatcher( configurationMap, newCtx);
				}
			}
		}catch(ConfigPropertiesException ce) {
			throw ce;
		}
		catch (Exception e1) {
			throw new ConfigPropertiesException(gotError+e1.getMessage()+" while getting children properties to the 'keyPath' ["+context+"]",e1);
		}
	}
	private void defineWatcher( Map<String, String> configurationMap, String newCtx) throws ConfigPropertiesException {
			try {
				byte[] configuration = this.clientZookeeper.getData().forPath(newCtx);
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
	public String getContextNameRepository() {
		return globalRepository;
	}
}
