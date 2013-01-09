package org.gethydrated.hydra.cli;

import java.io.IOException;

import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.api.message.Message;
import org.gethydrated.hydra.api.message.MessageType;
import org.gethydrated.hydra.api.service.MessageHandler;
import org.gethydrated.hydra.api.service.ServiceContext;
import org.gethydrated.hydra.api.service.SID;
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
     * @param ctx
     *            Context.
     */
    public CLIService(final ServiceContext ctx) {
        commands = new CLICommandRoot(ctx);
        commands.addSubCommand(new CLICommandEcho(ctx));
        commands.addSubCommand(new CLICommandConfig(ctx));
        commands.addSubCommand(new CLICommandService(ctx));
        commands.addSubCommand(new CLICommandShutdown(ctx));
        commands.addSubCommand(new CLICommandHelp(ctx, commands));
        ctx.subscribeEvent(InputEvent.class);
        ctx.registerMessageHandler(InputEvent.class, new MessageHandler<InputEvent>() {
            @Override
            public void handle(InputEvent message, SID sender) {
                String out =handleInputString(message.toString());
                ctx.getOutput().tell(
                        "local: " + out,
                        ctx.getSelf()
                );
                sender.tell(
                        "remote: " + out,
                        ctx.getSelf()
                );
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
     * Stops the service.
     */
    public final void stop() {

    }

}
