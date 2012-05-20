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
                return "s";
        }

        @Override
        public final void executeCommand(final String[] args) {
                getOutput().printf("%s=%s", args[0], args[1]);
        }

        @Override
        protected final String generateHelpText() {
                StringBuilder sb = new StringBuilder();
                sb.append("Aspects two parameters:");
                sb.append(System.getProperty("line.separator"));
                sb.append("The first parameter is the key for the configuration value");
                sb.append(System.getProperty("line.separator"));
                sb.append("The second parameter is the new configuration value");
                return sb.toString();
        }

        @Override
        protected final String generateShortDescr() {
                return "Set a configuration value";
        }

}
