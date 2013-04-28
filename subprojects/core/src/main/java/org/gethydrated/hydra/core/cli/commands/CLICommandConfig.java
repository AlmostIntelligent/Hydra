package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfig extends CLICommand {
    
    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandConfig(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
        addSubCommand(new CLICommandConfigSet(hydra, root));
        addSubCommand(new CLICommandConfigGet(hydra, root));
        addSubCommand(new CLICommandConfigList(hydra, root));
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
    public CLIResponse execute(final String[] args) {
        return new CLIResponse("");
    }

    @Override
    protected boolean localOnly() {
        return false;
    }

    @Override
    protected final String generateHelpText() {
        final StringBuilder sb = new StringBuilder();
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Access configuration\n";
    }

}
