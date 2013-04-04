package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.messages.ConnectTo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class CLICommandConnect extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public CLICommandConnect(InternalHydra hydra, CLICommand root) {
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
    public CLIResponse execute(String[] args) {
        String ip;
        int port;
        if (args.length == 1) {
            String[] arr = args[0].split(":");
            ip = arr[0];
            port = Integer.parseInt(arr[1]);
        } else if(args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } else {
            return new CLIResponse("Invalid number of arguments. Please use 'connect ip:port' or 'connect ip port'.\n");
        }
        try {
            getHydra().getNetKernel().connect(ip, port);
            return new CLIResponse("\n");
        } catch (Exception e) {
            e.printStackTrace();
            return new CLIResponse(String.format("An error occured: %s\n", e.getMessage()));

        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
