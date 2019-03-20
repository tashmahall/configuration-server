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

import com.zookeeper_utils.configuration_server.service.ZookeeperServiceScopeType;

/**
 * 
 * @author igor.ferreira
 * 
 */
@Qualifier
@Documented
@Retention(RUNTIME)
@Target({FIELD,TYPE,METHOD})
public @interface ConfigProperty {
	/**
	 * Caminho para o valor configurado no Config Server.
	 * O contexto da aplicação deve ser desconsiderado.
	 * Ex.
	 * 	Se o valor real para a configuração é '/app-context-name/address-keyPath', 
	 *  no atributo 'value' deve ser usado '/address-keyPath'
	 * 
	 * @return o valor do caminho. 
	 */
	@Nonbinding
	public String value() ;
	/**
	 * O {@link ZookeeperServiceScopeType} define o escopo que será mantido a valor carregado do Config Server.
	 * <b>ZookeeperServicePropertyType.REQUEST_SCOPED</b>
	 * 	Esse escopo é o padrão usado quando não definido na anotação. Nesse escopo não são criados Observadores para os endereços dos atributos
	 * 
	 * <b>ZookeeperServicePropertyType.APPLICATION_SCOPED</b>
	 * 	Nesse escopo o valor de um endereço é pego de um Mapa préviamente carregado do Config Server e armazenado em escopo de aplicação.
	 *  Nesse escopo são criados observadores para os endereços de atributos. 
	 *  
	 * @return the 'configPropertyType'.
	 */
	@Nonbinding	
	 public ZookeeperServiceScopeType scopeType() default ZookeeperServiceScopeType.REQUEST_SCOPED;
	
	@Nonbinding	
	 public boolean global() default false;
}
