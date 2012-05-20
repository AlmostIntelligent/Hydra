package org.gethydrated.hydra.cli.commands;

import java.io.PrintStream;

import org.gethydrated.hydra.api.service.ServiceContext;

/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public class CLICommandConfig extends CLICommand {

        /**
         * 
         */
        
        
        /**
         * 
         * @param out OutputStream.
         * @param ctx Service context.
         */
        public CLICommandConfig(final PrintStream out, final ServiceContext ctx) {
                super(out, ctx);
                addSubCommand(new CLICommandConfigSet(out, ctx));
                addSubCommand(new CLICommandConfigGet(out, ctx));
                addSubCommand(new CLICommandConfigList(out, ctx));
        }

        @Override
        public final String getCommandWord() {
                return "configuration";
        }

        @Override
        public final String getCommandShort() {
                return "cfg";
        }

        @Override
        public void executeCommand(final String[] args) {
                

        }

        @Override
        protected final String generateHelpText() {
                StringBuilder sb = new StringBuilder();
                sb.append(System.getProperty("line.separator"));
                return sb.toString();
        }

        @Override
        protected final String generateShortDescr() {
                return "Access configuration";
        }

}
