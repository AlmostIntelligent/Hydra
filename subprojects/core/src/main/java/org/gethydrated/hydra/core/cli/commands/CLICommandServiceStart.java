package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.api.HydraException;
import org.gethydrated.hydra.api.service.SID;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author hanno Date: 08.01.13 Time: 18:56 To change this template use File |
 *         Settings | File Templates.
 */
public class CLICommandServiceStart extends CLICommand {

    /**
     * Constructor.
     * @param ctx
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandServiceStart(final InternalHydra ctx, final CLICommand root) {
        super(ctx, root);
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
    public CLIResponse execute(final String[] args) {
        try {
            final SID id = getHydra().startService(args[0]);
            return new CLIResponse(String.format(
                    "Service started with SID: %s", id.toString()));
        } catch (final HydraException e) {
            return new CLIResponse(String.format("An exception occurred:"
                    + e.getMessage()));
        }
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
