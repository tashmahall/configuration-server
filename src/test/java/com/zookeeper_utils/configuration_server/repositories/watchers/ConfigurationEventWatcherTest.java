package com.zookeeper_utils.configuration_server.repositories.watchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Map;
import java.util.concurrent.CompletionStage;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.AsyncStage;
import org.apache.curator.x.async.api.AsyncExistsBuilder;
import org.apache.curator.x.async.api.AsyncGetDataBuilder;
import org.apache.curator.x.async.api.WatchableAsyncCuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.zookeeper_utils.configuration_server.repositories.watchers.ConfigurationEventWatcher;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value= {CuratorFrameworkFactory.class,AsyncCuratorFramework.class})
public class ConfigurationEventWatcherTest {
	@InjectMocks
	private ConfigurationEventWatcher sbv;
	
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
	private AsyncExistsBuilder aeb;
	@Mock
	private AsyncStage<Stat> as;
	@Mock
	private AsyncStage<byte[]> asByte;
	@Mock
	private CompletionStage<WatchedEvent> csWatched;
	@Mock
	private CompletionStage<Void> csVoid;
	@Mock
	private AsyncGetDataBuilder agdb;
	
	@Before
	public void load() {
		MockitoAnnotations.initMocks(this);
		mockStatic(CuratorFrameworkFactory.class);
		mockStatic(AsyncCuratorFramework.class);
		when(AsyncCuratorFramework.wrap(client)).thenReturn(async);
		when(async.watched()).thenReturn(wacf);
		when(wacf.checkExists()).thenReturn(aeb);
		when(aeb.forPath(anyString())).thenReturn(as);
		when(as.event()).thenReturn(csWatched);
		when(csWatched.thenAccept(sbv)).thenReturn(csVoid);
	}
	@Test
	public void testAcceptNodeDataChanged() throws Exception {
		ConfigurationEventWatcher sbv2 = spy(sbv);
		String keyPath="/zookeeper/first1";
		String keyPathValue = "/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeDataChanged);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenReturn(keyPathValue.getBytes());
		when(configurationMap.put(keyPath,keyPathValue)).thenReturn(null);
		
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
	@Test
	public void testAcceptNodeCreated() throws Exception {
		ConfigurationEventWatcher sbv2 = spy(sbv);
		String keyPath="/zookeeper/first1";
		String keyPathValue = "/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeCreated);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenReturn(keyPathValue.getBytes());
		when(configurationMap.put(keyPath,keyPathValue)).thenReturn(null);
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
	@Test
	public void testAcceptException() throws Exception {
		ConfigurationEventWatcher sbv2 = spy(sbv);
		String keyPath="/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeCreated);
		when(event.getPath()).thenReturn(keyPath);
		when(client.getData()).thenReturn(getDataBuilder);
		when(getDataBuilder.forPath(keyPath)).thenThrow(new Exception("Some message"));
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
	@Test
	public void testAcceptEventDiff() throws Exception {
		ConfigurationEventWatcher sbv2 = spy(sbv);
		when(event.getType()).thenReturn(EventType.ChildWatchRemoved);
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
	@Test
	public void testAcceptNodeDeleted() throws Exception {
		ConfigurationEventWatcher sbv2 = spy(sbv);
		String keyPath="/zookeeper/first1";
		when(event.getType()).thenReturn(EventType.NodeDeleted);
		when(event.getPath()).thenReturn(keyPath);
		when(configurationMap.remove(keyPath)).thenReturn(null);
		
		when(async.watched()).thenReturn(wacf);
		when(wacf.getData()).thenReturn(agdb);
		when(agdb.forPath(anyString())).thenReturn(asByte);
		when(asByte.event()).thenReturn(csWatched);
		when(csWatched.thenAccept(sbv2)).thenReturn(csVoid);
		sbv2.accept(event);
		verify(sbv2,times(1)).accept(event);
	}
}
