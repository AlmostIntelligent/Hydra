package org.gethydrated.hydra.core.sid;

import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.USID;

import java.net.MalformedURLException;
import java.util.UUID;

/**
 * Service id factory.
 */
public class DefaultSIDFactory implements SIDFactory {

    private final ActorSystem actorSystem;

    private final IdMatcher idMatcher;

    public DefaultSIDFactory(ActorSystem actorSystem, IdMatcher idMatcher) {
        this.actorSystem = actorSystem;
        this.idMatcher = idMatcher;
    }

    @Override
    public SID fromString(String sid) {
        if (sid == null) {
            throw new RuntimeException("SID string is null.");
        }
        if (!sid.matches("<[0-9]*:[01]:[0-9]*>")) {
            throw new RuntimeException("Invalid SID string.");
        }
        sid = sid.substring(1, sid.length()-1);
        String[] arr = sid.split(":");
        UUID uuid = idMatcher.getUUID(Integer.parseInt(arr[0]));
        return fromUSID(new USID(uuid ,Integer.parseInt(arr[1]),Long.parseLong(arr[2])));
    }

    @Override
    public SID fromUSID(USID usid) {
        if (!usid.nodeId.equals(idMatcher.getLocal())) {
            return buildForeignNodeSID(usid);
        }
        if (usid.typeId==1) {
            return buildSystemSID(usid);
        }
        return buildUserSID(usid);
    }

    /**
     * Can only find local sids
     * @param path source actor path
     * @return created sid
     */
    public SID fromActorPath(ActorPath path) {
        USID usid = null;
        UUID localNode = idMatcher.getLocal();
        try {
            if(path.isChildOf(ActorPath.apply("/app/services"))) {
                usid = new USID(localNode, 0, Long.parseLong(path.getName()));
            } else {
                switch (path.toString()) {
                    case "/app/cli":
                        usid = new USID(localNode, 1, 0);
                        break;
                    case "/app/coordinator":
                        usid = new USID(localNode, 1, 1);
                        break;
                    case "/app/services":
                        usid = new USID(localNode, 1, 2);
                        break;
                    case "/app/nodes":
                        usid = new USID(localNode, 1, 3);
                        break;
                    case "/sys/in":
                        usid = new USID(localNode, 1, 4);
                        break;
                    case "/sys/out":
                        usid = new USID(localNode, 1, 5);
                        break;
                    case "/sys/log":
                        usid = new USID(localNode, 1, 6);
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
     * @param ref source actor ref
     * @return created sid
     */
    public SID fromActorRef(ActorRef ref) {
        return fromActorPath(ref.getPath());
    }

    private SID buildUserSID(USID usid) {
        try {
            ActorPath path = ActorPath.apply("/app/services/"+usid.serviceId);
            ActorRef ref = actorSystem.getActor(path);
            return new LocalSID(idMatcher.getLocal(),ref);
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
