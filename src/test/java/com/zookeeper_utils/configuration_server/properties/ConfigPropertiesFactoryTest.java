package com.zookeeper_utils.configuration_server.properties;

import static com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType.APPLICATION_SCOPED_WITH_WATCHER;
import static com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType.GLOBAL_CONTEXT_NO_WATCHER;
import static com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;

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
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperties;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesInterface;
@RunWith(MockitoJUnitRunner.class)
public class ConfigPropertiesFactoryTest {
	@Spy
	@InjectMocks
	private ConfigPropertiesFactory sbv;
	
	@Mock
	private InjectionPoint injectionPoint;
	
	@Mock
	private Annotated annotated;
	
	@Mock
	private ConfigProperties configProperties;
	
	@Mock
	private ZookeeperServicePropertiesInterface zkServiceAppScoped;
	
	@Mock
	private ZookeeperServicePropertiesInterface zkServiceReqScoped;
	
	@Mock
	private ZookeeperServicePropertiesInterface zkServiceGlobalReqScoped;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();

	private String keyPath;
	private String value ;
	
	@Before
	public void loadData() {
		keyPath = "/first1";
		value = "test /zookeeper/first1";
	}
	
	@Test
	public void testProduceGlobalContext() throws ConfigPropertiesException {
		when(injectionPoint.getAnnotated()).thenReturn(annotated);
		when(annotated.getAnnotation(ConfigProperties.class)).thenReturn(configProperties);
		when(configProperties.keyPath()).thenReturn(keyPath);
		when(configProperties.configPropertyType()).thenReturn(GLOBAL_CONTEXT_NO_WATCHER);
		when(zkServiceGlobalReqScoped.getPropertyValue(keyPath)).thenReturn(value);
		String test = sbv.produce(injectionPoint);
		assertEquals(value,test);
		
	}
	@Test
	public void testProduceReqScopedNoWatcher() throws ConfigPropertiesException {
		when(injectionPoint.getAnnotated()).thenReturn(annotated);
		when(annotated.getAnnotation(ConfigProperties.class)).thenReturn(configProperties);
		when(configProperties.keyPath()).thenReturn(keyPath);
		when(configProperties.configPropertyType()).thenReturn(REQUEST_SCOPED_NO_WATCHER);
		when(zkServiceReqScoped.getPropertyValue(keyPath)).thenReturn(value);
		String test = sbv.produce(injectionPoint);
		assertEquals(value,test);
	}
	@Test
	public void testProduceAppScopedWithWatcher() throws ConfigPropertiesException {
		when(injectionPoint.getAnnotated()).thenReturn(annotated);
		when(annotated.getAnnotation(ConfigProperties.class)).thenReturn(configProperties);
		when(configProperties.keyPath()).thenReturn(keyPath);
		when(configProperties.configPropertyType()).thenReturn(APPLICATION_SCOPED_WITH_WATCHER);
		when(zkServiceAppScoped.getPropertyValue(keyPath)).thenReturn(value);
		String test = sbv.produce(injectionPoint);
		assertEquals(value,test);
	}
}
