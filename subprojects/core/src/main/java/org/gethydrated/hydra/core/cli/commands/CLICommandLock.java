package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.messages.LockRequest;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * TEMPORARY TESTING ONLY
 */
public class CLICommandLock extends CLICommand{
    /**
     * @param hydra Service hydra.
     */
    public CLICommandLock(InternalHydra hydra, CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "lock";
    }

    @Override
    public String getCommandShort() {
        return "lock";
    }

    @Override
    protected String generateHelpText() {
        return "";
    }

    @Override
    protected String generateShortDescr() {
        return "";
    }

    @Override
    public CLIResponse execute(String[] args) {
        SIDFactory factory = getHydra().getDefaultSIDFactory();
        SID test = factory.fromString("<0:0:1>");
        ActorRef ref = getHydra().getActorSystem().getActor("/app/coordinator");
        Future f = ref.ask(new LockRequest(test));
        try {
            Object o = f.get(10, TimeUnit.SECONDS);
            return new CLIResponse(o.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return new CLIResponse("");
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
