package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public abstract class CLICommand {

    /**
         * 
         */
    private final List<CLICommand> subCommands;

    /**
         * 
         */
    private ServiceContext context;

    /**
     * Full text for command help.
     */
    private final String helpText;

    /**
     * Short description of the command.
     */
    private final String shortDescr;

    /**
     * 
     * @param ctx
     *            Service context.
     */
    public CLICommand(final ServiceContext ctx) {
        setContext(ctx);
        subCommands = new LinkedList<>();
        helpText = generateHelpText();
        shortDescr = generateShortDescr();
    }

    /**
     * 
     * @return The command word.
     */
    public abstract String getCommandWord();

    /**
     * @return The short form of the command word.
     */
    public abstract String getCommandShort();

    /**
     * 
     * @return Context of the service.
     */
    public final ServiceContext getContext() {
        return context;
    }

    /**
     * 
     * @param ctx
     *            service context
     */
    private void setContext(final ServiceContext ctx) {
        context = ctx;
    }

    /**
     * 
     * @return Help text
     */
    public final String getHelpText() {
        return helpText;
    }

    /**
     * 
     * @return Help text.
     */
    protected abstract String generateHelpText();

    /**
     * 
     * @return Short command description.
     */
    public final String getShortDescription() {
        return shortDescr;
    }

    /**
     * 
     * @return Short description
     */
    protected abstract String generateShortDescr();

    /**
     * 
     * @param cmd
     *            new Sub command.
     */
    public final void addSubCommand(final CLICommand cmd) {
        subCommands.add(cmd);
    }

    /**
     * 
     * @return True, if this command has additional sub commands.
     */
    public final Boolean hasSubCommands() {
        return subCommands.size() > 0;
    }

    /**
     * 
     * @param args
     *            Array with arguments.
     */
    public abstract String execute(final String[] args);

    /**
     * 
     * @param args
     *            Array with arguments.
     */
    private String executeSecure(final String[] args) {
        try {
            return execute(args);
        } catch (Exception e) {
            return "Caught exception in command execution!";
        }
    }

    /**
     * Displays the help text.
     */
    public final String displayHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Help for command %s - ", getCommandWord()));
        sb.append(System.getProperty("line.separator"));
        sb.append(getShortDescription());
        if (getHelpText() != "") {
            sb.append("Long Description:");
            sb.append(System.getProperty("line.separator"));
            sb.append(getHelpText());
        }
        if (hasSubCommands()) {
            sb.append("List of sub commands");
            sb.append(System.getProperty("line.separator"));
            for (CLICommand cmd : subCommands) {
                sb.append(String.format("\t%s: %s", cmd.getCommandWord(),
                        cmd.getShortDescription()));
                sb.append(System.getProperty("line.separator"));
            }
        }
        sb.append("Type '<command> -help' for further information");
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

    /**
     * 
     * @param str
     *            possible command word.
     * @return True, if "str" is a sub command.
     */
    public final Boolean hasSubCommand(final String str) {
        for (CLICommand c : subCommands) {
            if (c.testString(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if the given String is this command.
     * 
     * @param str
     *            the string
     * @return True, if the String is this command.
     */
    public final Boolean testString(final String str) {
        return getCommandWord().equalsIgnoreCase(str)
                || getCommandShort().equals(str);
    }

    /**
     * 
     * @param str
     *            String.
     * @return True, if the String is a sub command.
     * @throws CLISubCommandDoesNotExistsException .
     */
    public final CLICommand isSubCommand(final String str)
            throws CLISubCommandDoesNotExistsException {
        for (CLICommand cmd : subCommands) {
            if (cmd.testString(str)) {
                return cmd;
            }
        }
        throw new CLISubCommandDoesNotExistsException(str);
    }

    /**
     * 
     * @param cmd
     *            The command string.
     */
    public final String parse(final String cmd) {
        if (cmd.contains("\"")) {
            List<String> list = new ArrayList<String>();
            Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmd);
            while (m.find())
                list.add(m.group(1).replace("\"", ""));
            return parse(list.toArray(new String[list.size()]));
        } else {
            return parse(cmd.split(" "));
        }
    }

    /**
     *
     * @param cmds
     *            .
     */
    public final String parse(final String[] cmds) {
        if (cmds.length > 0
                && (cmds[0].equalsIgnoreCase("-help")
                        || cmds[0].equalsIgnoreCase("--h") || cmds[0]
                            .equalsIgnoreCase("-?"))) {
            return displayHelp();
        } else if (cmds.length > 0 && hasSubCommands()) {
            try {
                CLICommand subCmd = isSubCommand(cmds[0]);
                String[] rest = new String[cmds.length - 1];
                int i;
                for (i = 1; i < cmds.length; i++) {
                    rest[i - 1] = cmds[i];
                }
                return subCmd.parse(rest);
            } catch (CLISubCommandDoesNotExistsException e) {
                return String.format("No sub command: %s \n", cmds[0]);
            }
        } else {
            return executeSecure(cmds);
        }

    }

}
