package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.platform.PlatformAware;

public interface ServiceApi extends HydraApi {

    public void registerLocal(String name, Object obj);

    public void registerGlobal(String name, Object obj);

    public void getLocalService(String name);

    public void getGlobalService(String name);
}
