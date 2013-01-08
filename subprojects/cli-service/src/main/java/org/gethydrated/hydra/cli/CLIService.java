package org.gethydrated.hydra.cli;

import java.io.IOException;

import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.cli.commands.CLICommand;
import org.gethydrated.hydra.cli.commands.CLICommandConfig;
import org.gethydrated.hydra.cli.commands.CLICommandEcho;
import org.gethydrated.hydra.cli.commands.CLICommandHelp;
import org.gethydrated.hydra.cli.commands.CLICommandRoot;
import org.gethydrated.hydra.cli.commands.CLICommandShutdown;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger log = LoggerFactory.getLogger(CLIService.class);

    /**
     * 
     */
    private CLICommand commands;

    /**
     * 
     */
    private volatile boolean running;

    /**
     * @param ctx
     *            Context.
     */
    public CLIService(final ServiceContext ctx) {
        running = true;
        commands = new CLICommandRoot(ctx);
        commands.addSubCommand(new CLICommandEcho(ctx));
        commands.addSubCommand(new CLICommandConfig(ctx));
        commands.addSubCommand(new CLICommandShutdown(ctx));
        commands.addSubCommand(new CLICommandHelp(ctx, commands));
        log.info("CLI Service initialised.");
    }

    /**
     * 
     * @param str
     *            command String
     */
    public final void handleInputString(final String str) {
        commands.parse(str);
    }

    /**
         * 
         */
    public final void handleInput() {
        StringBuilder str = new StringBuilder();
        while (running) {

            try {
                char c;
                int i;
                if ((i = System.in.read()) == -1) {
                    running = false;
                } else {
                    c = (char) i;
                    switch (c) {
                    case '\r':
                        break;
                    case '\n':
                        handleInputString(str.toString());
                        str.setLength(0);
                        break;
                    default:
                        str.append(c);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops the service.
     */
    public final void stop() {
        running = false;
    }

}
