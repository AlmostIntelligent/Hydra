package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.HydraException;
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
    /**
     * @param ctx Service context.
     */
    public CLICommandServiceStop(final ServiceContext ctx) {
        super(ctx);
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
        return "Stops a services identified by its USID";
    }

    @Override
    protected String generateShortDescr() {
        return "Stops a service";
    }

    @Override
    public String execute(String[] args) {
        try {
            long usid = Long.parseLong(args[1]);
            getContext().stopService(usid);
            return String.format("Service %i stopped.", usid);
        } catch (HydraException e) {
            return "Exception while stopping service";
        }
    }
}
