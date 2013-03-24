package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;

/**
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public class CLICommandShutdown extends CLICommand {

    /**
     *
     * @param ctx context.
     */
    public CLICommandShutdown(final InternalHydra ctx, CLICommand root) {
        super(ctx, root);
    }

    @Override
    public final String getCommandWord() {
        return "shutdown";
    }

    @Override
    public final String getCommandShort() {
        return ":sd";
    }

    @Override
    protected final String generateHelpText() {
        return "";
    }

    @Override
    protected final String generateShortDescr() {
        return "Start hydra shutdown ";
    }

    @Override
    public final String execute(final String[] args) {
        getHydra().shutdown();
        return "Shutdown";
    }

    @Override
    protected boolean localOnly() {
        return false;
    }

}

