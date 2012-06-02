package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.service.ServiceApi;
import org.gethydrated.hydra.core.service.ServiceManager;

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
     * 
     * @param sm
     *            ServiceManager.
     */
    public ServiceApiImpl(final ServiceManager sm) {
        super(sm);
    }

    @Override
    public void registerLocal(final String name, final Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerGlobal(final String name, final Long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public final Long getLocalService(final String name) {
        return null;
        // TODO Auto-generated method stub

    }

    @Override
    public final Long getGlobalService(final String name) {
        return null;
        // TODO Auto-generated method stub

    }

}
