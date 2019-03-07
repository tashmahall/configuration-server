package com.zookeeper_utils.configuration_server.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;

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
public class ZookeeperServicePropertiesApplicationScopedPropertiesNotNullTest {
	
	@Spy
	@InjectMocks
	private ZookeeperServicePropertiesApplicationScoped sbv;
	
	@Mock
	private ZookeeperRepositoryInterface zc;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    @Mock
    private ServletContext context;
    
    @Mock
    private Map<String,String> properties;
	

	
	@Before
	public void loadConfigurationMap() {
		String $zookeeper$first2 = "/zookeeper/first2";
		when(properties.get($zookeeper$first2)).thenReturn("test /zookeeper/first2");
		when(context.getServletContextName()).thenReturn("zookeeper");
	}
	
	@Test
	public void getPropertyValueTestProperitiesMapNotNull() throws ConfigPropertiesException {
		String teste = sbv.getPropertyValue("/first2");
		assertEquals("test /zookeeper/first2",teste);
	}
	@Test
	public void getPropertiesMapTestProperitiesMapNotNull() throws ConfigPropertiesException {
		Map<String,String> teste = sbv.getPropertiesMap();
		assertEquals(properties,teste);
	}
}

