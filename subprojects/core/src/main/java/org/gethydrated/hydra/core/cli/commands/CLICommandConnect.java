package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 *
 */
public class CLICommandConnect extends CLICommand {
    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandConnect(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "connect";
    }

    @Override
    public String getCommandShort() {
        return "c";
    }

    @Override
    protected String generateHelpText() {
        return "Connects to another hydra node at the given ip and port\n";
    }

    @Override
    protected String generateShortDescr() {
        return "Connects to another hydra node.\n";
    }

    @Override
    public CLIResponse execute(final String[] args) {
        String ip;
        int port;
        if (args.length == 1) {
            final String[] arr = args[0].split(":");
            ip = arr[0];
            port = Integer.parseInt(arr[1]);
        } else if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            return new CLIResponse(
                    "Invalid number of arguments. Please use 'connect ip:port' or 'connect ip port'.\n");
        }
        try {
            getHydra().getNetKernel().connect(ip, port);
            return new CLIResponse("Connected to " + ip + ":" + port + "\n");
        } catch (final Exception e) {
            if (e.getMessage() != null && e.getMessage().equals("Concurrent connection attempt.")) {
                return new CLIResponse("Connected to " + ip + ":" + port + "\n");
            }
            e.printStackTrace();
            return new CLIResponse(String.format("An error occured: %s\n",
                    e.getMessage()));

        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
