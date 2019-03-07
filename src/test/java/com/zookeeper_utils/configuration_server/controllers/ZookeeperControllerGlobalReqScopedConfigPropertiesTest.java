package com.zookeeper_utils.configuration_server.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.inject.Instance;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;

@RunWith(MockitoJUnitRunner.class)
public class ZookeeperControllerGlobalReqScopedConfigPropertiesTest {

	@Spy
	@InjectMocks
	private ZookeeperControllerGlobalReqScopedConfigProperties sbv;
	
	@Mock
	private	Instance<ZookeeperServicePropertiesInterface> zcInstance;
	
	@Mock
	private ZookeeperServicePropertiesInterface zookeeperServicePropertiesReqScoped;
	
	private Map<String,String> configurationMap ;

	
	@Before
	public void load() {
		when(zcInstance.get()).thenReturn(zookeeperServicePropertiesReqScoped);
		configurationMap = new TreeMap<String,String>();
		configurationMap.put("/zookeeper/first1", null);
		configurationMap.put("/zookeeper/first2","test /zookeeper/first2");
		configurationMap.put("/zookeeper/first1/second1","test /zookeeper/first1/second1");
	}

	@Test
	public void testGetParametersTree() throws Exception {
		String known= "{\"/zookeeper/first1/second1\":\"test /zookeeper/first1/second1\",\"/zookeeper/first2\":\"test /zookeeper/first2\"}";
		when(zookeeperServicePropertiesReqScoped.getPropertiesMap()).thenReturn(configurationMap);
		String test = sbv.getParametersTree();
		assertEquals(known,test);
	}

	@Test
	public void testGetInfo() throws Exception {
		String keyPath = "zookeeper/first2";
		String keyPathValue = "test /zookeeper/first2";
		String known= JackJsonUtils.createJsonLine(StringUtils.join("/",keyPath), keyPathValue);
		when(zookeeperServicePropertiesReqScoped.getPropertyValue(StringUtils.join("/",keyPath))).thenReturn(keyPathValue);
		String test = sbv.getInfo(keyPath);
		assertEquals(known,test);
	}


}
