package org.gethydrated.hydra.core.cli.commands;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLISubCommandDoesNotExistsException extends Exception {

    /**
     * Missing sub command.
     */
    private String subCommand;

    /**
     * 
     */
    private static final long serialVersionUID = 2958730452002967320L;

    /**
     * 
     * @param sub
     *            missing sub command.
     */
    public CLISubCommandDoesNotExistsException(final String sub) {
        setSubCommand(sub);
    }

    /**
     * 
     * @return missing subcommand.
     */
    public final String getSubCommand() {
        return subCommand;
    }

    /**
     * 
     * @param subCommand
     *            missing sub command.
     */
    public final void setSubCommand(final String subCommand) {
        this.subCommand = subCommand;
    }
}
