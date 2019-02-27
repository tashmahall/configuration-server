package com.zookeeper_utils.configuration_server.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesGlobalRequestScoped;

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperServicePropertiesGlobalRequestScopedTest {
	
	@Spy
	@InjectMocks
	private ZookeeperServicePropertiesGlobalRequestScoped sbv;
	
	@Mock
	private ZookeeperRepositoryInterface zc;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	private Map<String,String> configurationMap ;

	
	@Before
	public void loadConfigurationMap() {
		configurationMap = new TreeMap<String,String>();
		configurationMap.put("/zookeeper/first1", null);
		configurationMap.put("/zookeeper/first2","test /zookeeper/first2");
		configurationMap.put("/zookeeper/first1/second1","test /zookeeper/first1/second1");
	}
	
	@Test
	public void getPropertyValueTest() throws ConfigPropertiesException {
		when(zc.getValueFromKeyPath("/first2")).thenReturn("test /zookeeper/first2");
		String teste = sbv.getPropertyValue("/first2");
		assertEquals("test /zookeeper/first2",teste);
	}
	@Test
	public void getPropertiesMapTest() throws ConfigPropertiesException {
		when(zc.getKeyPathTree()).thenReturn(configurationMap);
		Map<String,String> teste = sbv.getPropertiesMap();
		assertEquals(configurationMap,teste);
	}
}

