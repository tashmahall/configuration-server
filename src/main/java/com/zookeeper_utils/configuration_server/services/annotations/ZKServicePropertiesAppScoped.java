package com.zookeeper_utils.configuration_server.services.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesApplicationScoped;
/**
 * Inject the Zookeeper Service layer with Application Scope {@link ZookeeperServicePropertiesApplicationScoped}, in this case, 
 * the service layer uses an repository that create watcher to watch properties changes.
 * 
 * 
 * @author igor.ferreia
 * 
 */
@Documented
@Qualifier
@Retention(RUNTIME)
@Target({ TYPE, FIELD, PARAMETER })
public @interface ZKServicePropertiesAppScoped {

}
