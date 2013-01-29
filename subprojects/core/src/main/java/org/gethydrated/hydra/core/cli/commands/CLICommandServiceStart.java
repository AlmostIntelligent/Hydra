package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.InternalHydra;

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
    public CLICommandServiceStart(final InternalHydra ctx) {
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
            SID id = getHydra().startService(args[0]);
            return "Service started with SID: " + id.toString();
        } catch (HydraException e) {
            return String.format("An exception occurred.");
        }
    }
}
