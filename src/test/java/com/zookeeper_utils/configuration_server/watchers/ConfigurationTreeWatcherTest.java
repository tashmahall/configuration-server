package com.zookeeper_utils.configuration_server.watchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationTreeWatcherTest {
	@Spy
	@InjectMocks
	private ConfigurationTreeWatcher sbv;
	
	@Mock
	private CuratorFramework client;

	@Mock
	private WatchedEvent event;
	
	@Mock
	private GetChildrenBuilder getChildrenBuilder;
	
	@Mock
	private GetDataBuilder getDataBuilder;
	
	@Mock
	private Map<String,String> configurationMap;
	
	
	@Before
	public void load() {}
	
	@Test
	public void testAccept() throws Exception {
		String keyPath="/zookeeper/first1";
		String keyPathValue = "/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeChildrenChanged);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenReturn(keyPathValue.getBytes());
		when(configurationMap.put(keyPath,keyPathValue)).thenReturn(null);
		sbv.accept(event);
		verify(sbv,times(1)).accept(event);
	}
	@Test
	public void testAcceptEventDiffOfNodeChildrenChanged() throws Exception {
		when(event.getType()).thenReturn(EventType.NodeCreated);
		sbv.accept(event);
		verify(sbv,times(1)).accept(event);
	}
	@Test
	public void testAcceptException() throws Exception {
		String keyPath="/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeChildrenChanged);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenThrow(new Exception("Some message"));
		sbv.accept(event);
		verify(sbv,times(1)).accept(event);
	}

}
