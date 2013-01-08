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
    private final CLICommand cmdRoot;

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
    public final String execute(final String[] args) {
        if (args.length == 0) {
            return displayHelp();
        } else {
            return findHelp(args, cmdRoot);
        }
    }

    /**
     * 
     * @param args
     *            Argument list.
     * @param root
     *            Command root.
     */
    private String findHelp(final String[] args, final CLICommand root) {
        if (args.length > 0) {
            CLICommand sub;
            try {
                sub = root.isSubCommand(args[0]);
                if (sub.hasSubCommand(args[0])) {
                    String[] arg = new String[args.length - 1];
                    System.arraycopy(args, 1, arg, 0, args.length - 1);
                    return findHelp(arg, sub);
                } else {
                    return sub.displayHelp();
                }
            } catch (CLISubCommandDoesNotExistsException e) {
                return String.format("command %s does not exist.", args[0]);
            }
        } else {
            return root.displayHelp();
        }
    }

}

