package com.zookeeper_utils.configuration_server.exceptions;

public class ConfigPropertiesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ConfigPropertiesException(String message) {
		super(message);
	}

	public ConfigPropertiesException(String message, Exception e1) {
		super(message,e1);
	}

}
