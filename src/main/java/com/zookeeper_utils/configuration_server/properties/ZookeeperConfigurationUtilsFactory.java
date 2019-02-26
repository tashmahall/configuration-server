package com.zookeeper_utils.configuration_server.properties;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryWithWhatcher;



/**
 * Produz objetos {@link ZookeeperConfigPropertiesApplicationScoped} 
 * a partir do Zookeeper {@code .properties} localizados no <i>ClassLoader</i> da aplicaÃ§Ã£o.
 * 
 * <p>Este <i>producer</i> reconhece informaÃ§Ãµes da anotaÃ§Ã£o {@link FileMessage}.</p>
 * 
 * @author igor.ferreira
 *
 */
@ApplicationScoped
public class ZookeeperConfigurationUtilsFactory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private ZookeeperRepositoryWithWhatcher zc;
	@Produces
	public ZookeeperConfigPropertiesApplicationScoped produce(InjectionPoint injectionPoint) throws IOException{
		return build();
	}
	
	public ZookeeperConfigPropertiesApplicationScoped build() throws IOException{
		String fileName = "zookeeper.properties";
		Properties properties = new Properties();
		properties.load(ZookeeperConfigPropertiesApplicationScoped.class.getClassLoader().getResourceAsStream(fileName));
		String contextName = properties.getProperty("application.context.name");
//		return new ZookeeperConfigPropertiesApplicationScoped(zc, contextName);
		return null;
	}


}