package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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
public class ZookeeperConfigurationLoaderJbossGlobalBindsTest {
	@InjectMocks
	private ZookeeperConfigurationLoaderJbossGlobalBinds sbv;
	@Mock
	private InitialContext initialContext;
	
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
		when(ZookeeperPropertiesFileLoadConfiguration.getInitialContext()).thenReturn(initialContext);
		when(initialContext.lookup(Mockito.eq("java:global/zookeeperHost"))).thenReturn("localhost");
		when(initialContext.lookup(Mockito.eq("java:global/zookeeperPort"))).thenReturn("2181");
		
		String test = sbv.loadHostAndPort();
		assertEquals("localhost:2181", test);
	}
	@Test
	public void testLoadHostAndPortException() throws Exception {
		when(ZookeeperPropertiesFileLoadConfiguration.getInitialContext()).thenThrow(new NamingException());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error [null] while trying to get the zookeeper host and port");
		sbv.loadHostAndPort();
	}
	@Test
	public void testGetContextNamen() {
		String zookepper = "zookeeper";
		when(context.getServletContextName()).thenReturn(zookepper);
		String test = sbv.getContextName();
        assertEquals( StringUtils.join("/",zookepper), test);
	}
	@Test
	public void testGetGlobalContextName() throws NamingException, ConfigPropertiesException {
		String $ans = "/ans";
		when(ZookeeperPropertiesFileLoadConfiguration.getInitialContext()).thenReturn(initialContext);
		when(initialContext.lookup(Mockito.eq("java:global/ctxGlobalName"))).thenReturn("/ans");
		String test = sbv.getGlobalContextName();
        assertEquals( $ans, test);
	}
	@Test(expected=ConfigPropertiesException.class)
	public void testGetGlobalContextNameExcpetion() throws Exception {
		when(ZookeeperPropertiesFileLoadConfiguration.getInitialContext()).thenReturn(initialContext);
		when(initialContext.lookup(Mockito.eq("java:global/ctxGlobalName"))).thenThrow(new NamingException());
		sbv.getGlobalContextName();
	}
}
