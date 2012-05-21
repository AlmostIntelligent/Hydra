package org.gethydrated.hydra.cli;

import java.io.IOException;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.cli.commands.CLICommand;
import org.gethydrated.hydra.cli.commands.CLICommandConfig;
import org.gethydrated.hydra.cli.commands.CLICommandEcho;
import org.gethydrated.hydra.cli.commands.CLICommandRoot;

/**
 * CLI Service.
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLIService {

        
        /**
         * 
         */
        private CLICommand commands;

        /**
         * @param ctx
         *                Context.
         */
        public CLIService(final ServiceContext ctx) {
                commands = new CLICommandRoot(ctx);
                commands.addSubCommand(new CLICommandEcho(ctx));
                commands.addSubCommand(new CLICommandConfig(ctx));
        }

        /**
         * 
         * @param str
         *                command String
         */
        public final void handleInputString(final String str) {
                commands.parseCommand(str);
        }

        /**
         * 
         */
        public final void handleInput() {
                StringBuilder str = new StringBuilder();
                while (true) {
                        char c;
                        try {
                                c = (char) System.in.read();
                                if (c == '\n') {
                                        handleInputString(str.toString());
                                        str.delete(0, -1);
                                } else {
                                        str.append(c);
                                }
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

}
