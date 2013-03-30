package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.ServiceApi;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.registry.RegisterService;
import org.gethydrated.hydra.core.registry.UnregisterService;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Service api implementation.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceApiImpl extends HydraApiImpl implements ServiceApi {

    InternalHydra hydra;

    /**
     * Constructor.
     */
    public ServiceApiImpl(InternalHydra hydra) {
        super(hydra);
        this.hydra = hydra;
    }

    @Override
    public void registerLocal(String name, SID id) throws HydraException {
        ActorRef ref = hydra.getActorSystem().getActor("/app/localregistry");
        try {
            Future f = ref.ask(new RegisterService(id, name));
            f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void registerGlobal(String name, SID id) throws HydraException {
        ActorRef ref = hydra.getActorSystem().getActor("/app/globalregistry");
        try {
            Future f = ref.ask(new RegisterService(id, name));
            f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void unregisterLocal(String name) throws HydraException {
        ActorRef ref = hydra.getActorSystem().getActor("/app/localregistry");
        try {
            Future f = ref.ask(new UnregisterService(name));
            f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void unregisterGlobal(String name) throws HydraException {
        ActorRef ref = hydra.getActorSystem().getActor("/app/globalregistry");
        try {
            Future f = ref.ask(new UnregisterService(name));
            f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SID getLocalService(String name) throws HydraException {
        ActorRef ref = hydra.getActorSystem().getActor("/app/localregistry");
        try {
            Future f = ref.ask(name);
            return (SID) f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SID getGlobalService(String name) throws HydraException {
        ActorRef ref = hydra.getActorSystem().getActor("/app/globalregistry");
        try {
            Future f = ref.ask(name);
            return (SID) f.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SIDFactory getSIDFactory() {
        return hydra.getDefaultSIDFactory();
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
