package com.zookeeper_utils.configuration_server.repositories;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.api.GetDataBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;


@RunWith(MockitoJUnitRunner.class)
public class ZookeeperRepositoryWithoutWhatcherTest {
	@InjectMocks
	@Spy
	private ZookeeperRepositoryWithoutWatcher sbv;
	@Mock
	private CuratorFramework clientZookeeper;
	@Mock
	private ServletContext context;
	@Mock
	private GetChildrenBuilder getChildrenBuilder;
	@Mock
	private GetDataBuilder getDataBuilder;
	
	private Map<String,String> configurationMap ;
	
    @Rule
    public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void loadConfigurationMap() {
		configurationMap = new TreeMap<String,String>();
		configurationMap.put("/zookeeper/first1", null);
		configurationMap.put("/zookeeper/first2","test /zookeeper/first2");
		configurationMap.put("/zookeeper/first1/second1","test /zookeeper/first1/second1");
		MockitoAnnotations.initMocks(this);
	}
	@Test
	public void testGetKeyPathTree() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first2= $zookeeper+"/"+"first2";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		byte[] test$zookeeper$f2= "test /zookeeper/first2".getBytes();
		byte[] test$zookeeper$f1$s1= "test /zookeeper/first1/second1".getBytes();
		List<String> list1 = Arrays.asList("first1","first2");
		List<String> list2 = Arrays.asList("second1"); 
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(list2);
		when(clientZookeeper.getChildren().forPath($zookeeper$first2)).thenReturn(null);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1$second1)).thenReturn(null);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(null);
		when(clientZookeeper.getData().forPath($zookeeper$first2)).thenReturn(test$zookeeper$f2);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenReturn(test$zookeeper$f1$s1);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap, test);
	}

	@Test
	public void testGetKeyPathTreeListIsEmpty() throws Exception {
		Map<String,String>configurationMap2 = new TreeMap<String,String>();
		configurationMap2.put("/zookeeper/first1", null);
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1");
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(new ArrayList<String>());
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(null);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap2, test);
	}	
	@Test
	public void testDefineWatcherByteArrayLessThanOne() throws Exception {
		Map<String,String>configurationMap2 = new TreeMap<String,String>();
		configurationMap2.put("/zookeeper/first1", null);
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1");
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(new ArrayList<String>());
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenReturn(new byte[0]);
		Map<String,String> test =sbv.getKeyPathTree();
		assertEquals(configurationMap2, test);
	}
	@Test
	public void testExceptionTreeGeneratorWatcherGetKeyPathTree() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while getting children properties to the 'keyPath' ["+$zookeeper+"]");
		sbv.getKeyPathTree();
	}
	
	@Test
	public void testExceptionDefineWatcherGetKeyPathTree() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		List<String> list1 = Arrays.asList("first1","first2");
		List<String> list2 = Arrays.asList("second1"); 
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getChildren()).thenReturn(getChildrenBuilder);
		when(clientZookeeper.getChildren().forPath($zookeeper)).thenReturn(list1);
		when(clientZookeeper.getChildren().forPath($zookeeper$first1)).thenReturn(list2);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while load the properties to the 'keyPath' ["+$zookeeper$first1+"]");
		sbv.getKeyPathTree();
	}
	
	@Test
	public void testGetValueFromKeyPath() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		byte[] test$zookeeper$f1$s1= "test /zookeeper/first1/second1".getBytes();
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenReturn(test$zookeeper$f1$s1);
		String test =sbv.getValueFromKeyPath("/first1/second1");
		assertEquals(configurationMap.get($zookeeper$first1$second1), test);
	}
	@Test
	public void testGetValueFromKeyPathException() throws Exception {
		String zookeeper = "zookeeper";
		String $zookeeper = "/"+zookeeper;
		String $zookeeper$first1= $zookeeper+"/"+"first1";
		String $zookeeper$first1$second1= $zookeeper$first1+"/"+"second1";
		when(context.getServletContextName()).thenReturn(zookeeper);
		when(clientZookeeper.getData()).thenReturn(getDataBuilder);
		when(clientZookeeper.getData().forPath($zookeeper$first1$second1)).thenThrow(new Exception());
        thrown.expect(ConfigPropertiesException.class);
        thrown.expectMessage("Got the error null while load the properties to the 'keyPath' [/first1/second1]");
        sbv.getValueFromKeyPath("/first1/second1");
	}
}
