package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;

/**
 * A simple echo command, simply prints out what comes in.
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class CLICommandEcho extends CLICommand {

    /**
     *
     * @param hydra
     *            Service context.
     */
    public CLICommandEcho(final InternalHydra hydra) {
        super(hydra);
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
    public final String execute(final String[] args) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (i = 0; i < args.length; i++) {
            sb.append(args[i]);
            sb.append(" ");
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    protected final String generateHelpText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Echos everything given as a parameter.");
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Echos the given parameters.";
    }

}
