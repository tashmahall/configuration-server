package com.zookeeper_utils.configuration_server.repositories;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Arrays;
import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
@RunWith(PowerMockRunner.class)
@PrepareForTest(value= {AsyncCuratorFramework.class})
public class ZookeeperKeyPathTreeGeneratorWithWatcherTest2 {
	@InjectMocks
	private ZookeeperKeyPathTreeGeneratorWithWatcher sbv;
	@Mock
	private GetChildrenBuilder getChildrenBuilder;
	@Mock
	private AsyncCuratorFramework async;

	
	
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Test(expected = ConfigPropertiesException.class) 
	public void tesTreeGeneratorExceptionNewCtx() throws Exception  {
		MockitoAnnotations.initMocks(this);
		mockStatic(AsyncCuratorFramework.class);
		String $zookeeper = "/zookeeper";
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1","first2");
		CuratorFramework clientZookeeper = Mockito.mock(CuratorFramework.class);
		when(AsyncCuratorFramework.wrap(clientZookeeper)).thenReturn(async);
		doNothing().when(clientZookeeper).start();
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STARTED);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenThrow(Exception.class);
        sbv.getKeyPathTree($zookeeper,clientZookeeper);
	}
	@Test(expected = ConfigPropertiesException.class) 
	public void tesTreeGeneratorExceptionCtx() throws Exception  {
		MockitoAnnotations.initMocks(this);
		mockStatic(AsyncCuratorFramework.class);
		String $zookeeper = "/zookeeper";
		CuratorFramework clientZookeeper = Mockito.mock(CuratorFramework.class);
		when(AsyncCuratorFramework.wrap(clientZookeeper)).thenReturn(async);
		doNothing().when(clientZookeeper).start();
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STARTED);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenThrow(Exception.class);
        sbv.getKeyPathTree($zookeeper,clientZookeeper);
	}
	@Test(expected = ConfigPropertiesException.class) 
	public void tesTreeGeneratorExceptionClientZookeeperNull() throws Exception  {
		MockitoAnnotations.initMocks(this);
		mockStatic(AsyncCuratorFramework.class);
		String $zookeeper = "/zookeeper";
		CuratorFramework clientZookeeper=null;
        sbv.getKeyPathTree($zookeeper,clientZookeeper);
	}
}
