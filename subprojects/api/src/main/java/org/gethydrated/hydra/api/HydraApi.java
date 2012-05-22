package org.gethydrated.hydra.api;

public interface HydraApi {

    public void startService(String name) throws HydraException;

    public void stopService(String name) throws HydraException;

}
