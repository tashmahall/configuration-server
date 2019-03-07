package com.zookeeper_utils.configuration_server.controllers;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zookeeper_utils.configuration_server.exceptions.ConfigPropertiesException;
import com.zookeeper_utils.configuration_server.properties.annotations.ConfigProperties;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertiesInterface;
import com.zookeeper_utils.configuration_server.services.ZookeeperServicePropertyType;
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesGlobalRequestScoped;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;


@Path("/parametrosGlobalReq")
@RequestScoped
public class ZookeeperControllerGlobalReqScopedConfigProperties implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	@ZKServicePropertiesGlobalRequestScoped
	private	Instance<ZookeeperServicePropertiesInterface> zcInstance;
	
	@Inject
	@ConfigProperties(keyPath="/globalrequest",configPropertyType=ZookeeperServicePropertyType.GLOBAL_CONTEXT_NO_WATCHER)
	private String testeGlobalCtxNoWatcher;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws JsonProcessingException, ConfigPropertiesException {
		return JackJsonUtils.entityToJsonString(zcInstance.get().getPropertiesMap());
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws ConfigPropertiesException, JsonProcessingException {
		String realPath = StringUtils.join("/",key);
		String keyValue = zcInstance.get().getPropertyValue(realPath);
		zcInstance.destroy(zcInstance.get());
		return JackJsonUtils.createJsonLine(realPath, keyValue);
	}
}
