package com.zookeeper_utils.configuration_server.properties.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType;

/**
 * 
 * @author igor.ferreira
 * 
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({FIELD,TYPE,METHOD})
public @interface ConfigProperties {
	/**
	 * Nome do arquivo de mensagem.
	 * <p>Este valor é obrigatório.
	 * 
	 * @return retorna o nome do keyPath. 
	 */
	@Nonbinding
	public String keyPath() ;
	/**
	 * Origem do arquivo. 
	 * <p>Se não informado, retorna o valor default {@code PropertyType.CONFIG_SERVER}.</p>
	 * 
	 * @return Retorna a origem do arquivo.
	 */
	@Nonbinding	
	 public ZookeeperServicePropertyType configPropertyType() default ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER;
}
