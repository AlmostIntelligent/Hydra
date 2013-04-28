package org.gethydrated.hydra.core.cli.commands;

import java.io.IOException;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 *
 */
public class CLICommandPort extends CLICommand {
    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandPort(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "port";
    }

    @Override
    public String getCommandShort() {
        return "p";
    }

    @Override
    protected String generateHelpText() {
        return "Shows the current port used for hydra node connections.\n"
                + "If used with an integer parameter, this will set a new port.\n"
                + "If the port is set to 0, no new connections will be established.\n";
    }

    @Override
    protected String generateShortDescr() {
        return "Hydra port informations\n";
    }

    @Override
    public CLIResponse execute(final String[] args) {

        if (args.length == 0) {
            try {
                final int port = getHydra().getConfiguration().getInteger(
                        "network.port");
                return new CLIResponse(
                        String.format("Current port: %d\n", port));
            } catch (final ConfigItemNotFoundException e) {
                return new CLIResponse(String.format("An error occured: %s\n",
                        e.getMessage()));
            }
        } else if (args.length == 1) {
            final int port = Integer.parseInt(args[0]);
            getHydra().getConfiguration().setInteger("network.port", port);
            try {
                getHydra().getNetKernel().bind(port);
            } catch (final IOException e) {
                return new CLIResponse(String.format("An error occured: %s\n",
                        e.getMessage()));
            }
            return new CLIResponse(String.format("Port set to: %d\n", port));
        } else {
            return new CLIResponse("Invalid number of arguments!\n");
        }
    }

    @Override
    protected boolean localOnly() {
        return true;
    }
}
