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
	 * O contexto da aplica��o deve ser desconsiderado.
	 * Ex.
	 * 	Se o valor real para a configura��o � '/app-context-name/address-keyPath', 
	 *  no atributo 'value' deve ser usado '/address-keyPath'
	 * 
	 * @return o valor do caminho. 
	 */
	@Nonbinding
	public String value() ;
	/**
	 * O {@link ZookeeperServiceScopeType} define o escopo que ser� mantido a valor carregado do Config Server.
	 * <b>ZookeeperServicePropertyType.REQUEST_SCOPED</b>
	 * 	Esse escopo � o padr�o usado quando n�o definido na anota��o. Nesse escopo n�o s�o criados Observadores para os endere�os dos atributos
	 * 
	 * <b>ZookeeperServicePropertyType.APPLICATION_SCOPED</b>
	 * 	Nesse escopo o valor de um endere�o � pego de um Mapa pr�viamente carregado do Config Server e armazenado em escopo de aplica��o.
	 *  Nesse escopo s�o criados observadores para os endere�os de atributos. 
	 *  
	 * @return the 'configPropertyType'.
	 */
	@Nonbinding	
	 public ZookeeperServiceScopeType scopeType() default ZookeeperServiceScopeType.REQUEST_SCOPED;
	
	@Nonbinding	
	 public boolean global() default false;
}
