package org.gethydrated.hydra.core.cli.commands;

import java.util.List;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandConfigList extends CLICommand {

    /**
     * Constructor.
     * @param ctx
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandConfigList(final InternalHydra ctx, final CLICommand root) {
        super(ctx, root);
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
        final StringBuilder sb = new StringBuilder();
        sb.append("Aspects one parameter:");
        sb.append(System.getProperty("line.separator"));
        sb.append("The parameter is the key for the configuration value");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    @Override
    protected final String generateShortDescr() {
        return "List all configuration sub items.\n";
    }

    @Override
    public final CLIResponse execute(final String[] args) {
        final StringBuilder sb = new StringBuilder();
        String elem = null;
        if (args.length >= 1) {
            elem = args[0];
        } else {
            elem = "";
        }
        List<String> l;
        try {
            l = getHydra().getConfiguration().list(elem);
            if (l.size() > 0) {
                for (final String s : l) {
                    sb.append(s);
                    sb.append(System.getProperty("line.separator"));
                }
                sb.append(System.getProperty("line.separator"));
            } else {
                sb.append(args[0]);
                sb.append(" has no children");
                sb.append(System.getProperty("line.separator"));
            }
        } catch (final ConfigItemNotFoundException e) {
            sb.append(String.format("Configuration item %s not found", args[0]));
            sb.append(System.getProperty("line.separator"));
        } catch (final NullPointerException e) {
            sb.append("Caught Nullpointer exception. No Context defined?");
            sb.append(System.getProperty("line.separator"));
        }
        return new CLIResponse(sb.toString());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }

}
