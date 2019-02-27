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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.lalyos.jfiglet.FigletFont;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperties;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesAppScoped;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;


@Path("/parametros")
@RequestScoped
public class ZookeeperController implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
	
	@Inject
	@ZKServicePropertiesAppScoped
	private ZookeeperServicePropertiesInterface zookeeperConfigProperties;
	
	@Inject
	@ConfigProperties(keyPath="/test",configPropertyType=ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER)
	private String teste;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getKeys() throws Exception {
		log.debug(FigletFont.convertOneLine("Test Working "+ teste));
		
		return getConfigurationTree();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws Exception {
		return JackJsonUtils.entityToJsonString(zookeeperConfigProperties.getPropertiesMap());
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws Exception {
		String keyValue = zookeeperConfigProperties.getPropertyValue(key);
		return JackJsonUtils.createJsonLine(key, keyValue);
	}
	@GET
	@Path("/configurationtree")
	@Produces(MediaType.APPLICATION_JSON)
	public String getConfigurationTree() throws Exception {
		String tree = Arrays.toString( zookeeperConfigProperties.getPropertiesMap().keySet().toArray());
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, "configuration-tree",tree);
		return JackJsonUtils.getString(response);
	}

}
