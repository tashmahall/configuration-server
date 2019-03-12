package com.zookeeper_utils.configuration_server.service;

/**
 * Ways to load the Configuration properties from the configuration server.
 * 
 * @author igor.ferreira
 *
 */
public enum ZookeeperServicePropertyType {
	/** Load Global configurations from Config Server. 
	 */
	GLOBAL_CONTEXT_NO_WATCHER, 
	
	/** Load the application context configuration in the memory. The configurations stay stored in the Map Application Scoped.
	 * Create watchers to update the configuration in real time, if the information is changed in the configuration server.
	 */
	APPLICATION_SCOPED_WITH_WATCHER, 
	
	/** Load the application context configuration in the memory. The configurations is loaded Request Scoped. 
	 */
	REQUEST_SCOPED_NO_WATCHER
}
