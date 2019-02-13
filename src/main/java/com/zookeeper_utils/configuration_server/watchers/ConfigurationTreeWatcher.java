package com.zookeeper_utils.configuration_server.watchers;

import java.util.Map;
import java.util.function.Consumer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigurationTreeWatcher implements Consumer<WatchedEvent> {
	private final Logger log = Logger.getLogger(this.getClass());
	private CuratorFramework client;
	private Map<String,String> configurationMap;
	public ConfigurationTreeWatcher() {}
	public ConfigurationTreeWatcher(CuratorFramework client, Map<String,String> configurationMap) {
		org.apache.log4j.BasicConfigurator.configure();
		this.client = client;
		this.configurationMap = configurationMap;
	}
	
	@Override
	public void accept(WatchedEvent event) {
		if(event.getType().compareTo(EventType.NodeChildrenChanged)==0) {
	        try {
	        	String znodeKey = event.getPath();
				String data = new String(client.getData().forPath(event.getPath()));
				configurationMap.put(znodeKey, data);
				AsyncCuratorFramework async= AsyncCuratorFramework.wrap(client);
		        async.watched().getChildren().forPath(znodeKey).event().thenAccept(this);
		        log.debug("Key Path ["+event.getPath()+"] - New Configuration Data ["+data+"]");
	        } catch (Exception e) {
	        	log.error(e);
			}
		}
	}
}
