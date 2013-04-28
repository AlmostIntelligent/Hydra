package org.gethydrated.hydra.core.cli.commands.registry;

import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.gethydrated.hydra.core.cli.commands.CLICommand;

/**
 * Service look up command.
 */
public class WhereIs extends CLICommand {
    /**
     * Constructor.
     * @param hydra parent Hydra.
     * @param root Root command.
     */
    public WhereIs(final InternalHydra hydra, final CLICommand root) {
        super(hydra, root);
        addSubCommand(new WhereIsLocal(hydra, root));
        addSubCommand(new WhereIsGlobal(hydra, root));
    }

    @Override
    public String getCommandWord() {
        return "whereis";
    }

    @Override
    public String getCommandShort() {
        return "wi";
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
        return new CLIResponse(generateHelpText());
    }

    @Override
    protected boolean localOnly() {
        return false;
    }
}
