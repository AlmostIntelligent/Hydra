package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * A simple echo command, simply prints out what comes in.
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class CLICommandEcho extends CLICommand {

    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandEcho(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
    }

    @Override
    public final String getCommandWord() {
        return "echo";
    }

    @Override
    public final String getCommandShort() {
        return ":e";
    }

    @Override
    public final CLIResponse execute(final String[] args) {
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        for (i = 0; i < args.length; i++) {
            sb.append(args[i]);
            sb.append(" ");
        }
        sb.append("\n");
        return new CLIResponse(sb.toString());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }

    @Override
    protected final String generateHelpText() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Echos everything given as a parameter.\n");
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Echos the given parameters.\n";
    }

}
