package org.gethydrated.hydra.core.api;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.event.Link;
import org.gethydrated.hydra.api.event.Monitor;
import org.gethydrated.hydra.api.event.UnMonitor;
import org.gethydrated.hydra.api.event.Unlink;
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

    private InternalHydra hydra;

    /**
     * Constructor.
     * @param hydra Internal Hydra representation.
     */
    public ServiceApiImpl(final InternalHydra hydra) {
        super(hydra);
        this.hydra = hydra;
    }

    @Override
    public void registerLocal(final String name, final SID id)
            throws HydraException {
        final ActorRef ref = hydra.getActorSystem().getActor(
                "/app/localregistry");
        try {
            @SuppressWarnings("rawtypes")
            final Future f = ref.ask(new RegisterService(id, name));
            f.get(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void registerGlobal(final String name, final SID id)
            throws HydraException {
        final ActorRef ref = hydra.getActorSystem().getActor(
                "/app/globalregistry");
        try {
            @SuppressWarnings("rawtypes")
            final Future f = ref.ask(new RegisterService(id, name));
            f.get(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void unregisterLocal(final String name) throws HydraException {
        final ActorRef ref = hydra.getActorSystem().getActor(
                "/app/localregistry");
        try {
            final Future<?> f = ref.ask(new UnregisterService(name));
            f.get(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public void unregisterGlobal(final String name) throws HydraException {
        final ActorRef ref = hydra.getActorSystem().getActor(
                "/app/globalregistry");
        try {
            final Future<?> f = ref.ask(new UnregisterService(name));
            f.get(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SID getLocalService(final String name) throws HydraException {
        final ActorRef ref = hydra.getActorSystem().getActor(
                "/app/localregistry");
        try {
            final Future<?> f = ref.ask(name);
            return (SID) f.get(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SID getGlobalService(final String name) throws HydraException {
        final ActorRef ref = hydra.getActorSystem().getActor(
                "/app/globalregistry");
        try {
            final Future<?> f = ref.ask(name);
            return (SID) f.get(10, TimeUnit.SECONDS);
        } catch (final Exception e) {
            throw new HydraException(e);
        }
    }

    @Override
    public SIDFactory getSIDFactory() {
        return hydra.getDefaultSIDFactory();
    }

    @Override
    public void link(final SID sid1, final SID sid2) {
        sid1.tell(new Link(sid1.getUSID(), sid2.getUSID()), sid2);
    }

    @Override
    public void unlink(final SID sid1, final SID sid2) {
        sid1.tell(new Unlink(sid2.getUSID()), sid2);
    }

    @Override
    public void monitor(final SID sid1, final SID sid2) {
        sid2.tell(new Monitor(sid1.getUSID(), sid2.getUSID()), sid1);
    }

    @Override
    public void unmonitor(final SID sid1, final SID sid2) {
        sid2.tell(new UnMonitor(sid1.getUSID()), sid1);
    }

}
