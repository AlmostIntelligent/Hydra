package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * Created with IntelliJ IDEA.
 *
 * @author hanno
 *         Date: 08.01.13
 *         Time: 19:03
 *         To change this template use File | Settings | File Templates.
 */
public class CLICommandServiceStop extends CLICommand {
    private SIDFactory sidFactory;

    /**
     * @param hydra Service context.
     */
    public CLICommandServiceStop(final InternalHydra hydra, CLICommand root) {
        super(hydra, root);
        sidFactory = hydra.getDefaultSIDFactory();
    }

    @Override
    public String getCommandWord() {
        return "stop";
    }

    @Override
    public String getCommandShort() {
        return getCommandWord();
    }

    @Override
    protected String generateHelpText() {
        return "Stops a services identified by its SID";
    }

    @Override
    protected String generateShortDescr() {
        return "Stops a service";
    }

    @Override
    public CLIResponse execute(String[] args) {
        try {
            SID sid = sidFactory.fromString(args[0]);
            getHydra().stopService(sid);
            return new CLIResponse(String.format("Service %s stopped.", args[0]));
        } catch (HydraException e) {
            return new CLIResponse("Exception while stopping service");
        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
