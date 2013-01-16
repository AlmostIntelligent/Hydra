package org.gethydrated.hydra.cli;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
     * Root command, parent of all sub commands
     */
    private final CLICommand commands;

    /**
     * Dictionary for variables
     */
    private Map<String, String> variable_dict;

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

        variable_dict = new HashMap<String, String>();

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
        String command = null;
        String var = null;
        if (str.trim().startsWith("$")){
            /* Got variable, handle it*/
            var = str.substring(1, str.indexOf('=')).trim();    //1. extract variable name
            command = str.substring(str.indexOf('=')+1).trim(); //2. extract command
        } else {
            command = str;
        }
        if (command.contains("$")) {
            /* Variables inside the command, replace 'em*/
            for(String k : variable_dict.keySet()) {
                command = command.replace("$"+k, variable_dict.get(k));
            }
        }
        if (var != null) {
            /* Assign result to variable */
            String result = commands.parse(command);
            variable_dict.put(var, result);
            return result;
        } else {
            /* No need for assignment  */
            return commands.parse(command);
        }
    }

    /**
     * Stops the service.
     */
    public final void stop() {

    }

}
