package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;
import org.gethydrated.hydra.core.registry.RegisterService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class RegisterGlobal extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public RegisterGlobal(InternalHydra hydra, CLICommand root) {
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
    public CLIResponse execute(String[] args) {
        if(args.length == 2) {
            SID sid = getHydra().getDefaultSIDFactory().fromString(args[0]);
            ActorRef ref = getHydra().getActorSystem().getActor("/app/globalregistry");
            Future f = ref.ask(new RegisterService(sid, args[1]));
            try {
                String s = (String) f.get(15, TimeUnit.SECONDS);
                return new CLIResponse(s+ "\n");
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                return new CLIResponse("An error occurred: " + e.getMessage() + "\n");
            }
        } else {
            return new CLIResponse("Wrong parameter count. Usage: register global [sid] [name]\n");
        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}