package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;

import com.zookeeper_utils.configuration_server.repositories.ZookeeperLoadConfiguration;
import com.zookeeper_utils.configuration_server.repositories.ZookeeperRepositoryInterface;

public class ZookeeperLoadConfigurationTest {
	@Test
	public void testGetProperties() throws IOException {
		String fileName = "zookeeper.properties";
		Properties properties = new Properties();
		properties.load(ZookeeperRepositoryInterface.class.getClassLoader().getResourceAsStream(fileName));
		Properties pTest = ZookeeperLoadConfiguration.getProperties();
		assertEquals(properties, pTest);
	}

}
