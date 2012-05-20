package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno
 *
 */
public class CLICommandRoot extends CLICommand {

        /**
         * 
         * @param out .
         * @param ctx .
         */
        public CLICommandRoot(final PrintStream out, final ServiceContext ctx) {
                super(out, ctx);
        }

        @Override
        public final String getCommandWord() {
                return "root";
        }

        @Override
        public final String getCommandShort() {
                return getCommandWord();
        }

        @Override
        public final void executeCommand(final String[] args) {
                getOutput().println("");

        }

        @Override
        protected final String generateHelpText() {
                return "";
        }

        @Override
        protected final String generateShortDescr() {
                return "CLI Service";
        }

}
