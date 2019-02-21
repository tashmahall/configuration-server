package com.zookeeper_utils.configuration_server.properties;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;

/**
 * Declara informações do arquivo {@code .properties} 
 * para produção de objetos {@link ZookeeperConfigPropeperties}. 
 * 
 * <ul>
 * 	<li>O atributo {@code value} informa o nome do arquivo.</li>
 * </ul>
 * 
 * 
 * @author igor.ferreira
 * 
 */
@Documented
@Retention(RUNTIME)
@Target({FIELD,TYPE,METHOD})
public @interface ConfigProperties {
	/**
	 * Nome do arquivo de mensagem.
	 * <p>Este valor é obrigatório.
	 * <br>A extensão {@code .properties} pode ser suprimida.</p>
	 * 
	 * @return retorna o nome do arquivo. 
	 */
	@Nonbinding 
	String keyPath();
	/**
	 * Origem do arquivo. 
	 * <p>Se não informado, retorna o valor default {@code PropertyType.CONFIG_SERVER}.</p>
	 * 
	 * @return Retorna a origem do arquivo.
	 */
}
