package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;

/**
 * Service unregister command.
 */
public class Unregister extends CLICommand {
    /**
     * Constructor.
     * @param hydra parent Hydra.
     * @param root Root command.
     */
    public Unregister(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
        addSubCommand(new UnregisterLocal(hydra, root));
        addSubCommand(new UnregisterGlobal(hydra, root));
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
    public CLIResponse execute(final String[] args) {
        return new CLIResponse(displayHelp());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
