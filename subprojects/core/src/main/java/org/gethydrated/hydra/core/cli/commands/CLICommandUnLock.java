package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.messages.LockRelease;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * TEMPORARY TESTING ONLY
 */
public class CLICommandUnLock extends CLICommand{
    /**
     * @param hydra Service hydra.
     */
    public CLICommandUnLock(InternalHydra hydra, CLICommand root) {
        super(hydra, root);
    }

    @Override
    public String getCommandWord() {
        return "unlock";
    }

    @Override
    public String getCommandShort() {
        return "unlock";
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
        Future f = ref.ask(new LockRelease(test));
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
