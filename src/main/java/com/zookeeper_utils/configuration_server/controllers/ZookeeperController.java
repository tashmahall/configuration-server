package com.zookeeper_utils.configuration_server.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.lalyos.jfiglet.FigletFont;
import com.zookeeper_utils.configuration_server.properties.ConfigProperties;
import com.zookeeper_utils.configuration_server.properties.ZookeeperConfigProperties;
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
	private ServletContext context;
	
	@Inject
	private ZookeeperConfigProperties zookeeperConfigProperties;
	
	@Inject
	@ConfigProperties(keyPath="/test")
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
		return zookeeperConfigProperties.getMessage("/"+context.getServletContextName()+"/info/ambiente/keys");
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws Exception {
        return zookeeperConfigProperties.getMessage("/"+key);
	}
	@GET
	@Path("/configurationtree")
	@Produces(MediaType.APPLICATION_JSON)
	public String getConfigurationTree() throws Exception {
		ObjectNode response = JackJsonUtils.createNewNode();
		JackJsonUtils.put(response, "configuration-tree", zookeeperConfigProperties.getConfigurationTree());
		return JackJsonUtils.getString(response);
	}

}
