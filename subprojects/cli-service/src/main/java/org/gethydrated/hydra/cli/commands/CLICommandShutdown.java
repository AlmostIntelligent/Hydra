package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

public class CLICommandShutdown extends CLICommand {

    public CLICommandShutdown(ServiceContext ctx) {
        super(ctx);
    }

    @Override
    public String getCommandWord() {
        return "shutdown";
    }

    @Override
    public String getCommandShort() {
        return ":sd";
    }

    @Override
    protected String generateHelpText() {
        return "Hydra shutdown.";
    }

    @Override
    protected String generateShortDescr() {
        return generateHelpText();
    }

    @Override
    public void executeCommand(String[] args) {
        System.exit(0);
    }

}
