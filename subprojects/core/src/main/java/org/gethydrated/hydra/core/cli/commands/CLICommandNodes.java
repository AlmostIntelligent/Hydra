package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.InternalHydra;

import java.util.List;
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
    public CLICommandNodes(InternalHydra hydra) {
        super(hydra);
    }

    @Override
    public String getCommandWord() {
        return "nodes";
    }

    @Override
    public String getCommandShort() {
        return "ns";
    }

    @Override
    protected String generateHelpText() {
        return "Shows a list of all currently connected hydra nodes";
    }

    @Override
    protected String generateShortDescr() {
        return "Shows all connected nodes";
    }

    @Override
    public String execute(String[] args) {
        ActorRef nodes = getHydra().getActorSystem().getActor("/app/nodes");
        Future result = nodes.ask("nodes");
        try {
            @SuppressWarnings("unchecked")
            List<String> connectedNodes = (List<String>) result.get(1, TimeUnit.SECONDS);
            if(connectedNodes.size() == 0) {
                return String.format("No nodes connected.\n");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Connected Nodes: \n");
            for(String n : connectedNodes) {
                sb.append("n\n");
            }
            return sb.toString();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return String.format("An error occured: %s\n", e.getMessage());
        }
    }
}
