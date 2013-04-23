package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Global service lookup command.
 */
public class WhereIsGlobal extends CLICommand {
    /**
     * Constructor.
     * @param hydra parent Hydra.
     * @param root Root command.
     */
    public WhereIsGlobal(final InternalHydra hydra, final CLICommand root) {
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
        return "Locates a registered SID by name.\n";
    }

    @Override
    public CLIResponse execute(final String[] args) {
        if (args.length == 1) {
            final ActorRef ref = getHydra().getActorSystem().getActor(
                    "/app/globalregistry");
            @SuppressWarnings("rawtypes")
            final Future f = ref.ask(args[0]);
            try {
                final SID s = (SID) f.get(15, TimeUnit.SECONDS);
                return new CLIResponse(s.toString() + "\n");
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
