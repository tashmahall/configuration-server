package com.zookeeper_utils.configuration_server.repositories;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


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
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
public @interface ZookeeperLoadRepositoryAccess {
	/**
	 * Nome do arquivo de mensagem.
	 * <p>Este valor é obrigatório.
	 * <br>A extensão {@code .properties} pode ser suprimida.</p>
	 * 
	 * @return retorna o nome do arquivo. 
	 */
}
