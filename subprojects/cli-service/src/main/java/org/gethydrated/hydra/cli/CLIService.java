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
                commands = new CLICommandRoot(System.out, ctx);
                commands.addSubCommand(new CLICommandEcho(System.out, ctx));
                commands.addSubCommand(new CLICommandConfig(System.out, ctx));
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
                                        commands.parseCommand(str.toString());
                                        str.delete(0, -1);
                                } else {
                                        str.append(c);
                                }
                        } catch (IOException e) {
                                
                        }
                }
        }

}
