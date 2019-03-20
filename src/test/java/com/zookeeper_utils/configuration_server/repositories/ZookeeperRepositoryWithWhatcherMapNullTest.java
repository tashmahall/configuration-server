package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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
@PrepareForTest(value= {CuratorFrameworkFactory.class})
public class ZookeeperRepositoryWithWhatcherMapNullTest {
	@InjectMocks
	private ZookeeperRepositoryWithWhatcher sbv;

	@Mock
	private ZookeeperConfigurationLoader zcl;
	@Mock
	private ZookeeperKeyPathGenerator zkpg;
	
	private Map<String,String> appContextMap ;
	private Map<String,String> globalContextMap ;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void loadConfigurationMap() throws NamingException, ConfigPropertiesException {
		appContextMap = new TreeMap<String,String>();
		appContextMap.put("/zookeeper/first2","test /zookeeper/first2");
		globalContextMap = new TreeMap<String,String>();
		globalContextMap.put("/ans/first2","test /ans/first2");

	}
	@Test
	public void testGetKeyPathTreeAppContext() throws Exception {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		String $zookeeper = "/zookeeper";
		MockitoAnnotations.initMocks(this);
		//LoadClienteZookeeper
		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
		mockStatic(CuratorFrameworkFactory.class);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		//Final LoadClientZookeeper
		when(zcl.getContextName()).thenReturn($zookeeper);
		when(zkpg.getKeyPathTree($zookeeper, clientZookeeper)).thenReturn(appContextMap);
		Map<String,String> test =sbv.getKeyPathTreeApplicationContext();
		assertEquals(appContextMap, test);
	}

	@Test
	public void getValueFromKeyPathAppContext() throws Exception {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		String $ans = "/zookeeper";
		MockitoAnnotations.initMocks(this);
		//LoadClienteZookeeper
		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
		mockStatic(CuratorFrameworkFactory.class);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		when(zcl.getContextName()).thenReturn($ans);
		//Final LoadClientZookeeper
		
		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(appContextMap);
		String test =sbv.getValueFromKeyPathApplicationContext("/first2");
		assertEquals(appContextMap.get("/zookeeper/first2"), test);
		
	}	
	@Test(expected=ConfigPropertiesException.class)
	public void getValueFromKeyPathInexist() throws Exception {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		String $ans = "/zookeeper";
		MockitoAnnotations.initMocks(this);
		//LoadClienteZookeeper
		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
		mockStatic(CuratorFrameworkFactory.class);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		when(zcl.getContextName()).thenReturn($ans);
		//Final LoadClientZookeeper
		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(appContextMap);
		sbv.getValueFromKeyPathApplicationContext("/first3");
	}
	@Test
	public void testGetKeyPathTreeGlobalContext() throws Exception {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		String $ans = "/ans";
		MockitoAnnotations.initMocks(this);
		//LoadClienteZookeeper
		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
		mockStatic(CuratorFrameworkFactory.class);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		//Final LoadClientZookeeper
		when(zcl.getGlobalContextName()).thenReturn($ans);
		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(globalContextMap);
		Map<String,String> test =sbv.getKeyPathTreeGlobalContext();
		assertEquals(globalContextMap, test);
	}

	@Test
	public void getValueFromKeyPathGlobalContext() throws Exception {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		String $ans = "/ans";
		MockitoAnnotations.initMocks(this);
		//LoadClienteZookeeper
		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
		mockStatic(CuratorFrameworkFactory.class);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		when(zcl.getGlobalContextName()).thenReturn($ans);
		//Final LoadClientZookeeper
		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(globalContextMap);
		String test =sbv.getValueFromKeyPathGlobalContext("/first2");
		assertEquals(globalContextMap.get("/ans/first2"), test);
		
	}	

}
