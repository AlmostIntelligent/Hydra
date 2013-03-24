package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;

/**
 *
 */
public class CLICommandLocal extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public CLICommandLocal(InternalHydra hydra, CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "local";
    }

    @Override
    public String getCommandShort() {
        return ":l";
    }

    @Override
    protected String generateHelpText() {
        return "Changes the processing context of the cli to the local hydra node.\n";
    }

    @Override
    protected String generateShortDescr() {
        return "Access to the local hydra node.\n";
    }

    @Override
    public String execute(String[] args) {
        getRootCommand().setCurrentNodeId(0);
        return "You are now on your local node.\n";
    }

    @Override
    protected boolean localOnly() {
        return true;
    }
}
