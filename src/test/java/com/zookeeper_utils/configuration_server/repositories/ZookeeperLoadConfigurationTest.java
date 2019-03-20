package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

public class ZookeeperLoadConfigurationTest {
	@Test
	public void testGetProperties() throws IOException {
		String fileName = "zookeeper.properties";
		Properties properties = new Properties();
		properties.load(ZookeeperRepositoryInterface.class.getClassLoader().getResourceAsStream(fileName));
		Properties pTest = ZookeeperPropertiesFileLoadConfiguration.getProperties();
		assertEquals(properties, pTest);
	}

}
