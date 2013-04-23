package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 *
 */
public class CLICommandLocal extends CLICommand {
    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandLocal(final InternalHydra hydra, final CLICommand root) {
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
    public CLIResponse execute(final String[] args) {
        getRootCommand().setCurrentNodeId(0);
        return new CLIResponse("You are now on your local node.\n");
    }

    @Override
    protected boolean localOnly() {
        return true;
    }
}
