package org.gethydrated.hydra.api.service;

import org.gethydrated.hydra.api.HydraApi;
import org.gethydrated.hydra.api.platform.PlatformAware;

public interface ServiceApi extends HydraApi, PlatformAware {

    public void registerLocal();

    public void registerGlobal();

    public void getLocalService();

    public void getGlobalService();
}
