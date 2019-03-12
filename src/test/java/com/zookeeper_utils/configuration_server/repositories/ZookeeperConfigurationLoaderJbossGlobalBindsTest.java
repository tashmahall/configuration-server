package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import javax.naming.InitialContext;
import javax.naming.NamingException;

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
import com.zookeeper_utils.configuration_server.repositories.ZookeeperConfigurationLoaderJbossGlobalBinds;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperLoadConfiguration;
@RunWith(PowerMockRunner.class)
@PrepareForTest(ZookeeperLoadConfiguration.class)
public class ZookeeperConfigurationLoaderJbossGlobalBindsTest {
	@InjectMocks
	private ZookeeperConfigurationLoaderJbossGlobalBinds sbv;
	@Mock
	private InitialContext context;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void load() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockStatic(ZookeeperLoadConfiguration.class);

	}
	
	@Test
	public void testLoadHostAndPort() throws Exception {
		when(ZookeeperLoadConfiguration.getInitialContext()).thenReturn(context);
		when(context.lookup(Mockito.eq("java:global/zookeeperHost"))).thenReturn("localhost");
		when(context.lookup(Mockito.eq("java:global/zookeeperPort"))).thenReturn("2181");
		String test = sbv.loadHostAndPort();
		assertEquals("localhost:2181", test);
	}
	@Test
	public void testLoadHostAndPortException() throws Exception {
		when(ZookeeperLoadConfiguration.getInitialContext()).thenThrow(NamingException.class);
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error [null] while trying to get the zookeeper host and port");
		sbv.loadHostAndPort();
	}
}
