package com.zookeeper_utils.configuration_server.controllers;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperties;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesAppScoped;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;


@Path("/parametrosAppScoped")
@RequestScoped
public class ZookeeperControllerAppScopedConfigProperties implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	
	@Inject
	@ZKServicePropertiesAppScoped
	private	Instance<ZookeeperServicePropertiesInterface> zcInstance;
	
	@Inject
	@ConfigProperties(keyPath="/appwithwatcher",configPropertyType=ZookeeperServicePropertyType.APPLICATION_SCOPED_WITH_WATCHER)
	private String testeAppScopedWithWatcher;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getKeys() throws ConfigPropertiesException, JsonProcessingException  {
		log.debug("teste_APPLICATION_SCOPED_WITH_WATCHER Working "+ testeAppScopedWithWatcher);
		String tree = Arrays.toString( zcInstance.get().getPropertiesMap().keySet().toArray());
		zcInstance.destroy(zcInstance.get());
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, "configuration-tree",tree);
		return JackJsonUtils.getString(response);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws ConfigPropertiesException, JsonProcessingException  {
		Map<String,String> map=	zcInstance.get().getPropertiesMap();
		zcInstance.destroy(zcInstance.get());
		return JackJsonUtils.entityToJsonString(map);
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws ConfigPropertiesException, JsonProcessingException  {
		String keyValue = zcInstance.get().getPropertyValue(key);
		zcInstance.destroy(zcInstance.get());
		return JackJsonUtils.createJsonLine(key, keyValue);
	}
}
