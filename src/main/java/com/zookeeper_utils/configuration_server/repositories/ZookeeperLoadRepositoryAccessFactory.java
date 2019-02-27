package com.zookeeper_utils.configuration_server.repositories;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesApplicationScoped;



/**
 * Produz objetos {@link ZookeeperServicePropertiesApplicationScoped} 
 * a partir do Zookeeper {@code .properties} localizados no <i>ClassLoader</i> da aplicaÃ§Ã£o.
 * 
 * <p>Este <i>producer</i> reconhece informaÃ§Ãµes da anotaÃ§Ã£o {@link FileMessage}.</p>
 * 
 * @author igor.ferreira
 *
 */
//TODO PROBABLY REMOVE THIS CLASS IN THE END
@ApplicationScoped
public class ZookeeperLoadRepositoryAccessFactory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	public ZookeeperRepositoryWithWhatcher produce(InjectionPoint injectionPoint) throws ConfigPropertiesException{
		return build();
	}
	public ZookeeperRepositoryWithWhatcher build() throws ConfigPropertiesException{
//		String fileName = "zookeeper.properties";
//		Properties properties = new Properties();
//		properties.load(ZookeeperRepositoryWithWhatcher.class.getClassLoader().getResourceAsStream(fileName));
//		String host = properties.getProperty("zookeeper.host");
//		String port = properties.getProperty("zookeeper.port");
		ZookeeperRepositoryWithWhatcher zc = new ZookeeperRepositoryWithWhatcher();
		return zc;
	}


}