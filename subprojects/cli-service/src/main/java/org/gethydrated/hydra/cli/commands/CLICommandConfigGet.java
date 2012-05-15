package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class CLICommandConfigGet extends CLICommand {

        /**
         * 
         * @param out OutputStream.
         * @param ctx Service context.
         */
        public CLICommandConfigGet(final PrintStream out, final ServiceContext ctx) {
                super(out, ctx);
                
        }

        @Override
        public final String getCommandWord() {
                return "get";
        }

        @Override
        public final String getCommandShort() {
                return getCommandWord();
        }

        @Override
        public final void executeCommand(final String[] args) {
                getOutput().printf("%s", args[0]);
        }

}
