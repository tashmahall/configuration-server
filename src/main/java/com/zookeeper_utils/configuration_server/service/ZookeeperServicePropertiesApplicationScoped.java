package com.zookeeper_utils.configuration_server.service;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.repositories.annotations.ZKReopositoryWithWatcher;
import com.zookeeper_utils.configuration_server.service.annotations.ZKServicePropertiesAppScoped;


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
	@ZKReopositoryWithWatcher
	private ZookeeperRepositoryInterface zc;
	@Inject
	private ServletContext context;
	/**
	 * Return the value for the keyPath informed.
	 * 
	 * @param keyPath the key associated to a configuration property.
	 * @return Property value
	 * @throws ConfigPropertiesException when there is a problem to return the 'keyPath' value.
	 */
    public String getPropertyValue(String keyPath) throws ConfigPropertiesException {
    	if (this.properties==null) {
    		this.properties = this.zc.getKeyPathTree(); 
    	}
    	String realContext = StringUtils.join("/",context.getServletContextName(),keyPath);
    	return properties.get(realContext);
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
     * @throws ConfigPropertiesException when there is a problem to return the 'keyPathTree'.
     */
    public Map<String,String> updateAllConfigurationTree() throws ConfigPropertiesException {
    	this.properties = this.zc.getKeyPathTree();
    	return this.properties;
    }
}
