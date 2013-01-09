package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @author hanno
 *         Date: 08.01.13
 *         Time: 18:56
 *         To change this template use File | Settings | File Templates.
 */
public class CLICommandServiceStart extends CLICommand {


    /**
     * @param ctx Service context.
     */
    public CLICommandServiceStart(final ServiceContext ctx) {
        super(ctx);
    }

    @Override
    public String getCommandWord() {
        return "start";
    }

    @Override
    public String getCommandShort() {
        return getCommandWord();
    }

    @Override
    protected String generateHelpText() {
        return "Starts a services identify by its name or ID.";
    }

    @Override
    protected String generateShortDescr() {
        return "Starts a service.";
    }

    @Override
    public String execute(String[] args) {
        try {
            long id = getContext().startService(args[0]);
            return String.format("Service started with USID: %i", id);
        } catch (HydraException e) {
            return String.format("An exception occurred.");
        }
    }
}
