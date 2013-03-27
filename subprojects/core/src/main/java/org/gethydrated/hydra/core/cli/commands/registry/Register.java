package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;

/**
 *
 */
public class Register extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public Register(InternalHydra hydra, CLICommand root) {
        super(hydra, root);
        addSubCommand(new RegisterLocal(hydra, root));
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
    public CLIResponse execute(String[] args) {
        return new CLIResponse(displayHelp());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
