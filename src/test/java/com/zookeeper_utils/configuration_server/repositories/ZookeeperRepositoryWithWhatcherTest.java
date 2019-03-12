package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletionStage;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.AsyncStage;
import org.apache.curator.x.async.api.AsyncExistsBuilder;
import org.apache.curator.x.async.api.AsyncGetChildrenBuilder;
import org.apache.curator.x.async.api.WatchableAsyncCuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperConfigurationLoader;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryWithWhatcher;
import com.zookeeper_utils.configuration_server.repositories.watchers.ConfigurationEventWatcher;
import com.zookeeper_utils.configuration_server.repositories.watchers.ConfigurationTreeWatcher;


@RunWith(PowerMockRunner.class)
@PrepareForTest(value= {CuratorFrameworkFactory.class,AsyncCuratorFramework.class})
public class ZookeeperRepositoryWithWhatcherTest {
	@InjectMocks
	private ZookeeperRepositoryWithWhatcher sbv;
	@Mock
	private CuratorFramework clientZookeeper;
	@Mock
	private ServletContext context;
	@Mock
	private GetChildrenBuilder getChildrenBuilder;
	@Mock
	private GetDataBuilder getDataBuilder;
	@Mock
	private InitialContext initiaContext;
	@Mock
	private AsyncCuratorFramework async;
	@Mock
	private	ZookeeperConfigurationLoader zcl;
	@Mock
	private WatchableAsyncCuratorFramework wacf;
	@Mock
	private AsyncExistsBuilder aeb;
	@Mock
	private AsyncStage<Stat> as;
	@Mock
	private CompletionStage<WatchedEvent> csWatched;
	@Mock
	private CompletionStage<Void> csVoid;
	@Mock
	private AsyncGetChildrenBuilder agcb;	
	@Mock
	private AsyncStage<List<String>> asList;
	
	
	private Map<String,String> configurationMap ;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void loadConfigurationMap() throws NamingException, ConfigPropertiesException {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		configurationMap = new TreeMap<String,String>();
		configurationMap.put("/zookeeper/first1", null);
		configurationMap.put("/zookeeper/first2","test /zookeeper/first2");
		configurationMap.put("/zookeeper/first1/second1","test /zookeeper/first1/second1");
		MockitoAnnotations.initMocks(this);
		mockStatic(CuratorFrameworkFactory.class);
		mockStatic(AsyncCuratorFramework.class);
		when(CuratorFrameworkFactory.newClient(eq(connectioZookeeper),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		when(AsyncCuratorFramework.wrap(clientZookeeper)).thenReturn(async);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(async.watched()).thenReturn(wacf);
		when(wacf.checkExists()).thenReturn(aeb);
		when(aeb.forPath(anyString())).thenReturn(as);
		when(as.event()).thenReturn(csWatched);
		when(csWatched.thenAccept(any(ConfigurationEventWatcher.class))).thenReturn(csVoid);
		when(wacf.getChildren()).thenReturn(agcb);
		when(agcb.forPath(anyString())).thenReturn(asList);
		when(asList.event()).thenReturn(csWatched);
		this.async.watched().getChildren().forPath("").event().thenAccept(new ConfigurationTreeWatcher(this.clientZookeeper,configurationMap));

	}
	@Test
	public void testGetKeyPathTree() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first2= $zookeeper+"/"+"first2";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		byte[] test$zookeeper$f2= "test /zookeeper/first2".getBytes();
		byte[] test$zookeeper$f1$s1= "test /zookeeper/first1/second1".getBytes();
		List<String> list1 = Arrays.asList("first1","first2");
		List<String> list2 = Arrays.asList("second1"); 
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(list2);
		when(clientZookeeper.getChildren().forPath($zookeeper$first2)).thenReturn(null);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1$second1)).thenReturn(null);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(null);
		when(clientZookeeper.getData().forPath($zookeeper$first2)).thenReturn(test$zookeeper$f2);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenReturn(test$zookeeper$f1$s1);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap, test);
	}

	@Test
	public void testGetKeyPathTreeListIsEmpty() throws Exception {
		Map<String,String>configurationMap2 = new TreeMap<String,String>();
		configurationMap2.put("/zookeeper/first1", null);
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1");
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(new ArrayList<String>());
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(null);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap2, test);
	}	
	@Test
	public void testDefineWatcherByteArrayLessThanOne() throws Exception {
		Map<String,String>configurationMap2 = new TreeMap<String,String>();
		configurationMap2.put("/zookeeper/first1", null);
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1");
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(new ArrayList<String>());
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(new byte[0]);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap2, test);
	}
	@Test
	public void testExceptionTreeGeneratorWatcherGetKeyPathTree() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while getting children properties to the 'keyPath' ["+$zookeeper+"]");
		sbv.getKeyPathTree();
	}
	
	@Test
	public void testExceptionDefineWatcherGetKeyPathTree() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1","first2");
		List<String> list2 = Arrays.asList("second1"); 
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(list2);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while load the properties to the 'keyPath' ["+$zookeeper$first1+"]");
		sbv.getKeyPathTree();
	}
	
	@Test
	public void testGetValueFromKeyPath() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first2= $zookeeper+"/"+"first2";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		byte[] test$zookeeper$f2= "test /zookeeper/first2".getBytes();
		byte[] test$zookeeper$f1$s1= "test /zookeeper/first1/second1".getBytes();
		List<String> list1 = Arrays.asList("first1","first2");
		List<String> list2 = Arrays.asList("second1"); 
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(list2);
		when(clientZookeeper.getChildren().forPath($zookeeper$first2)).thenReturn(null);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1$second1)).thenReturn(null);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(null);
		when(clientZookeeper.getData().forPath($zookeeper$first2)).thenReturn(test$zookeeper$f2);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenReturn(test$zookeeper$f1$s1);
		String test =sbv.getValueFromKeyPath("/first1/second1");
		assertEquals(configurationMap.get($zookeeper$first1$second1), test);
	}

}
