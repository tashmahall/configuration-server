package com.zookeeper_utils.configuration_server.properties;

import java.text.MessageFormat;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.lang3.ArrayUtils;

import com.zookeeper_utils.configuration_server.service.ZookeeperService;

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
@ApplicationScoped
public class ZookeeperConfigProperties {

	private Map<String, String> properties;
	private ZookeeperService zc;
	
	public ZookeeperConfigProperties(ZookeeperService zookeeperClient){
		zc = zookeeperClient;
	}
	
	/**
	 * Retorna a mensagem mapeada por chave específica.
	 * 
	 * @param key a chave associada a configuração que será¡ retornada
	 * @return Valor da propriedade
	 * @throws Exception 
	 */
    public String getMessage(String key) throws Exception{
    	if (properties==null||!properties.containsKey(key)) {
    		properties = zc.getNewConfigurationMap(key); 
    	}
        return (String) properties.get(key);
    }
    
    /**
     * Retorna a mensagem mapeada por chave especÃ­fica e carrega parÃ¢metros.
     * 
     * <p>Os parÃ¢metros sÃ£o carregados pela ordem q sÃ£o mapeados na mensagem.</p>
     * 
     * @param key chave associada a mensagem que serÃ¡ retornada
     * @param args parÃ¢metros a ser adcionados na mensagem
     * @return Valor da propriedade
     * @throws Exception 
     */
    public String getMessage(String key, Object... args) {
        try {
			return MessageFormat.format(getMessage(key), args);
		} catch (Exception e) {
			return "mensagem padrão de erro";
		}
    }

	public Map<String, String> getProperties() {
		return properties;
	}
	
    public String getConfigurationTree(String contextName) throws Exception{
    	properties = zc.getConfigurationTree(contextName);
    	return ArrayUtils.toString(properties.keySet().toArray());
    }
}
