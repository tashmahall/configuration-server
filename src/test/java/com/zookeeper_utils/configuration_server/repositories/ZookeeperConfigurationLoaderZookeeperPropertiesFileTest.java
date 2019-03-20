package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
@RunWith(PowerMockRunner.class)
@PrepareForTest(ZookeeperPropertiesFileLoadConfiguration.class)
public class ZookeeperConfigurationLoaderZookeeperPropertiesFileTest {
	@InjectMocks
	private ZookeeperConfigurationLoaderZookeeperPropertiesFile sbv;
	@Mock
	private Properties properties;
	@Mock
	private ServletContext context;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void load() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockStatic(ZookeeperPropertiesFileLoadConfiguration.class);

	}
	
	@Test
	public void testLoadHostAndPort() throws Exception {
		when(ZookeeperPropertiesFileLoadConfiguration.getProperties()).thenReturn(properties);
		when(properties.getProperty(Mockito.eq("zookeeper.host"))).thenReturn("localhost");
		when(properties.getProperty(Mockito.eq("zookeeper.port"))).thenReturn("2181");
		String test = sbv.loadHostAndPort();
		assertEquals("localhost:2181", test);
	}
	@Test
	public void testLoadHostAndPortException() throws Exception {
		when(ZookeeperPropertiesFileLoadConfiguration.getProperties()).thenThrow(IOException.class);
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Error while trying to get the zookeeper properties in the file zookeeper.properties");
		sbv.loadHostAndPort();
	}
	@Test
	public void testGetContexName() {
		String zookeeper = "zookeeper";
		String $zookeeper = StringUtils.join("/",zookeeper);
		when(context.getServletContextName()).thenReturn(zookeeper);
		String test = sbv.getContextName();
		assertEquals($zookeeper, test);
	}
	@Test
	public void testGetGlobalContexName() throws ConfigPropertiesException, IOException {
		String $ans = "/ans";
		when(ZookeeperPropertiesFileLoadConfiguration.getProperties()).thenReturn(properties);
		when(properties.getProperty(Mockito.eq("global.context.name"))).thenReturn($ans);
		String test = sbv.getGlobalContextName();
		assertEquals($ans, test);
	}
	@Test
	public void testGetGlobalContexNameException() throws Exception {
		when(ZookeeperPropertiesFileLoadConfiguration.getProperties()).thenThrow(IOException.class);
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Error while trying to get the zookeeper properties in the file zookeeper.properties");
		sbv.getGlobalContextName();
	}
}
