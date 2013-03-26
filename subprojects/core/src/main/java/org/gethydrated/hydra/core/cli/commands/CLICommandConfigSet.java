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
public class CLICommandConfigSet extends CLICommand {

    /**
     *
     * @param ctx
     *            Service context.
     * @param root
     */
    public CLICommandConfigSet(final InternalHydra ctx, CLICommand root) {
        super(ctx, root);

    }

    @Override
    public final String getCommandWord() {
        return "set";
    }

    @Override
    public final String getCommandShort() {
        return "s";
    }

    @Override
    public final CLIResponse execute(final String[] args) {
           StringBuilder sb = new StringBuilder();
        if (args.length >= 2) {
            getHydra().getConfiguration().set(args[0], args[1]);
            try {
                sb.append(args[0]);
                sb.append(" = ");
                sb.append(getHydra().getConfiguration().get(args[0]));
                sb.append(System.getProperty("line.separator"));
            } catch (ConfigItemNotFoundException e) {
                sb.append(String.format(
                        "Encountered an exception, while setting %s to %s",
                        args[0], args[1]));
                sb.append(System.getProperty("line.separator"));
            } catch (NullPointerException e) {
                sb.append("Caught Nullpointer exception. No Context defined?");
                sb.append(System.getProperty("line.separator"));
            }
        } else {
            sb.append("Not enough parameters.");
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
        StringBuilder sb = new StringBuilder();
        sb.append("Aspects two parameters:");
        sb.append(System.getProperty("line.separator"));
        sb.append("The first parameter is the key for the configuration value");
        sb.append(System.getProperty("line.separator"));
        sb.append("The second parameter is the new configuration value");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Set a configuration value\n";
    }

}
