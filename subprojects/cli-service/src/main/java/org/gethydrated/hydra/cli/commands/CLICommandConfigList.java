package org.gethydrated.hydra.cli.commands;

import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfigList extends CLICommand {

    /**
     * 
     * @param ctx
     *            Service Context.
     */
    public CLICommandConfigList(final ServiceContext ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public final String getCommandWord() {
        return "list";
    }

    @Override
    public final String getCommandShort() {
        return "l";
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
        return "List all configuration sub items.";
    }

    @Override
    public final void execute(final String[] args) {
        if (args.length >= 1) {
            List<String> l;
            try {
                l = getContext().getConfiguration().list(args[0]);
                for (String s : l) {
                    getOutput().println(s);
                }
            } catch (ConfigItemNotFoundException e) {
                getOutput().printf("Configuration item %s not found", args[0]);
                getOutput().println();
            } catch (NullPointerException e) {
                getOutput().println("Caught Nullpointer exception. No Context defined?");
            }

        } else {
            getOutput().println("No key given.");
        }

    }

}
