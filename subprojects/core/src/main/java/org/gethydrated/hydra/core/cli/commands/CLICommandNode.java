package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 *
 */
public class CLICommandNode extends CLICommand {
    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandNode(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "node";
    }

    @Override
    public String getCommandShort() {
        return ":n";
    }

    @Override
    protected String generateHelpText() {
        return "Changes the processing context of the cli to another node.\n";
    }

    @Override
    protected String generateShortDescr() {
        return "Access to a distant hydra node.\n";
    }

    @Override
    public CLIResponse execute(final String[] args) {
        try {
            if (args.length != 1) {
                return new CLIResponse("Wrong paramter number.\n");
            }
            final int id = Integer.parseInt(args[0]);
            if (id != 0 && getHydra().getNetKernel().isConnected(id)) {
                getRootCommand().setCurrentNodeId(id);
                return new CLIResponse(
                        "You are now on Node "
                                + id
                                + ". To return to your local node, use the 'local' command.\n");
            } else {
                return new CLIResponse("Unknown node id: " + args[0] + "\n");
            }
        } catch (final NumberFormatException e) {
            return new CLIResponse(
                    String.format("An exception occurred: Could not convert + '"
                            + args[0] + "' to a number.\n"));
        }
    }

    @Override
    protected boolean localOnly() {
        return true;
    }
}
