package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * A simple echo command, simply prints out what comes in.
 * 
 * @author Hanno Sternberg
 *
 */
public class CLICommandEcho extends CLICommand {

        /**
         * 
         * @param out OutputStream.
         * @param ctx Service context.
         */
        public CLICommandEcho(final PrintStream out, final ServiceContext ctx) {
                super(out, ctx);
        }

        @Override
        public final String getCommandWord() {
                return "echo";
        }

        @Override
        public final String getCommandShort() {
                return ":e";
        }

        @Override
        public final void executeCommand(final String[] args) {
                int i = 0;
                for (i = 0; i < args.length - 1; i++) {
                        getOutput().printf("%s ", args[i]);
                }
                getOutput().println(args[args.length - 1]);
        }

}
