package com.zookeeper_utils.configuration_server.repositories.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
/**
 * Verifica se o 'value' informado através da anotação é válido.
 * O processo de validação segue algumas regras. No final do processo, é possível ter certeza de que o 'value' informado é válido.
 * Se o 'value' informado infringiu alguma regra, a validação lançará a exceção {@link ConfigPropertiesException} notificando o problema.
 * 
 * 
 * @author igor.ferreira
 * 
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER,METHOD,TYPE })
public @interface SanitizeKeyPath {

}
