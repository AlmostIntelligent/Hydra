package org.gethydrated.hydra.core.sid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.gethydrated.hydra.actors.ActorPath;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.actors.ActorSystem;
import org.gethydrated.hydra.actors.refs.NullRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.USID;
import org.gethydrated.hydra.core.io.network.NodeController;

import java.net.MalformedURLException;
import java.util.UUID;

/**
 * Service id factory.
 */
public class DefaultSIDFactory implements SIDFactory {

    private final ActorSystem actorSystem;

    private final NodeController nodeController;

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructor.
     * @param actorSystem Actor system.
     * @param nodeController Node controller.
     */
    public DefaultSIDFactory(final ActorSystem actorSystem,
            final NodeController nodeController) {
        this.actorSystem = actorSystem;
        this.nodeController = nodeController;
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.registerModule(new JaxbAnnotationModule());
    }

    @Override
    public SID fromString(final String sid) {
        if (sid == null) {
            throw new RuntimeException("SID string is null.");
        }
        if (!sid.matches("<[0-9]*:[01]:[0-9]*>")) {
            throw new RuntimeException("Invalid SID string.");
        }
        String tmp = sid.substring(1, sid.length() - 1);
        final String[] arr = tmp.split(":");
        final UUID uuid = nodeController.getUUID(Integer.parseInt(arr[0]));
        return fromUSID(new USID(uuid, Integer.parseInt(arr[1]),
                Long.parseLong(arr[2])));
    }

    @Override
    public SID fromUSID(final USID usid) {
        if (!usid.getNodeId().equals(nodeController.getLocal())) {
            return buildForeignNodeSID(usid);
        }
        if (usid.getTypeId() == 1) {
            return buildSystemSID(usid);
        }
        if (usid.getTypeId() == 2) {
            return buildTmpSID(usid);
        }
        return buildUserSID(usid);
    }

    /**
     * Can only find local SIDs.
     * 
     * @param path
     *            source actor path
     * @return created SID
     */
    public SID fromActorPath(final ActorPath path) {
        final UUID localNode = nodeController.getLocal();
        final USID usid = actorPathToUSID(path, localNode);
        if (usid == null) {
            return new DeadSID(new USID(localNode, 0, 0));
        }
        return fromUSID(usid);
    }

    /**
     * Can only find local SIDs.
     * 
     * @param ref
     *            source actor ref
     * @return created SID
     */
    public SID fromActorRef(final ActorRef ref) {
        return fromActorPath(ref.getPath());
    }

    private SID buildUserSID(final USID usid) {
        try {
            final ActorPath path = ActorPath.apply("/app/services/"
                    + usid.getServiceId());
            final ActorRef ref = actorSystem.getActor(path);
            if (ref.isTerminated()) {
                return new DeadSID(usid);
            }
            return new LocalSID(nodeController.getLocal(), ref, mapper.writer());
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private SID buildTmpSID(final USID usid) {
        final ActorRef ref = actorSystem
                .getActor("/tmp/" + usid.getServiceId());
        return new TmpSID(usid, ref);
    }

    private SID buildSystemSID(final USID usid) {
        final ActorPath path = usidToActorPath(usid);
        if (path != null) {
            final ActorRef ref = actorSystem.getActor(path);

            return new LocalSystemSID(usid, ref);
        }
        throw new RuntimeException("Could not match usid:" + usid);
    }

    private SID buildForeignNodeSID(final USID usid) {
        try {
            final ActorPath path = ActorPath.apply("/app/nodes/"
                    + nodeController.getID(usid.getNodeId()));
            final ActorRef ref = actorSystem.getActor(path);
            return new ForeignSID(nodeController.getID(usid.getNodeId()), usid,
                    ref, mapper.writer());
        } catch (final MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transforms an actor path to an equivalent USID.
     * @param path actor path
     * @param nodeId node id.
     * @return Unique service identifier.
     */
    public static USID actorPathToUSID(final ActorPath path, final UUID nodeId) {
        USID usid = null;
        try {
            if (path.isChildOf(ActorPath.apply("/app/services"))) {
                usid = new USID(nodeId, 0, Long.parseLong(path.getName()));
            } else if (path.isChildOf(ActorPath.apply("/tmp"))) {
                usid = new USID(nodeId, 2, Long.parseLong(path.getName()));
            } else {
                switch (path.toString()) {
                case "/app/cli":
                    usid = new USID(nodeId, 1, 0);
                    break;
                case "/app/locking":
                    usid = new USID(nodeId, 1, 1);
                    break;
                case "/app/services":
                    usid = new USID(nodeId, 1, 2);
                    break;
                case "/app/nodes":
                    usid = new USID(nodeId, 1, 3);
                    break;
                case "/sys/in":
                    usid = new USID(nodeId, 1, 4);
                    break;
                case "/sys/out":
                    usid = new USID(nodeId, 1, 5);
                    break;
                case "/sys/log":
                    usid = new USID(nodeId, 1, 6);
                    break;
                case "/app/localregistry":
                    usid = new USID(nodeId, 1, 7);
                    break;
                case "/app/globalregistry":
                    usid = new USID(nodeId, 1, 8);
                    break;
                default:
                    break;
                }
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return usid;
    }

    /**
     * Transforms an USID to an equivalent actor path.
     * @param usid Unique service identifier.
     * @return actor path.
     */
    public static ActorPath usidToActorPath(final USID usid) {
        if (usid == null) {
            return new NullRef().getPath();
        }
        String path = null;
        if (usid.getTypeId() == 0) {
            path = "/app/services/" + usid.getServiceId();
        } else if (usid.getTypeId() == 2) {
            path = "/tmp/" + usid.getServiceId();
        } else {
            switch ((int) usid.getServiceId()) {
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
            default:
                break;
            }
        }
        if (path != null) {
            try {
                return ActorPath.apply(path);
            } catch (final MalformedURLException e) {
                // should never happen.
                e.printStackTrace();
            }
        }
        return null;
    }

}
