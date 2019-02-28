package com.zookeeper_utils.configuration_server.controllers;

import java.io.Serializable;
import java.util.Arrays;

import javax.enterprise.context.RequestScoped;
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
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesRequestScoped;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;


@Path("/parametrosReqScoped")
@RequestScoped
public class ZookeeperControllerReqScopedConfigProperties implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	
	@Inject
	@ZKServicePropertiesRequestScoped
	private ZookeeperServicePropertiesInterface zookeeperServicePropertiesReqScoped;
	
	@Inject
	@ConfigProperties(keyPath="/requestnowatcher",configPropertyType=ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER)
	private String teste_REQUEST_SCOPED_NO_WATCHER;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getKeys() throws JsonProcessingException, ConfigPropertiesException  {
		log.debug("teste_REQUEST_SCOPED_NO_WATCHER Working "+ teste_REQUEST_SCOPED_NO_WATCHER);
		String tree = Arrays.toString( zookeeperServicePropertiesReqScoped.getPropertiesMap().keySet().toArray());
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, "configuration-tree",tree);
		return JackJsonUtils.getString(response);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws Exception {
		return JackJsonUtils.entityToJsonString(zookeeperServicePropertiesReqScoped.getPropertiesMap());
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws Exception {
		String keyValue = zookeeperServicePropertiesReqScoped.getPropertyValue(key);
		return JackJsonUtils.createJsonLine(key, keyValue);
	}
}
