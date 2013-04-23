package org.gethydrated.hydra.core.cli.commands.registry;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;
import org.gethydrated.hydra.core.registry.UnregisterService;

/**
 * Global service unregister command.
 */
public class UnregisterGlobal extends CLICommand {
    /**
     * Constructor.
     * @param hydra parent Hydra.
     * @param root Root command.
     */
    public UnregisterGlobal(final InternalHydra hydra, final CLICommand root) {
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
        return "Deletes a registry entry with a given name.\n";
    }

    @Override
    public CLIResponse execute(final String[] args) {
        if (args.length == 1) {
            final ActorRef ref = getHydra().getActorSystem().getActor(
                    "/app/globalregistry");
            @SuppressWarnings("rawtypes")
            final Future f = ref.ask(new UnregisterService(args[0]));
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
                    "Wrong parameter count. Usage: unregister global [name]\n");
        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
