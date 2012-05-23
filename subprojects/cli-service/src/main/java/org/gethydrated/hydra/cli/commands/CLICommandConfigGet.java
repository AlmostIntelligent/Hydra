package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.service.ServiceContext;

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
     */
    public CLICommandConfigGet(final ServiceContext ctx) {
        super(ctx);

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
    public final void executeCommand(final String[] args) {
        if (args.length >= 1) {
            try {
                getOutput().printf("%s",
                        getContext().getConfigurationGetter().get(args[0]));
            } catch (ConfigItemNotFoundException e) {
                getOutput().printf("Configuration item %s not found", args[0]);
            }
        } else {
            getOutput().print("No key given.");
        }
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
