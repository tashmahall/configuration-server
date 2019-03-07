package com.zookeeper_utils.configuration_server.controllers;

import javax.enterprise.context.RequestScoped;
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
import com.zookeeper_utils.configuration_server.services.annotations.ZKServicePropertiesRequestScoped;
import com.zookeeper_utils.configuration_server.utils.JackJsonUtils;


@Path("/parametrosReqScoped")
@RequestScoped
public class ZookeeperControllerReqScopedConfigProperties{
	
	@Inject
	@ZKServicePropertiesRequestScoped
	private ZookeeperServicePropertiesInterface zookeeperServicePropertiesReqScoped;
	
	@Inject
	@ConfigProperties(keyPath="/requestnowatcher",configPropertyType=ZookeeperServicePropertyType.REQUEST_SCOPED_NO_WATCHER)
	private String testeRequestScopedNoWatcher;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getParametersTree() throws  ConfigPropertiesException, JsonProcessingException{
		return JackJsonUtils.entityToJsonString(zookeeperServicePropertiesReqScoped.getPropertiesMap());
	}
	
	@GET
	@Path("/{key}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInfo(@PathParam("key") String key) throws ConfigPropertiesException, JsonProcessingException {
		String realPath = StringUtils.join("/",key);
		String keyValue = zookeeperServicePropertiesReqScoped.getPropertyValue(realPath);
		return JackJsonUtils.createJsonLine(realPath, keyValue);
	}
}
