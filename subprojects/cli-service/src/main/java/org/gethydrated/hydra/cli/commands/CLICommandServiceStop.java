package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.api.service.SIDFactory;
import org.gethydrated.hydra.api.service.ServiceContext;

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
     * @param ctx Service context.
     */
    public CLICommandServiceStop(final ServiceContext ctx) {
        super(ctx);
        sidFactory = ctx.getSIDFactory();
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
            getContext().stopService(sid);
            return String.format("Service %i stopped.", sid);
        } catch (HydraException e) {
            return "Exception while stopping service";
        }
    }
}
