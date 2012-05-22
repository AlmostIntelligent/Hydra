package org.gethydrated.hydra.core.service;

import java.util.List;

import org.gethydrated.hydra.api.service.Service;
import org.gethydrated.hydra.api.service.ServiceActivator;
import org.gethydrated.hydra.api.service.ServiceException;

public class ServiceImpl implements Service {

    private final List<ServiceActivator> activators;

    public ServiceImpl(List<ServiceActivator> sa) {
        activators = sa;
    }

    @Override
    public void start() throws ServiceException {
        try {
            for (ServiceActivator sa : activators) {
                sa.start(null);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void stop() throws ServiceException {
        try {
            for (ServiceActivator sa : activators) {
                sa.stop(null);
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

}
