package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
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
@PrepareForTest(value= {CuratorFrameworkFactory.class,MapUtils.class,StringUtils.class})
public class ZookeeperRepositoryWithWhatcherMapNotNullTest {
	@InjectMocks
	private ZookeeperRepositoryWithWhatcher sbv;

	@Mock
	private ZookeeperConfigurationLoader zcl;
	@Mock
	private ZookeeperKeyPathGenerator zkpg;
	@Mock
	private Map<String,String> configurationMapAppContext ;
	@Mock
	private Map<String,String> configurationMapGlobalContext ;
	@Mock
	private CuratorFramework clientZookeeper;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void loadConfigurationMap() throws NamingException, ConfigPropertiesException {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		MockitoAnnotations.initMocks(this);
		mockStatic(MapUtils.class);
		mockStatic(StringUtils.class);
		mockStatic(CuratorFrameworkFactory.class);
		when(CuratorFrameworkFactory.newClient(eq(connectioZookeeper),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		when(MapUtils.isEmpty(configurationMapGlobalContext)).thenReturn(false);
		
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		
	}
	@Test
	public void testGetKeyPathTreeAppContextCuratorStopped() throws Exception {
		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
		String $zookeeper = "/zookeeper";
		MockitoAnnotations.initMocks(this);
		when(zcl.getContextName()).thenReturn($zookeeper);
		when(MapUtils.isEmpty(configurationMapAppContext)).thenReturn(true);
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STOPPED);
		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
		when(CuratorFrameworkFactory.newClient(eq(connectioZookeeper),any(RetryPolicy.class))).thenReturn(clientZookeeper);
		when(zkpg.getKeyPathTree($zookeeper, clientZookeeper)).thenReturn(configurationMapAppContext);
		Map<String,String> test =sbv.getKeyPathTreeApplicationContext();
		assertEquals(configurationMapAppContext, test);
	}
	@Test
	public void testGetKeyPathTreeAppContextCuratorStarted() throws Exception {
		
		String $zookeeper = "/zookeeper";
		MockitoAnnotations.initMocks(this);
		when(zcl.getContextName()).thenReturn($zookeeper);
		when(MapUtils.isEmpty(configurationMapAppContext)).thenReturn(false);
		when(clientZookeeper.getState()).thenReturn(CuratorFrameworkState.STARTED);
		when(zkpg.getKeyPathTree($zookeeper, clientZookeeper)).thenReturn(configurationMapAppContext);
		Map<String,String> test =sbv.getKeyPathTreeApplicationContext();
		assertEquals(configurationMapAppContext, test);
	}
//	@Test
//	public void testGetKeyPathTreeAppContextMapDoesNotHave() throws Exception {
//		
//		String $zookeeper = "/zookeeper";
//		MockitoAnnotations.initMocks(this);
//		when(zcl.getContextName()).thenReturn($zookeeper);
//		when(MapUtils.isEmpty(configurationMapAppContext)).thenReturn(false);
//		when(zkpg.getKeyPathTree($zookeeper, clientZookeeper)).thenReturn(configurationMapAppContext);
//		Map<String,String> test =sbv.getKeyPathTreeApplicationContext();
//		assertEquals(configurationMapAppContext, test);
//	}
//	@Test
//	public void getValueFromKeyPathAppContext() throws Exception {
//		String $ans = "/zookeeper";
//		MockitoAnnotations.initMocks(this);
//		when(zcl.getContextName()).thenReturn($ans);
//		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(configurationMapAppContext);
//		String test =sbv.getValueFromKeyPathApplicationContext("/first2");
//		assertEquals(configurationMapAppContext.get("/zookeeper/first2"), test);
//		
//	}	
//	@Test(expected=ConfigPropertiesException.class)
//	public void getValueFromKeyPathInexist() throws Exception {
//		String $ans = "/zookeeper";
//		when(zcl.getContextName()).thenReturn($ans);
//		//Final LoadClientZookeeper
//		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(configurationMapAppContext);
//		sbv.getValueFromKeyPathApplicationContext("/first3");
//	}
//	@Test
//	public void testGetKeyPathTreeGlobalContext() throws Exception {
//		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
//		String $ans = "/ans";
//		MockitoAnnotations.initMocks(this);
//		//LoadClienteZookeeper
//		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
//		mockStatic(CuratorFrameworkFactory.class);
//		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
//		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
//		//Final LoadClientZookeeper
//		when(zcl.getGlobalContextName()).thenReturn($ans);
//		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(configurationMapGlobalContext);
//		Map<String,String> test =sbv.getKeyPathTreeGlobalContext();
//		assertEquals(configurationMapGlobalContext, test);
//	}
//
//	@Test
//	public void getValueFromKeyPathGlobalContext() throws Exception {
//		String connectioZookeeper="anstsagatha.ans.gov.br:2181";
//		String $ans = "/ans";
//		MockitoAnnotations.initMocks(this);
//		//LoadClienteZookeeper
//		CuratorFramework clientZookeeper = mock(CuratorFramework.class);
//		mockStatic(CuratorFrameworkFactory.class);
//		when(zcl.loadHostAndPort()).thenReturn(connectioZookeeper);
//		when(CuratorFrameworkFactory.newClient(anyString(),any(RetryPolicy.class))).thenReturn(clientZookeeper);
//		when(zcl.getGlobalContextName()).thenReturn($ans);
//		//Final LoadClientZookeeper
//		when(zkpg.getKeyPathTree($ans, clientZookeeper)).thenReturn(configurationMapGlobalContext);
//		String test =sbv.getValueFromKeyPathGlobalContext("/first2");
//		assertEquals(configurationMapGlobalContext.get("/ans/first2"), test);
//		
//	}	

}
