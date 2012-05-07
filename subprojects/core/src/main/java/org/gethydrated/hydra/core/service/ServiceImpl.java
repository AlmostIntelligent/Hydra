package org.gethydrated.hydra.core.service;

import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceException;

public class ServiceImpl implements Service {

	private final ServiceActivator activator;
	
	public ServiceImpl(ServiceActivator sa) {
		activator = sa;
	}

	@Override
	public void start() throws ServiceException {
		try {
			activator.start(null);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void stop() throws ServiceException {
		try {
			activator.stop(null);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	
}
