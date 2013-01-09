package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.ServiceApi;
import org.gethydrated.hydra.core.service.Services;

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
    public ServiceApiImpl() {
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

    @Override
    public SIDFactory getSIDFactory() {
        return null;
    }

}
