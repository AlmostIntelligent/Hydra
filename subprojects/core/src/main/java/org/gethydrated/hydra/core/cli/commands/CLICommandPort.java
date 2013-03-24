package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 *
 */
public class CLICommandPort extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public CLICommandPort(InternalHydra hydra, CLICommand root) {
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
        return "Shows the current port used for hydra node connections.\n" +
                "If used with an integer parameter, this will set a new port." +
                "If the port is set to 0, no new connections will be established.";
    }

    @Override
    protected String generateShortDescr() {
        return "Hydra port informations";
    }

    @Override
    public CLIResponse execute(String[] args) {

        if(args.length == 0) {
            try {
                int port = getHydra().getConfiguration().getInteger("network.port");
                return new CLIResponse(String.format("Current port: %d\n", port));
            } catch (ConfigItemNotFoundException e) {
                return new CLIResponse(String.format("An error occured: %s\n", e.getMessage()));
            }
        } else if(args.length == 1) {
            int port = Integer.parseInt(args[0]);
            ActorRef connector = getHydra().getActorSystem().getActor("/app/connector");
            getHydra().getConfiguration().setInteger("network.port", port);
            connector.tell("portchanged", null);
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
