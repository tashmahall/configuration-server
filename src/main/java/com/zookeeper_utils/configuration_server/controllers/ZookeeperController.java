package com.zookeeper_utils.configuration_server.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zookeeper_utils.configuration_server.properties.ZookeeperConfigProperties;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;


@Path("/parametros")
@ApplicationScoped
public class ZookeeperController {
	@Inject
	private ServletContext context;
	@Inject
	private ZookeeperConfigProperties config;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getKeys() throws Exception {
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, "Keys", config.getMessage("/showcase-rest/info/ambiente/keys"));
		return JackJsonUtils.getString(response);
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws Exception {
		return config.getMessage("/showcase-rest/info/ambiente/keys");
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws Exception {
        return config.getMessage("/"+key);
	}
	@GET
	@Path("/configurationtree")
	@Produces(MediaType.APPLICATION_JSON)
	public String getConfigurationTree() throws Exception {
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, "configuration-tree", config.getConfigurationTree("/"+context.getServletContextName()));
		return JackJsonUtils.getString(response);
	}

}
