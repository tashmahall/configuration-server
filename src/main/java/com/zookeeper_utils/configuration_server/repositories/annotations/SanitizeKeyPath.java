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
 * Verifica se o 'value' informado atrav�s da anota��o � v�lido.
�* O processo de valida��o segue algumas regras. No final do processo, � poss�vel ter certeza de que o 'value' informado � v�lido.
�* Se o 'value' informado infringiu alguma regra, a valida��o lan�ar� a exce��o {@link ConfigPropertiesException} notificando o problema.
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
