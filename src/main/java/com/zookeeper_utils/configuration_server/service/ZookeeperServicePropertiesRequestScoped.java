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
	 * (Retorna o valor para o 'keyPath' informado.)
	 * 
	 * @param keyPath the key associated to a configuration property (A chava associada a uma configuração).
	 * @return Property value
	 * @throws ConfigPropertiesException when there is a problem to return the 'keyPath' value.
	 */
    public String getPropertyValue(String keyPath, boolean isGlobal) throws ConfigPropertiesException {
    	if(isGlobal) {
    		return zc.getValueFromKeyPathGlobalContext(keyPath);
    	}
    	return zc.getValueFromKeyPathApplicationContext(keyPath);
    }
     
    /**
     * Return the properties Map with the keyPaths and related values that there are in the config server.
     * (Retorna o Map de propriedades com os 'keyPaths' e valores relacionados que existem no config server)
     * <p>The properties Map is loaded in the order that are stored in the config server.</p>
     * <p>(O Map é carregado na ordem que está armazenado no Config Server).</p>
     * @return properties Map 
     * @throws ConfigPropertiesException when there is a problem to return the 'keyPathTree'.
     */
	public Map<String, String> getPropertiesMap(boolean isGlobal) throws ConfigPropertiesException {
    	if(isGlobal) {
    		return zc.getKeyPathTreeGlobalContext();
    	}
		return this.zc.getKeyPathTreeApplicationContext(); 
	}
}
