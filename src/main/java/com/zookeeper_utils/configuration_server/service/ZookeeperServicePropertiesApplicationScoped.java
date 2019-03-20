package com.zookeeper_utils.configuration_server.service;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
	@Inject
	@ZKReopositoryWithWatcher
	private ZookeeperRepositoryInterface zc;
	/**
	 * Return the value for the keyPath informed.
	 * (Retorna o valor para o 'keyPath' informdo.)
	 * 
	 * @param keyPath the key associated to a configuration property (Chave associada a uma configuração).  
	 * @param isGlobal the 'keyPath' informed is related to a configuration property in a global context. (A 'keyPath' está associada a uma configuração em contexto global).
	 * @return Property value
	 * @throws ConfigPropertiesException when there is a problem to return the 'keyPath' value.
	 */
    public String getPropertyValue(String keyPath,boolean isGlobal) throws ConfigPropertiesException {
    	if(isGlobal) {
    		return zc.getValueFromKeyPathGlobalContext(keyPath);
    	}
    	return zc.getValueFromKeyPathApplicationContext(keyPath);
    }
    /**
     * Return the properties Map with the keyPaths and related values that there are in the config server.
     * (Retorna o Map de propriedades com os 'keyPaths' e valores relacionados que existem no config Server.)
     * <p>The properties Map is loaded in the order that are stored in the config server.</p>
     * <p>(O Map de propriedades é carregado na ordem que foi guardado no Config Server).</p>
     * 
     * @return properties Map 
     * @throws ConfigPropertiesException when there is a problem to return the 'keyPathTree'.
     */
	public Map<String, String> getPropertiesMap(boolean isGlobal) throws ConfigPropertiesException {
    	if(isGlobal) {
    		return zc.getKeyPathTreeGlobalContext();
    	}
    	return zc.getKeyPathTreeApplicationContext();
	}
}
