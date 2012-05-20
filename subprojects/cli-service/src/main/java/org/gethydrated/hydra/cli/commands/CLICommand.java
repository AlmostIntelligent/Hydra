package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        private List<CLICommand> subCommands;

        /**
         * 
         */
        private PrintStream output;
        
        /**
         * 
         */
        private ServiceContext context;
        
        /**
         * Full text for command help.
         */
        private String helpText;
        
        /**
         * Short description of the command.
         */
        private String shortDescr;
        
        /**
         * 
         * @param out Reference to the output stream
         * @param ctx Service context.
         */
        public CLICommand(final PrintStream out, final ServiceContext ctx) {
                setOutput(out);
                setContext(ctx);
                subCommands = new LinkedList<CLICommand>();
                helpText = generateHelpText();
                shortDescr = generateShortDescr();
        }
        
        /**
         * 
         * @return OutputStream.
         */
        public final PrintStream getOutput() {
                return output;
        }

        /**
         * 
         * @param out OutputStream.
         */
        private void setOutput(final PrintStream out) {
                output = out;
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
         * @param ctx service context
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
         * @param cmd new Sub command.
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
         * @param args Array with arguments.
         */
        public abstract void executeCommand(final String[] args);
        
        /**
         * 
         */
        private void displayHelp() {
                getOutput().printf("Help for command %s", getCommandWord());
                getOutput().println();
                getOutput().println();
                getOutput().println(getShortDescription());
                getOutput().println();
                getOutput().println("Long Description: ");
                getOutput().println(getHelpText());
                getOutput().println();
                getOutput().println("List of sub commands");
                for (CLICommand cmd : subCommands) {
                        getOutput().printf("\t%s: %s", cmd.getCommandWord(), cmd.getShortDescription());
                        getOutput().println();
                }
                getOutput().println();
                getOutput().println("Type <command help for further information");
                getOutput().println();
        }

        /**
         * 
         * @param str String.
         * @return True, if the String is a sub command.
         */
        public final CLICommand isSubCommand(final String str) {
                for (CLICommand cmd : subCommands) {
                        if (cmd.getCommandWord().equalsIgnoreCase(str) || cmd.getCommandShort().equals(str)) {
                                return cmd;
                        }
                }
                return null;
        }
        
        /**
         * 
         * @param cmd
         *                The command string.
         */
        public final void parseCommand(final String cmd) {
                if (cmd.contains("\"")) {
                        String[] parts = cmd.split("\"");
                        int i = 0;
                        ArrayList<String> result = new ArrayList<String>();
                        for (i = 0; i < parts.length; i++) {
                                if (i == 0) {
                                        for (String c: parts[0].split(" ")) {
                                                result.add(c);
                                        }
                                } else {
                                        if (parts[i].trim() != "") {
                                                result.add(parts[i]);
                                        }
                                }
                        }
                } else {
                        parseCommand(cmd.split(" "));
                }
        }
        
        /**
         * 
         * @param cmds .
         */
        public final void parseCommand(final String[] cmds) {
                if (cmds[0].equalsIgnoreCase("help")) {
                        displayHelp();
                } else if (hasSubCommands()) {
                        CLICommand subCmd = isSubCommand(cmds[0]);
                        if (subCmd != null) {
                                String[] rest = new String[cmds.length - 1];
                                int i;
                                for (i = 1; i < cmds.length; i++) {
                                        rest[i - 1] = cmds[i];
                                }
                                subCmd.parseCommand(rest);
                        }
                                
                } else {
                        executeCommand(cmds);
                }
                
        }

}
