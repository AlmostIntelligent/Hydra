package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;

/**
 * Service register command.
 */
public class Register extends CLICommand {
    /**
     * Constructor.
     * @param hydra parent Hydra.
     * @param root Root command.
     */
    public Register(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
        addSubCommand(new RegisterLocal(hydra, root));
        addSubCommand(new RegisterGlobal(hydra, root));
    }

    @Override
    public String getCommandWord() {
        return "register";
    }

    @Override
    public String getCommandShort() {
        return "reg";
    }

    @Override
    protected String generateHelpText() {
        return "Name registry functions";
    }

    @Override
    protected String generateShortDescr() {
        return "Name registry";
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
