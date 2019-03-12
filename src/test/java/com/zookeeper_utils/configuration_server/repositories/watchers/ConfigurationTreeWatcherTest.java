package com.zookeeper_utils.configuration_server.repositories.watchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.AsyncStage;
import org.apache.curator.x.async.api.AsyncGetChildrenBuilder;
import org.apache.curator.x.async.api.WatchableAsyncCuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.zookeeper_utils.configuration_server.repositories.watchers.ConfigurationTreeWatcher;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value= {CuratorFrameworkFactory.class,AsyncCuratorFramework.class})
public class ConfigurationTreeWatcherTest {
	@InjectMocks
	private ConfigurationTreeWatcher sbv;
	private ConfigurationTreeWatcher sbv2;
	
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
	
	@Mock
	AsyncCuratorFramework async;
	
	@Mock
	private WatchableAsyncCuratorFramework wacf;
	@Mock
	private CompletionStage<WatchedEvent> csWatched;
	@Mock
	private CompletionStage<Void> csVoid;
	@Mock
	private AsyncGetChildrenBuilder agcb;
	@Mock
	private AsyncStage<List<String>> asList;
	@Before
	public void load() {
		MockitoAnnotations.initMocks(this);
		sbv2 = spy(sbv);
		mockStatic(CuratorFrameworkFactory.class);
		mockStatic(AsyncCuratorFramework.class);
		when(AsyncCuratorFramework.wrap(client)).thenReturn(async);
		when(async.watched()).thenReturn(wacf);
		when(wacf.getChildren()).thenReturn(agcb);
		when(agcb.forPath(anyString())).thenReturn(asList);
		when(asList.event()).thenReturn(csWatched);
		when(csWatched.thenAccept(sbv)).thenReturn(csVoid);
	}
	
	@Test
	public void testAccept() throws Exception {
		String keyPath="/zookeeper/first1";
		String keyPathValue = "/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeChildrenChanged);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenReturn(keyPathValue.getBytes());
		when(configurationMap.put(keyPath,keyPathValue)).thenReturn(null);
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
	@Test
	public void testAcceptEventDiffOfNodeChildrenChanged() throws Exception {
		when(event.getType()).thenReturn(EventType.NodeCreated);
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
	@Test
	public void testAcceptException() throws Exception {
		String keyPath="/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeChildrenChanged);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenThrow(new Exception("Some message"));
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}

}
