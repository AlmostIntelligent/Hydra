package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.service.ServiceContext;

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
     */
    public CLICommandConfigSet(final ServiceContext ctx) {
        super(ctx);

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
    public final void execute(final String[] args) {
        if (args.length >= 2) {
            getContext().getConfiguration().set(args[0], args[1]);
            try {
                getOutput().printf("%s = %s", args[0],
                        getContext().getConfiguration().get(args[0]));
                getOutput().println();
            } catch (ConfigItemNotFoundException e) {
                getOutput().printf(
                        "Encountered an exception, while setting %s to %s",
                        args[0], args[1]);
                getOutput().println();
            } catch (NullPointerException e) {
                getOutput().println("Caught Nullpointer exception. No Context defined?");
            }
        } else {
            getOutput().println("Not enough parameters.");
        }
    }

    @Override
    protected final String generateHelpText() {
        StringBuilder sb = new StringBuilder();
        sb.append("Aspects two parameters:");
        sb.append(System.getProperty("line.separator"));
        sb.append("The first parameter is the key for the configuration value");
        sb.append(System.getProperty("line.separator"));
        sb.append("The second parameter is the new configuration value");
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "Set a configuration value";
    }

}
