package com.zookeeper_utils.configuration_server.repositories.watchers;

import java.util.Map;
import java.util.function.Consumer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;

public class ConfigurationEventWatcher implements Consumer<WatchedEvent> {
	private final Logger log = Logger.getLogger(this.getClass());
	private CuratorFramework client;
	private Map<String,String> configurationMap;
	public ConfigurationEventWatcher(CuratorFramework client, Map<String, String> configurationMap) {
		this.client = client;
		this.configurationMap = configurationMap;
	}
	
	@Override
	public void accept(WatchedEvent event) {
		if(event.getType().compareTo(EventType.NodeDataChanged)==0||event.getType().compareTo(EventType.NodeCreated)==0) {
			String znodeKey=null;
			try {
				znodeKey = event.getPath();
				String data = new String(client.getData().forPath(event.getPath()));
				configurationMap.put(znodeKey, data);
				AsyncCuratorFramework async= AsyncCuratorFramework.wrap(client);
		        async.watched().checkExists().forPath(znodeKey).event().thenAccept(this);
	        } catch (Exception e) {
	        	log.error("Got error "+e.getMessage()+" while creating the event whatcher to the path ["+znodeKey+"]",e);
			}
		}
		if(event.getType().compareTo(EventType.NodeDeleted)==0) {
        	String znodeKey = event.getPath();
			configurationMap.remove(znodeKey);
			AsyncCuratorFramework async= AsyncCuratorFramework.wrap(client);
	        async.watched().getData().forPath(znodeKey).event().thenAccept(this);
		}
	}
}
