package com.zookeeper_utils.configuration_server.properties;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepository;


/**
 * Classe utilitária para geração de mensagens a partir do Zookeeper.
 * 
 *  <ul>
 *  <li><b>getMessage</b>
 *      - retorna a mensagem a partir de uma chave específica.</li>
 *  </ul>
 *  
 * <p>Um Map é instaciado carregando as configurações existentes no Config Server Zookeeper</p>
 * 
 * 
 * @author igor.ferreira
 *
 */
 public class ZookeeperConfigProperties {

	/**
	 * 
	 */
	private Map<String, String> properties;
	private ZookeeperRepository zc;
	private String contextName;
	public ZookeeperConfigProperties (ZookeeperRepository zookeeperClient,String contextName) {
		zc = zookeeperClient;
		this.contextName = contextName;
	}
	/**
	 * Retorna o valor mapeado por chave específica.
	 * 
	 * @param key a chave associada a configuração que será¡ retornada
	 * @return Valor da propriedade
	 * @throws Exception 
	 */
    public String getPropertyValue(String key) throws Exception{
    	if (this.properties==null||!this.properties.containsKey(key)) {
    		this.properties = this.zc.getNewConfigurationMap("/"+contextName+"/"+key); 
    	}
        return (String) this.properties.get(key);
    }
	/**
	 * Retorna o valor mapeado por chave específica.
	 * Nesse método não é criado um watcher que obseva futuras mudanças do valor da chave
	 * 
	 * @param key a chave associada a configuração que será¡ retornada
	 * @return Valor da propriedade
	 * @throws ConfigPropertiesException 
	 */
    public String getPropertyValueUnwatchedWay(String key) throws ConfigPropertiesException {
    	String value = this.zc.getConfigurationValueUnwatchedWay(contextName+key); 
        return  value;
    }
    
    /**
     * Retorna o valor mapeado por chave específica e carrega parâmetros.
     * 
     * <p>Os parâmetros são carregados pela ordem que são mapeados no config server.</p>
     * 
     * @param key chave associada a mensagem que serÃ¡ retornada
     * @param args parãmetros a ser adcionados no valor
     * @return Valor da propriedade
     * @throws Exception 
     */
    public String getMessage(String key, Object... args) {
        try {
        	if(args == null) {
        		return key; 
        	}
			return MessageFormat.format(this.getMessage(key), args[args.length-1]);
		} catch (Exception e) {
			return "mensagem padrão de erro";
		}
    }
    
    /**
     * Retorna o Map de propriedades com as chaves e valores existentes no config server.
     * 
     * <p>O Map de propriedades é carregada na ordem que são mapeados no config server.</p>
     * Caso o Map de propriedades ainda não exista na memória, é atualizada e retorna o existente no config server.
     * 
     * @return o Map de propriedades 
     * @throws ConfigPropertiesException 
     */
	public Map<String, String> getProperties() throws ConfigPropertiesException {
		this.updateConfigurationTree();
		return this.properties;

	}
    /**
     * Atualiza na memória e retorna a árvore de propriedades, mapeada no config server.
     * 
     * <p>A árvore de propriedades é carregada na ordem que são mapeados no config server.</p>
     * 

     * @return árvore de propriedades
     * @throws ConfigPropertiesException 
     */
    public String updateConfigurationTree() throws ConfigPropertiesException {
    	this.properties = this.zc.getConfigurationTree(this.contextName);
    	return ArrayUtils.toString(this.properties.keySet().toArray());
    }
    /**
     * Retorna a árvore de propriedades existente na memória, mapeada no config server.
     * Caso a árovre ainda não exista na memória, é atualizada e retorna a existente no config server.
     * 
     * <p>A árvore de propriedades é carregada na ordem que são mapeados no config server.</p>
     * 

     * @return árvore de propriedades
     * @throws ConfigPropertiesException 
     */
    public String getConfigurationTree() throws ConfigPropertiesException {
    	if(this.properties == null) {
    		return this.updateConfigurationTree();
    	}
    	return ArrayUtils.toString(this.properties.keySet().toArray());
    }
}
