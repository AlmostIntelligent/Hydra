package org.gethydrated.hydra.core.cli.commands.registry;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;
import org.gethydrated.hydra.core.registry.RegisterService;

/**
 * Global service register command.
 */
public class RegisterGlobal extends CLICommand {
    /**
     * Constructor.
     * @param hydra parent Hydra.
     * @param root Root command.
     */
    public RegisterGlobal(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "global";
    }

    @Override
    public String getCommandShort() {
        return "gl";
    }

    @Override
    protected String generateHelpText() {
        return generateShortDescr();
    }

    @Override
    protected String generateShortDescr() {
        return "Registers a SID to a given name.";
    }

    @Override
    public CLIResponse execute(final String[] args) {
        if (args.length == 2) {
            final SID sid = getHydra().getDefaultSIDFactory().fromString(
                    args[0]);
            final ActorRef ref = getHydra().getActorSystem().getActor(
                    "/app/globalregistry");
            final Future<?> f = ref.ask(new RegisterService(sid, args[1]));
            try {
                final String s = (String) f.get(15, TimeUnit.SECONDS);
                return new CLIResponse(s + "\n");
            } catch (InterruptedException | ExecutionException
                    | TimeoutException e) {
                return new CLIResponse("An error occurred: " + e.getMessage()
                        + "\n");
            }
        } else {
            return new CLIResponse(
                    "Wrong parameter count. Usage: register global [sid] [name]\n");
        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
