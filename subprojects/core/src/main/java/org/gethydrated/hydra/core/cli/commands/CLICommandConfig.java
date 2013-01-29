package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;

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
     * @param hydra
     *            Service context.
     */
    public CLICommandConfig(final InternalHydra hydra) {
        super(hydra);
        addSubCommand(new CLICommandConfigSet(hydra));
        addSubCommand(new CLICommandConfigGet(hydra));
        addSubCommand(new CLICommandConfigList(hydra));
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
    public String execute(final String[] args) {
        return "";
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
