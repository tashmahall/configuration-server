package com.zookeeper_utils.configuration_server.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.service.ZookeeperServicePropertiesApplicationScoped;

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperServicePropertiesApplicationScopedPropertiesTest {
	
	@Spy
	@InjectMocks
	private ZookeeperServicePropertiesApplicationScoped sbv;
	
	@Mock
	private ZookeeperRepositoryInterface zc;

    @Mock
    private ServletContext context;
    
    private Map<String,String> globalMap;
    private Map<String,String> appMap;
    
    
    
	
	@Before
	public void loadConfigurationMap() {
		globalMap = new TreeMap<String, String>();
		appMap = new TreeMap<String, String>();
		globalMap.put("/ans/first2", "test /ans/frist2");
		appMap.put("/zookeeper/first2", "test /zookeeper/frist2");
	}
	
	@Test
	public void getPropertyValueIsGlobalTrue() throws ConfigPropertiesException {
		String $ansr$first2 = "/ans/first2";
		String value$ans$first2 = "test /ans/first2";
		when(zc.getValueFromKeyPathGlobalContext($ansr$first2)).thenReturn(value$ans$first2);
		String test = sbv.getPropertyValue($ansr$first2, true);
		assertEquals(value$ans$first2,test);
	}
	@Test
	public void getPropertyValueIsGlobalFalse() throws ConfigPropertiesException {
		String $zookeeper$first2 = "/zookeeper/first2";
		String value$zookeeper$first2 = "test /zookeeper/first2";
		when(zc.getValueFromKeyPathApplicationContext($zookeeper$first2)).thenReturn(value$zookeeper$first2);
		String test = sbv.getPropertyValue($zookeeper$first2, false);
		assertEquals(value$zookeeper$first2,test);
	}
	@Test
	public void getPropertiesMapIsGlobalTrue() throws ConfigPropertiesException {
		when(zc.getKeyPathTreeGlobalContext()).thenReturn(globalMap);
		Map<String,String> mapTest = sbv.getPropertiesMap(true);
		assertEquals(globalMap,mapTest);
	}
	@Test
	public void getPropertiesMapIsGlobalFalse() throws ConfigPropertiesException {
		when(zc.getKeyPathTreeApplicationContext()).thenReturn(appMap);
		Map<String,String> mapTest = sbv.getPropertiesMap(false);
		assertEquals(appMap,mapTest);
	}
}

