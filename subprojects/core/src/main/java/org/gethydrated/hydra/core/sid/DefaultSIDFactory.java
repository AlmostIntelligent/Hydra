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
        if (!usid.getNodeId().equals(idMatcher.getLocal())) {
            return buildForeignNodeSID(usid);
        }
        if (usid.getTypeId()==1) {
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
                    case "/app/locking":
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
                    case "/app/localregistry":
                        usid = new USID(localNode, 1, 7);
                        break;
                    case  "/app/globalregistry":
                        usid = new USID(localNode, 1, 8);
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
            ActorPath path = ActorPath.apply("/app/services/"+usid.getServiceId());
            ActorRef ref = actorSystem.getActor(path);
            return new LocalSID(idMatcher.getLocal(),ref);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private SID buildSystemSID(USID usid) {
        String path = null;
        switch ((int)usid.getServiceId()) {
            case 0:
                path = "/app/cli";
                break;
            case 1:
                path = "/app/locking";
                break;
            case 2:
                path = "/app/services";
                break;
            case 3:
                path = "/app/nodes";
                break;
            case 4:
                path = "/sys/in";
                break;
            case 5:
                path = "/sys/out";
                break;
            case 6:
                path = "/sys/log";
                break;
            case 7:
                path = "/app/localregistry";
                break;
            case 8:
                path = "/app/globalregistry";
                break;
        }
        if(path != null) {
            ActorRef ref = actorSystem.getActor(path);

            return new LocalSystemSID(usid, ref);
        }
        throw new RuntimeException("Could not match usid:" + usid);
    }

    private SID buildForeignNodeSID(USID usid) {
        try {
            ActorPath path = ActorPath.apply("/app/nodes/"+idMatcher.getId(usid.getNodeId()));
            ActorRef ref = actorSystem.getActor(path);
            return new ForeignSID(idMatcher.getId(usid.getNodeId()),usid, ref);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}
