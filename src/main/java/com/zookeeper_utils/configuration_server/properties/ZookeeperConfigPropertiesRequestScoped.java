package com.zookeeper_utils.configuration_server.properties;

import java.util.Map;

import javax.enterprise.context.RequestScoped;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryNoWatcher;


/**
 * Class used to load the configuration from Config Server
 * 
 * 
 * 
 * @author igor.ferreira
 *
 */
@RequestScoped
 public class ZookeeperConfigPropertiesRequestScoped {

	/**
	 * 
	 */
	@ZKReopositoryNoWatcher
	private ZookeeperRepositoryInterface zc;
	public ZookeeperConfigPropertiesRequestScoped () {}
	/**
	 * Return the value for the keyPath informed.
	 * 
	 * @param keyPath the key associated to a configuration property.
	 * @return Property value
	 * @throws ConfigPropertiesException 
	 * @throws Exception 
	 */
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
    		return this.zc.getKeyPathTree(); 
	}
}
