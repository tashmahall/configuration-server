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

import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertyType;

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
	 * Path to the configuration value in the configuration server.
	 * The application context name must be disregard
	 * e.g.
	 * 	If the keyPath real configuration value is /app-context-name/address-keyPath, 
	 *  in the 'keyPath' attribute must be used just '/address-keyPath'
	 * 
	 * @return the 'keyPath' Value. 
	 */
	@Nonbinding
	public String value() ;
	/**
	 * The {@link ZookeeperServicePropertyType} define the strategy that will be used to get the 'keyPath' value.
	 * <b>ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER</b>
	 * 	That strategy is the default strategy used. In this case the Zookeeper get the 'keyPath' value from the Configuration server
	 *  each time that is required and doesn't create Watchers to watch changes happened over the 'keyPath' value.  
	 * 
	 * <b>ZookeeperServicePropertyType.APPLICATION_SCOPED_WITH_WATCHER</b>
	 * 	In this case the Zookeeper get the 'keyPath' value from a Map previously loaded from the Configuration server
	 *  and stored in Application Scope. 
	 *  I this strategy it creates an watcher to each 'keyPath', so when the 'keyPath' value change its value the Map previously loaded 
	 *  is updated with the new value.  
	 *  
	 * <b>ZookeeperServicePropertyType.GLOBAL_CONTEXT_NO_WATCHER</b>
	 * 	In this case the Zookeeper get the 'keyPath' value from the Global context in the Configuration server
	 *  each time that is required and doesn't create Watchers to watch changes happened over the 'keyPath' value.  
	 *	
	 * @return the 'configPropertyType'.
	 */
	@Nonbinding	
	 public ZookeeperServicePropertyType configPropertyType() default ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER;
}
