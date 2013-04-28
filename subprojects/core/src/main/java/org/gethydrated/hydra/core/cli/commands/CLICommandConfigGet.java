package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfigGet extends CLICommand {

    /**
     * Constructor.
     * @param ctx
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandConfigGet(final InternalHydra ctx, final CLICommand root) {
        super(ctx, root);

    }

    @Override
    public final String getCommandWord() {
        return "get";
    }

    @Override
    public final String getCommandShort() {
        return "g";
    }

    @Override
    public final CLIResponse execute(final String[] args) {
        final StringBuilder sb = new StringBuilder();
        if (args.length >= 1) {
            try {
                sb.append(getHydra().getConfiguration().get(args[0]));
                sb.append(System.getProperty("line.separator"));
            } catch (final ConfigItemNotFoundException e) {
                sb.append(String.format("Configuration item %s not found",
                        args[0]));
                sb.append(System.getProperty("line.separator"));
            } catch (final NullPointerException e) {
                sb.append("Caught Nullpointer exception. No Context defined?");
                sb.append(System.getProperty("line.separator"));
            }
        } else {
            sb.append("No key given.");
            sb.append(System.getProperty("line.separator"));
        }
        return new CLIResponse(sb.toString());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }

    @Override
    protected final String generateHelpText() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Aspects one parameter:");
        sb.append(System.getProperty("line.separator"));
        sb.append("The parameter is the key for the configuration value");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Reads a configuration value\n";
    }

}
