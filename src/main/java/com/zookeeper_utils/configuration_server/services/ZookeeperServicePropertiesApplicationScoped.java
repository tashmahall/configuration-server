package com.zookeeper_utils.configuration_server.services;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryWatcher;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesAppScoped;


/**
 * Class used to load the configuration from Config Server
 * 
 * 
 * 
 * @author igor.ferreira
 *
 */
@ApplicationScoped
@ZKServicePropertiesAppScoped
 public class ZookeeperServicePropertiesApplicationScoped  implements ZookeeperServicePropertiesInterface{

	/**
	 * 
	 */
	private Map<String, String> properties;
	@Inject
	@ZKReopositoryWatcher
	private ZookeeperRepositoryInterface zc;
	public ZookeeperServicePropertiesApplicationScoped () {}
	/**
	 * Return the value for the keyPath informed.
	 * 
	 * @param keyPath the key associated to a configuration property.
	 * @return Property value
	 * @throws ConfigPropertiesException 
	 * @throws Exception 
	 */
	//TODO adjust the class to get value from the local properties Map
    public String getPropertyValue(String keyPath) throws ConfigPropertiesException {
    	String configuration=zc.getValueFromKeyPath(keyPath);
        return configuration;
    }
     
    /**
     * Return the properties Map with the keyPaths and related values that there are in the config server.
     * 
     * <p>The properties Map is loaded in the order that are stored in the config server.</p>
     * 
     * @return properties Map 
     * @throws ConfigPropertiesException 
     */
	public Map<String, String> getPropertiesMap() throws ConfigPropertiesException {
    	if (this.properties==null) {
    		this.properties = this.zc.getKeyPathTree(); 
    	}
		return this.properties;

	}
    /**
     * Even existing the watcher that leaves the properties Map updated, with this method is possible to reload all the properties Map at one time.
     * 
      * <p>The properties Map is loaded in the order that are stored in the config server.</p>
     * 

     * @return Properties Map
     * @throws ConfigPropertiesException 
     */
    public Map<String,String> updateAllConfigurationTree() throws ConfigPropertiesException {
    	this.properties = this.zc.getKeyPathTree();
    	return this.properties;
    }
}
