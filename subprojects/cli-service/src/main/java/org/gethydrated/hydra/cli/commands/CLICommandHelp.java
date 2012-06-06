package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLICommandHelp extends CLICommand {

    /**
     * Root command.
     */
    private CLICommand cmdRoot;

    /**
     * 
     * @param ctx
     *            Service context.
     * @param root
     *            Command root.
     */
    public CLICommandHelp(final ServiceContext ctx, final CLICommand root) {
        super(ctx);
        cmdRoot = root;
    }

    @Override
    public final String getCommandWord() {
        return "help";
    }

    @Override
    public final String getCommandShort() {
        return ":h";
    }

    @Override
    protected final String generateHelpText() {
        return "Syntax: help <command> (<subcommand>).";
    }

    @Override
    protected final String generateShortDescr() {
        return "Displays help to a given command.";
    }

    @Override
    public final void execute(final String[] args) {
        if (args.length == 0) {
            displayHelp();
        } else {
            findHelp(args, cmdRoot);
        }
    }

    /**
     * 
     * @param args
     *            Argument list.
     * @param root
     *            Command root.
     */
    private void findHelp(final String[] args, final CLICommand root) {
        if (args.length > 0) {
            CLICommand sub;
            try {
                sub = root.isSubCommand(args[0]);
                if (sub.hasSubCommand(args[0])) {
                    String[] arg = new String[args.length - 1];
                    for (int i = 1; i < args.length; i++) {
                        arg[i - 1] = args[i];
                    }
                    findHelp(arg, sub);
                } else {
                    sub.displayHelp();
                }
            } catch (CLISubCommandDoesNotExistsException e) {
                getOutput().printf("command %s does not exist.", args[0]);
                getOutput().println();
            }
        } else {
            root.displayHelp();
        }
    }

}

