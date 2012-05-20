package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class CLICommandConfigList extends CLICommand {

        /**
         * 
         * @param out OutputStream.
         * @param ctx Service Context.
         */
        public CLICommandConfigList(final PrintStream out, final ServiceContext ctx) {
                super(out, ctx);
                // TODO Auto-generated constructor stub
        }

        @Override
        public final String getCommandWord() {
                return "list";
        }

        @Override
        public final String getCommandShort() {
                return "l";
        }

        @Override
        protected final String generateHelpText() {
                StringBuilder sb = new StringBuilder();
                sb.append("Aspects one parameter:");
                sb.append(System.getProperty("line.separator"));
                sb.append("The parameter is the key for the configuration value");
                return sb.toString();
        }

        @Override
        protected final String generateShortDescr() {
                return "List all configuration sub items.";
        }

        @Override
        public final void executeCommand(final String[] args) {
                getOutput().printf("[%s]", args[0]);

        }

}
