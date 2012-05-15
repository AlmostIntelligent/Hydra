package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class CLICommandConfigSet extends CLICommand {

        /**
         * 
         * @param out OutputStream.
         * @param ctx Service context.
         */
        public CLICommandConfigSet(final PrintStream out, final ServiceContext ctx) {
                super(out, ctx);
                
        }

        @Override
        public final String getCommandWord() {
                return "set";
        }

        @Override
        public final String getCommandShort() {
                return getCommandWord();
        }

        @Override
        public final void executeCommand(final String[] args) {
                getOutput().printf("%s=%s", args[0], args[1]);
        }

}
