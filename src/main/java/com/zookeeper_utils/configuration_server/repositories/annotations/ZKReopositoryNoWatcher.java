package com.zookeeper_utils.configuration_server.repositories.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryWithoutWatcher;
/**
 * Inject the Zookeeper Repository with context properties {@link ZookeeperRepositoryWithoutWatcher} in this case, the repository does not create
 * watcher to watch properties changes.
 * If the zookeeper.properties file doesn't exist it throws the exception {@link ConfigPropertiesException} notifying the problem.
 * 
 * 
 * @author igor.ferreia
 * 
 */
@Documented
@Qualifier
@Retention(RUNTIME)
@Target({ FIELD,TYPE })
public @interface ZKReopositoryNoWatcher {
	
}
