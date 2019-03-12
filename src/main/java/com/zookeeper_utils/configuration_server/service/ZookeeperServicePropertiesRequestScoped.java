package com.zookeeper_utils.configuration_server.service;

import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryNoWatcher;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesRequestScoped;


/**
 * Class used to load the configuration from Config Server
 * 
 * 
 * 
 * @author igor.ferreira
 *
 */
@RequestScoped
@ZKServicePropertiesRequestScoped
 public class ZookeeperServicePropertiesRequestScoped implements ZookeeperServicePropertiesInterface {

	/**
	 * 
	 */
	@Inject
	@ZKReopositoryNoWatcher
	private ZookeeperRepositoryInterface zc;
	/**
	 * Return the value for the keyPath informed.
	 * 
	 * @param keyPath the key associated to a configuration property.
	 * @return Property value
	 * @throws ConfigPropertiesException when there is a problem to return the 'keyPath' value.
	 */
    public String getPropertyValue(String keyPath) throws ConfigPropertiesException {
    	return zc.getValueFromKeyPath(keyPath);
    }
     
    /**
     * Return the properties Map with the keyPaths and related values that there are in the config server.
     * 
     * <p>The properties Map is loaded in the order that are stored in the config server.</p>
     * 
     * @return properties Map 
     * @throws ConfigPropertiesException when there is a problem to return the 'keyPathTree'.
     */
	public Map<String, String> getPropertiesMap() throws ConfigPropertiesException {
    		return this.zc.getKeyPathTree(); 
	}
}
