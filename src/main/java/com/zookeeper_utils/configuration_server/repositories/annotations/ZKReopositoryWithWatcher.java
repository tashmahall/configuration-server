package com.zookeeper_utils.configuration_server.repositories.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryWithWhatcher;
/**
 * Inject the Zookeeper Repository with context properties {@link ZookeeperRepositoryWithWhatcher} in this case, the repository create
 * watcher to watch properties changes.
 * 
 * 
 * @author igor.ferreira
 * 
 */
@Documented
@Qualifier
@Retention(RUNTIME)
@Target({ FIELD,TYPE })
public @interface ZKReopositoryWithWatcher {
	
}
