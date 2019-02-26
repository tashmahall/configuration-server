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
import com.zookeeper_utils.configuration_server.properties.ConfigProperties;
/**
 * Verify if the 'keyPath' informed through the the annotation {@link ConfigProperties} is valid.
 * The validation process follows some rules. In the final of process, it's possible to be sure that the 'keyPath' informed is valid.
 * If the 'keyPath' informed broke any rule the validation throws the exception {@link ConfigPropertiesException} notifying the problem.
 * 
 * 
 * @author igor.ferreia
 * 
 */
@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER,METHOD,TYPE })
public @interface SanitizeKeyPath {

}
