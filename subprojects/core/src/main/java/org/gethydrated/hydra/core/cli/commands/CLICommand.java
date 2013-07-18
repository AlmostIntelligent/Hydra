package org.gethydrated.hydra.core.cli.commands;

import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.cli.CLIResponse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private InternalHydra hydra;

    /**
     * Full text for command help.
     */
    private final String helpText;

    /**
     * Short description of the command.
     */
    private final String shortDescr;

    /**
     * Current node too execute commands on. Id 0 is always the local node.
     */
    private int currentNodeId;

    private CLICommand root;

    /**
     * 
     * @param hydra
     *            Service hydra.
     * @param root
     *            cli root command.
     */
    public CLICommand(final InternalHydra hydra, final CLICommand root) {
        setHydra(hydra);
        setRoot(root);
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
    public final InternalHydra getHydra() {
        return hydra;
    }

    /**
     * 
     * @param hydra
     *            service hydra
     */
    private void setHydra(final InternalHydra hydra) {
        this.hydra = hydra;
    }

    private void setRoot(final CLICommand root) {
        if (root != null) {
            this.root = root;
        } else {
            this.root = this;
        }
    }

    /**
     * Returns the root command.
     * @return root command.
     */
    protected CLICommand getRootCommand() {
        return root;
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
     * @return cli response.
     */
    public abstract CLIResponse execute(final String[] args);

    /**
     * 
     * @param args
     *            Array with arguments.
     */
    private CLIResponse executeSecure(final String[] args) {
        try {
            return execute(args);
        } catch (final Exception e) {
            e.printStackTrace();
            return new CLIResponse("Caught exception in command execution! "
                    + e);
        }
    }

    /**
     * Displays the help text.
     * @return result.
     */
    public final String displayHelp() {
        final StringBuilder sb = new StringBuilder();
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
            for (final CLICommand cmd : subCommands) {
                sb.append(String.format("\t%s: %s", cmd.getCommandWord(),
                        cmd.getShortDescription()));
                sb.append(System.getProperty("line.separator"));
            }
        }
        sb.append("Type '<command> -help' for further information");
        sb.append(System.getProperty("line.separator"));
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
        for (final CLICommand c : subCommands) {
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
        for (final CLICommand cmd : subCommands) {
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
     * @return cli response.
     */
    public final CLIResponse parse(final String cmd) {
        if (cmd.contains("\"")) {
            final List<String> list = new ArrayList<>();
            final Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*")
                    .matcher(cmd);
            while (m.find()) {
                list.add(m.group(1).replace("\"", ""));
            }
            return parse(list.toArray(new String[list.size()]), cmd);
        } else {
            return parse(cmd.split(" "), cmd);
        }
    }

    /**
     * 
     * @param cmds .
     * @param input .
     * @return cli response.
     */
    public final CLIResponse parse(final String[] cmds, final String input) {
        if (cmds.length > 0
                && (cmds[0].equalsIgnoreCase("-help")
                        || cmds[0].equalsIgnoreCase("--h") || cmds[0]
                            .equalsIgnoreCase("-?"))) {
            return new CLIResponse(displayHelp());
        } else if (cmds.length > 0 && hasSubCommands()) {
            try {
                final CLICommand subCmd = isSubCommand(cmds[0]);
                if (!isNodeLocal() && !subCmd.localOnly()) {
                    final ActorRef ref = getHydra().getActorSystem().getActor(
                            "/app/nodes/" + getCurrentNodeId());
                    ref.tell(new InputEvent(input, null), null);
                    // return (CLIResponse)
                    // f.get(getHydra().getConfiguration().getInteger("cli.distributed-timeout"),
                    // TimeUnit.SECONDS);
                    return new CLIResponse("");
                } else {
                    final String[] rest = new String[cmds.length - 1];
                    int i;
                    for (i = 1; i < cmds.length; i++) {
                        rest[i - 1] = cmds[i];
                    }
                    return subCmd.parse(rest, input);
                }
            } catch (final CLISubCommandDoesNotExistsException e) {
                return new CLIResponse(String.format("No sub command: %s \n",
                        cmds[0]));
            }
        } else {
            return executeSecure(cmds);
        }
    }

    /**
     * Returns if the command is local only.
     * @return true if local only.
     */
    protected abstract boolean localOnly();

    /**
     * Sets the current operating node of the cli.
     * @param id node id.
     */
    protected void setCurrentNodeId(final int id) {
        currentNodeId = id;
    }

    /**
     * Returns the current operating node of the cli.
     * @return node id.
     */
    protected int getCurrentNodeId() {
        return currentNodeId;
    }

    /**
     * Returns if the current node is the local node.
     * @return true if local.
     */
    protected boolean isNodeLocal() {
        return (0 == currentNodeId);
    }

}
