package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

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
    public final String execute(final String[] args) {
        System.exit(0);   /* Is there a smoother way to do this?*/
        return "Shutdown";
    }

}

