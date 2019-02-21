package com.zookeeper_utils.configuration_server.repositories;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

import com.zookeeper_utils.configuration_server.properties.ZookeeperConfigProperties;



/**
 * Produz objetos {@link ZookeeperConfigProperties} 
 * a partir do Zookeeper {@code .properties} localizados no <i>ClassLoader</i> da aplicaÃ§Ã£o.
 * 
 * <p>Este <i>producer</i> reconhece informaÃ§Ãµes da anotaÃ§Ã£o {@link FileMessage}.</p>
 * 
 * @author igor.ferreira
 *
 */
@ApplicationScoped
public class ZookeeperLoadRepositoryAccessFactory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	public ZookeeperRepository produce(InjectionPoint injectionPoint) throws IOException{
		return build();
	}
	public ZookeeperRepository build() throws IOException{
		String fileName = "zookeeper.properties";
		Properties properties = new Properties();
		properties.load(ZookeeperRepository.class.getClassLoader().getResourceAsStream(fileName));
		String host = properties.getProperty("zookeeper.host");
		String port = properties.getProperty("zookeeper.port");
		ZookeeperRepository zc = new ZookeeperRepository(host, port);
		return zc;
	}


}