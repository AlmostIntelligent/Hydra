package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;

/**
 *
 */
public class CLICommandNode extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public CLICommandNode(InternalHydra hydra, CLICommand root) {
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
        return "Changes the processing context of the cli to another node.";
    }

    @Override
    protected String generateShortDescr() {
        return "Access to a distant hydra node.";
    }

    @Override
    public String execute(String[] args) {
        try {
            if(args.length != 1) {
                return "Wrong paramter number.\n";
            }
            int id = Integer.parseInt(args[0]);
            if(id != 0 && getHydra().getIdMatcher().contains(id)) {
                getRootCommand().setCurrentNodeId(id);
                return "You are now on Node " +  id + ". To return to your local node, use the 'local' command.\n";
            } else {
                return "Unknown node id: " + args[0] + "\n";
            }
        } catch (NumberFormatException e) {
            return String.format("An exception occurred: Could not convert + '" +args[0]+ "' to a number.\n");
        }
    }

    @Override
    protected boolean localOnly() {
        return true;
    }
}
