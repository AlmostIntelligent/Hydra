package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.InternalHydra;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfigGet extends CLICommand {

    /**
     *
     * @param ctx
     *            Service context.
     * @param root
     */
    public CLICommandConfigGet(final InternalHydra ctx, CLICommand root) {
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
    public final String execute(final String[] args) {
        StringBuilder sb = new StringBuilder();
        if (args.length >= 1) {
            try {
                sb.append(getHydra().getConfiguration().get(args[0]));
                sb.append(System.getProperty("line.separator"));
            } catch (ConfigItemNotFoundException e) {
                sb.append(String.format("Configuration item %s not found", args[0]));
                sb.append(System.getProperty("line.separator"));
            } catch (NullPointerException e) {
                sb.append("Caught Nullpointer exception. No Context defined?");
                sb.append(System.getProperty("line.separator"));
            }
        } else {
           sb.append("No key given.");
        }
        return sb.toString();
    }

    @Override
    protected boolean localOnly() {
        return false;
    }

    @Override
    protected final String generateHelpText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Aspects one parameter:");
        sb.append(System.getProperty("line.separator"));
        sb.append("The parameter is the key for the configuration value");
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Reads a configuration value";
    }

}
