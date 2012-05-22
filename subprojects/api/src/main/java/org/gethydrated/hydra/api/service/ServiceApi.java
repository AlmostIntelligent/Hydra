package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.HydraApi;

public interface ServiceApi extends HydraApi {

    void registerLocal(String name, Object obj);

    void registerGlobal(String name, Object obj);

    void getLocalService(String name);

    void getGlobalService(String name);
}
