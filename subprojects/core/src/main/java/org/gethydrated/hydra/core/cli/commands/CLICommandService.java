package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author hanno Date: 08.01.13 Time: 19:08 To change this template use File |
 *         Settings | File Templates.
 */
public class CLICommandService extends CLICommand {
    /**
     * Constructor.
     * @param hydra
     *              Service context.
     * @param root
     *              root command.
     */
    public CLICommandService(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
        addSubCommand(new CLICommandServiceStart(hydra, root));
        addSubCommand(new CLICommandServiceStop(hydra, root));
    }

    @Override
    public String getCommandWord() {
        return "service";
    }

    @Override
    public String getCommandShort() {
        return "srv";
    }

    @Override
    protected String generateHelpText() {
        return "Service control functions";
    }

    @Override
    protected String generateShortDescr() {
        return "Service control";
    }

    @Override
    public CLIResponse execute(final String[] args) {
        return new CLIResponse(displayHelp());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
