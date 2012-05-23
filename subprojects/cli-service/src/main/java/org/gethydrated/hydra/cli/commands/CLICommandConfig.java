package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfig extends CLICommand {

    /**
         * 
         */

    /**
     * 
     * @param ctx
     *            Service context.
     */
    public CLICommandConfig(final ServiceContext ctx) {
        super(ctx);
        addSubCommand(new CLICommandConfigSet(ctx));
        addSubCommand(new CLICommandConfigGet(ctx));
        addSubCommand(new CLICommandConfigList(ctx));
    }

    @Override
    public final String getCommandWord() {
        return "configuration";
    }

    @Override
    public final String getCommandShort() {
        return "cfg";
    }

    @Override
    public void executeCommand(final String[] args) {

    }

    @Override
    protected final String generateHelpText() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Access configuration";
    }

}
