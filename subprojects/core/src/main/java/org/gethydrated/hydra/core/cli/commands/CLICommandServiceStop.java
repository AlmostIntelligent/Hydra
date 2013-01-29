package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.core.HydraImpl;
import org.gethydrated.hydra.core.InternalHydra;

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
    public CLICommandServiceStop(final InternalHydra hydra) {
        super(hydra);
        //sidFactory = hydra.getSIDFactory();
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
    public String execute(String[] args) {
        try {
            SID sid = sidFactory.fromString(args[0]);
            getHydra().stopService(sid);
            return String.format("Service %i stopped.", sid);
        } catch (HydraException e) {
            return "Exception while stopping service";
        }
    }
}
