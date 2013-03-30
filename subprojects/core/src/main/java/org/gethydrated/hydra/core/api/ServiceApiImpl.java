package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.ServiceApi;

/**
 * Service api implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceApiImpl extends HydraApiImpl implements ServiceApi {

    /**
     * Constructor.
     */
    public ServiceApiImpl(Hydra hydra) {
        super(hydra);
    }

    @Override
    public void registerLocal(String name, SID id) throws HydraException {
        throw new HydraException("");
    }

    @Override
    public void registerGlobal(String name, SID id) throws HydraException {
        throw new HydraException("");
    }

    @Override
    public void unregisterLocal(String name) throws HydraException {
        throw new HydraException("");
    }

    @Override
    public void unregisterGlobal(String name) throws HydraException {
        throw new HydraException("");
    }

    @Override
    public SID getLocalService(String name) throws HydraException {
        throw new HydraException("");
    }

    @Override
    public SID getGlobalService(String name) throws HydraException {
        throw new HydraException("");
    }

    @Override
    public SIDFactory getSIDFactory() {
        return null;
    }

    @Override
    public void link(SID sid) {

    }

    @Override
    public void unlink(SID sid) {

    }

    @Override
    public void monitor(SID sid) {

    }

    @Override
    public void unmonitor(SID sid) {

    }

}
