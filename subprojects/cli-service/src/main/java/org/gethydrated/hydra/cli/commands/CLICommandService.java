package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @author hanno
 *         Date: 08.01.13
 *         Time: 19:08
 *         To change this template use File | Settings | File Templates.
 */
public class CLICommandService extends CLICommand {
    /**
     * @param ctx Service context.
     */
    public CLICommandService(final ServiceContext ctx) {
        super(ctx);
        addSubCommand(new CLICommandServiceStart(ctx));
        addSubCommand(new CLICommandServiceStop(ctx));
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
    public String execute(String[] args) {
        return displayHelp();
    }
}
