package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.transport.NodeAddress;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class CLICommandNodes extends CLICommand {

    /**
     * @param hydra Service hydra.
     */
    public CLICommandNodes(InternalHydra hydra, CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "nodes";
    }

    @Override
    public String getCommandShort() {
        return ":ns";
    }

    @Override
    protected String generateHelpText() {
        return "Shows a list of all currently connected hydra nodes\n";
    }

    @Override
    protected String generateShortDescr() {
        return "Shows all connected nodes\n";
    }

    @Override
    public CLIResponse execute(String[] args) {
        ActorRef nodes = getHydra().getActorSystem().getActor("/app/nodes");
        Future result = nodes.ask("nodes");
        try {
            @SuppressWarnings("unchecked")
            HashMap<UUID,NodeAddress> connectedNodes = (HashMap<UUID,NodeAddress>) result.get(1, TimeUnit.SECONDS);
            if(connectedNodes.size() == 0) {
                return new CLIResponse(String.format("No nodes connected.\n"));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Connected Nodes: \n");
            for(UUID uuid : connectedNodes.keySet()) {
                sb.append("Node ");
                sb.append(getHydra().getIdMatcher().getId(uuid));
                sb.append(": ");
                NodeAddress n = connectedNodes.get(uuid);
                sb.append(n.getIp());
                sb.append(":");
                sb.append(n.getPort());
                sb.append("(");
                sb.append(uuid);
                sb.append(")\n");
            }
	    sb.append("\n");
            return new CLIResponse(sb.toString());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return new CLIResponse(String.format("An error occured: %s\n", e.getMessage()));
        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
