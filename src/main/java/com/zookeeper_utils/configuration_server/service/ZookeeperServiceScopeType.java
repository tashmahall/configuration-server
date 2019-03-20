package com.zookeeper_utils.configuration_server.service;

/**
 * Ways to load the Configuration properties from the configuration server.
 * 
 * @author igor.ferreira
 *
 */
public enum ZookeeperServiceScopeType {
	
	/** Carrega a configura��o de contexto da aplica��o na mem�ria. As configura��es permanecem armazenadas no Mapa do em escopo de aplica��o.
	*   Cria observadores para atualizar a configura��o em tempo real, caso sejam alteradas no servidor de configura��o.
	*/
	APPLICATION_SCOPED, 
	
	/** Carregua a configura��o de contexto do aplicativo na mem�ria. As configura��es s�o carregadas no Request Scoped. 
	 */
	REQUEST_SCOPED
}
