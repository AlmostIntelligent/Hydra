package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandRoot extends CLICommand {

    /**
     * 
     * @param ctx
     *            .
     */
    public CLICommandRoot(final ServiceContext ctx) {
        super(ctx);
    }

    @Override
    public final String getCommandWord() {
        return "";
    }

    @Override
    public final String getCommandShort() {
        return getCommandWord();
    }

    @Override
    public final String execute(final String[] args) {
        return "";

    }

    @Override
    protected final String generateHelpText() {
        return "";
    }

    @Override
    protected final String generateShortDescr() {
        return "CLI Service";
    }

}
