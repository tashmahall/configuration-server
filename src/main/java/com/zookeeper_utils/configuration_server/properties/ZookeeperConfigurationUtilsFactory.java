package com.zookeeper_utils.configuration_server.properties;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

import org.apache.commons.lang3.StringUtils;

import com.zookeeper_utils.configuration_server.service.ZookeeperService;


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
public class ZookeeperConfigurationUtilsFactory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Produces
	public ZookeeperConfigProperties produce(InjectionPoint injectionPoint) throws IOException{
		Annotated annotated = injectionPoint.getAnnotated();
		ZookeeperLoadConfiguration file = annotated.getAnnotation(ZookeeperLoadConfiguration.class)  ;
		String fileName = file != null ? StringUtils.appendIfMissing(file.value(), ".properties", ".properties") : "zookeeper.properties";
		return build(fileName);
	}
	
	public static ZookeeperConfigProperties build(String file) throws IOException{
		Properties properties = new Properties();
		properties.load(ZookeeperConfigProperties.class.getClassLoader().getResourceAsStream(file));
		String host = properties.getProperty("zookeeper.host");
		String port = properties.getProperty("zookeeper.port");
		ZookeeperService zc = ZookeeperService.getInstance(host, port);
		return new ZookeeperConfigProperties(zc);
	}


}