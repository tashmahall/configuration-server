package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.imps.CuratorFrameworkState;
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


@RunWith(PowerMockRunner.class)
@PrepareForTest(CuratorFrameworkFactory.class)
public class ZookeeperRepositoryWithoutWatcherTest {
	@InjectMocks
	private ZookeeperRepositoryWithoutWatcher sbv;
	@Mock
	private CuratorFramework clientZookeeper;
	@Mock
	private ServletContext context;
	@Mock
	private GetChildrenBuilder getChildrenBuilder;
	@Mock
	private GetDataBuilder getDataBuilder;
	@Mock
	private ZookeeperKeyPathGenerator zri;
	@Mock
	private	ZookeeperConfigurationLoader zcl;
	private Map<String,String> appnMap ;
	private Map<String,String> globalMap ;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void loadConfigurationMap() throws NamingException, ConfigPropertiesException {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		globalMap = new TreeMap<String, String>();
		globalMap.put("/ans/first2","test /ans/first2");
		MockitoAnnotations.initMocks(this);
		appnMap = new TreeMap<String,String>();
		appnMap.put("/zookeeper/first1", null);
		appnMap.put("/zookeeper/first2","test /zookeeper/first2");
		appnMap.put("/zookeeper/first1/second1","test /zookeeper/first1/second1");
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		mockStatic(CuratorFrameworkFactory.class);
		when(CuratorFrameworkFactory.newClient(eq(connectioZookeeper),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		
	}
	@Test
	public void testGetKeyPathTree() throws Exception {
		String $zookeeper = "/zookeeper";
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(zcl.getContextName()).thenReturn($zookeeper);
		when(zri.getKeyPathTree($zookeeper, clientZookeeper)).thenReturn(appnMap);
		when(context.getServletContextName()).thenReturn("zookeeper");
		Map<String,String> test =sbv.getKeyPathTreeApplicationContext();
		assertEquals(appnMap, test);
	}

	@Test
	public void testGetValueFromKeyPath() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		byte[] test$zookeeper$f1$s1= "test /zookeeper/first1/second1".getBytes();
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(zcl.getContextName()).thenReturn($zookeeper);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenReturn(test$zookeeper$f1$s1);
		String test =sbv.getValueFromKeyPathApplicationContext("/first1/second1");
		assertEquals(appnMap.get($zookeeper$first1$second1), test);
	}
	@Test
	public void testGetValueFromKeyPathException() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while load the properties to the 'keyPath' [/first1/second1]");
        sbv.getValueFromKeyPathApplicationContext("/first1/second1");
	}
	@Test
	public void testGetKeyPathTreeGlobalContext() throws Exception {
		String $ans = "/ans";
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(zcl.getGlobalContextName()).thenReturn($ans);
		when(zri.getKeyPathTree($ans, clientZookeeper)).thenReturn(globalMap);
        Map<String,String> test = sbv.getKeyPathTreeGlobalContext();
        assertEquals(globalMap, test);
	}
	@Test
	public void testGetValueFromKeyPathGlobalContext() throws Exception {
		String $ans = "/ans";
		byte[] test$ans$f2= "test /ans/first2".getBytes();
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(zcl.getGlobalContextName()).thenReturn($ans);
		when(zri.getKeyPathTree($ans, clientZookeeper)).thenReturn(globalMap);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath(eq("/ans/first2"))).thenReturn(test$ans$f2);
        String test = sbv.getValueFromKeyPathGlobalContext("/first2");
        assertEquals("test /ans/first2", test);
	}
}
