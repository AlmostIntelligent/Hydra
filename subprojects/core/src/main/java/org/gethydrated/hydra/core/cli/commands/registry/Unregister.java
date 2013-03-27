package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;

/**
 *
 */
public class Unregister extends CLICommand {
    /**
     * @param hydra Service hydra.
     */
    public Unregister(InternalHydra hydra, CLICommand root) {
        super(hydra, root);
        addSubCommand(new UnregisterLocal(hydra, root));
    }

    @Override
    public String getCommandWord() {
        return "unregister";
    }

    @Override
    public String getCommandShort() {
        return "unreg";
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
