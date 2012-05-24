package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Christian Kulpa
 *
 */
public class CLICommandShutdown extends CLICommand {

    /**
     * 
     * @param ctx context.
     */
    public CLICommandShutdown(final ServiceContext ctx) {
        super(ctx);
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
    public final void executeCommand(final String[] args) {
        System.exit(0);
    }

}
