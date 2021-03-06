package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.io.transport.NodeAddress;

import java.util.Map;
import java.util.UUID;

/**
 *
 */
public class CLICommandNodes extends CLICommand {

    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandNodes(final InternalHydra hydra, final CLICommand root) {
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
    public CLIResponse execute(final String[] args) {
        final Map<UUID, NodeAddress> connectedNodes = getHydra().getNetKernel()
                .getNodesWithAddress();
        if (connectedNodes.size() == 0) {
            return new CLIResponse(String.format("No nodes connected.\n"));
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Connected Nodes: \n");
        for (final UUID uuid : connectedNodes.keySet()) {
            sb.append("Node ");
            sb.append(getHydra().getNetKernel().getID(uuid));
            sb.append(": ");
            final NodeAddress n = connectedNodes.get(uuid);
            sb.append(n.getIp());
            sb.append(":");
            sb.append(n.getPort());
            sb.append("(");
            sb.append(uuid);
            sb.append(") ");
            if (getHydra().getNetKernel().isConnected(uuid)) {
                sb.append("(connected)");
            }
            sb.append("\n");
        }
        sb.append("\n");
        return new CLIResponse(sb.toString());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
