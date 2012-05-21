package org.gethydrated.hydra.cli.commands;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno
 *
 */
public class CLICommandRoot extends CLICommand {

        /**
         * 
         * @param ctx .
         */
        public CLICommandRoot(final ServiceContext ctx) {
                super(ctx);
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
