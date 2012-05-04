package org.gethydrated.hydra.api.service;

public interface Service {

	void start() throws ServiceException;
	
	void stop() throws ServiceException;
}
