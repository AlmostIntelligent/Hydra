package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.USID;

import java.net.MalformedURLException;

/**
 * Service id factory.
 */
public class DefaultSIDFactory implements SIDFactory {

    private final ActorSystem actorSystem;

    public DefaultSIDFactory(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;
    }

    @Override
    public SID fromString(String sid) {
        return fromUSID(USID.parse(sid));
    }

    @Override
    public SID fromUSID(USID usid) {
        if (usid.nodeId!=0) {
            return buildForeignNodeSID(usid);
        }
        if (usid.typeId==1) {
            return buildSystemSID(usid);
        }
        return buildUserSID(usid);
    }

    /**
     * Can only find local sids
     * @param path
     * @return
     */
    public SID fromActorPath(ActorPath path) {
        USID usid = null;
        try {
            if(path.isChildOf(ActorPath.apply("/app/services"))) {
                usid = new USID(0L, 0, Long.parseLong(path.getName()));
            } else {
                switch (path.toString()) {
                    case "/app/cli":
                        usid = new USID(0L, 1, 0);
                        break;
                    case "/app/coordinator":
                        usid = new USID(0L, 1, 1);
                        break;
                    case "/app/services":
                        usid = new USID(0L, 1, 2);
                        break;
                    case "/app/nodes":
                        usid = new USID(0L, 1, 3);
                        break;
                    case "/sys/in":
                        usid = new USID(0L, 1, 4);
                        break;
                    case "/sys/out":
                        usid = new USID(0L, 1, 5);
                        break;
                    case "/sys/log":
                        usid = new USID(0L, 1, 6);
                        break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(usid == null) {
            throw new RuntimeException("Could not apply actor path to sid");
        }
        return fromUSID(usid);
    }

    /**
     * Can only find local sids
     * @param ref
     * @return
     */
    public SID fromActorRef(ActorRef ref) {
        return fromActorPath(ref.getPath());
    }

    private SID buildUserSID(USID usid) {
        try {
            ActorPath path = ActorPath.apply("/app/services/"+usid.serviceId);
            ActorRef ref = actorSystem.getActor(path);
            return new LocalSID(ref);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private SID buildSystemSID(USID usid) {
        return null;
    }

    private SID buildForeignNodeSID(USID usid) {
        try {
            ActorPath path = ActorPath.apply("/app/nodes/"+usid.nodeId);
            ActorRef ref = actorSystem.getActor(path);
            return new ForeignSID(usid, ref);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
