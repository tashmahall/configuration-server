package com.zookeeper_utils.configuration_server.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertiesRequestScoped;

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperServicePropertiesRequestScopedTest {
	
	@Spy
	@InjectMocks
	private ZookeeperServicePropertiesRequestScoped sbv;
	
	@Mock
	private ZookeeperRepositoryInterface zc;


	
	private Map<String,String> mapGlobal ;
	
	private Map<String,String> mapApp ;

	
	@Before
	public void loadConfigurationMap() {
		mapApp = new TreeMap<String,String>();
		mapApp.put("/zookeeper/first2","test /zookeeper/first2");
		mapGlobal = new TreeMap<String,String>();
		mapGlobal.put("/ans/first2","test /ans/first2");
	}
	
	@Test
	public void getPropertyValueTestIsGlobalTrue() throws ConfigPropertiesException {
		when(zc.getValueFromKeyPathGlobalContext(eq("/first2"))).thenReturn("test /ans/first2");
		String teste = sbv.getPropertyValue("/first2",true);
		assertEquals("test /ans/first2",teste);
	}
	@Test
	public void getPropertyValueTestIsGlobalFalse() throws ConfigPropertiesException {
		when(zc.getValueFromKeyPathApplicationContext(eq("/first2"))).thenReturn("test /zookeeper/first2");
		String teste = sbv.getPropertyValue("/first2",false);
		assertEquals("test /zookeeper/first2",teste);
	}
	@Test
	public void getPropertiesMapTestIsGlobalTrue() throws ConfigPropertiesException {
		when(zc.getKeyPathTreeGlobalContext()).thenReturn(mapGlobal);
		Map<String,String> teste = sbv.getPropertiesMap(true);
		assertEquals(mapGlobal,teste);
	}
	@Test
	public void getPropertiesMapTestIsGlobalFalse() throws ConfigPropertiesException {
		when(zc.getKeyPathTreeApplicationContext()).thenReturn(mapApp);
		Map<String,String> teste = sbv.getPropertiesMap(false);
		assertEquals(mapApp,teste);
	}
}

