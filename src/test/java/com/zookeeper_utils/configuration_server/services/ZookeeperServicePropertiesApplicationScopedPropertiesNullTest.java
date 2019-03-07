package com.zookeeper_utils.configuration_server.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

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

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperServicePropertiesApplicationScopedPropertiesNullTest {
	
	@Spy
	@InjectMocks
	private ZookeeperServicePropertiesApplicationScoped sbv;
	
	@Mock
	private ZookeeperRepositoryInterface zc;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Mock
    private ServletContext context;
    

	
	private Map<String,String> configurationMap ;

	
	@Before
	public void loadConfigurationMap() {
		configurationMap = new TreeMap<String,String>();
		configurationMap.put("/zookeeper/first1", null);
		configurationMap.put("/zookeeper/first2","test /zookeeper/first2");
		configurationMap.put("/zookeeper/first1/second1","test /zookeeper/first1/second1");
		when(context.getServletContextName()).thenReturn("zookeeper");
	}
	@Test
	public void getPropertyValueTestProperitiesMapNull() throws ConfigPropertiesException {
		when(zc.getKeyPathTree()).thenReturn(configurationMap);
		String teste = sbv.getPropertyValue("/first2");
		assertEquals("test /zookeeper/first2",teste);
	}
	@Test
	public void getPropertiesMapTest() throws ConfigPropertiesException {
		
		when(zc.getKeyPathTree()).thenReturn(configurationMap);
		Map<String,String> teste = sbv.getPropertiesMap();
		assertEquals(configurationMap,teste);
	}
	@Test
	public void getUpdateAllConfigurationTreeTest() throws ConfigPropertiesException {
		when(zc.getKeyPathTree()).thenReturn(configurationMap);
		Map<String,String> teste = sbv.updateAllConfigurationTree();
		assertEquals(configurationMap,teste);
	}
}

