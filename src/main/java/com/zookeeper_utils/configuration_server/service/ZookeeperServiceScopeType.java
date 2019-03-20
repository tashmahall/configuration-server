package com.zookeeper_utils.configuration_server.service;

/**
 * Ways to load the Configuration properties from the configuration server.
 * 
 * @author igor.ferreira
 *
 */
public enum ZookeeperServiceScopeType {
	
	/** Carrega a configuração de contexto da aplicação na memória. As configurações permanecem armazenadas no Mapa do em escopo de aplicação.
	*   Cria observadores para atualizar a configuração em tempo real, caso sejam alteradas no servidor de configuração.
	*/
	APPLICATION_SCOPED, 
	
	/** Carregua a configuração de contexto do aplicativo na memória. As configurações são carregadas no Request Scoped. 
	 */
	REQUEST_SCOPED
}
