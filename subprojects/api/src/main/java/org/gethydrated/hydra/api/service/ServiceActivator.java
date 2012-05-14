package org.gethydrated.hydra.api.service;

public interface ServiceActivator {
    void start(ServiceContext context) throws Exception;

    void stop(ServiceContext context) throws Exception;
}
