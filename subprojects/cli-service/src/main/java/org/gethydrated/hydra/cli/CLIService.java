package org.gethydrated.hydra.cli;

import java.io.IOException;

import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.cli.commands.*;
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
    private final Logger log = LoggerFactory.getLogger(CLIService.class);

    /**
     * 
     */
    private final CLICommand commands;

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
        commands.addSubCommand(new CLICommandService(ctx));
        commands.addSubCommand(new CLICommandShutdown(ctx));
        commands.addSubCommand(new CLICommandHelp(ctx, commands));
        ctx.subscribeEvent(InputEvent.class);
        ctx.registerMessageHandler(InputEvent.class, new MessageHandler<InputEvent>() {
            @Override
            public void handle(InputEvent message) {
                String str = handleInputString(message.toString());
                System.out.println(str);
            }
        });
        log.info("CLI Service initialised.");
    }

    /**
     * 
     * @param str
     *            command String
     */
    public final String handleInputString(final String str) {
        return commands.parse(str);
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
