package org.gethydrated.hydra.core.cli;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorRef;
import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.core.InternalHydra;
import org.gethydrated.hydra.core.Version;
import org.gethydrated.hydra.core.cli.commands.*;
import org.gethydrated.hydra.core.cli.commands.registry.Register;
import org.gethydrated.hydra.core.cli.commands.registry.Unregister;
import org.gethydrated.hydra.core.cli.commands.registry.WhereIs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * CLI Service.
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class CLIService extends Actor {
    
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

    private ActorRef output;

    /**
     * @param hydra
     *            Handle to hydra system implementation.
     */
    public CLIService(final InternalHydra hydra) {
        commands = new CLICommandRoot(hydra);
        commands.addSubCommand(new CLICommandEcho(hydra, commands));
        commands.addSubCommand(new CLICommandConfig(hydra, commands));
        commands.addSubCommand(new CLICommandService(hydra, commands));
        commands.addSubCommand(new CLICommandShutdown(hydra, commands));
        commands.addSubCommand(new CLICommandNodes(hydra, commands));
        commands.addSubCommand(new CLICommandPort(hydra, commands));
        commands.addSubCommand(new CLICommandLocal(hydra, commands));
        commands.addSubCommand(new CLICommandNode(hydra, commands));
        commands.addSubCommand(new CLICommandConnect(hydra, commands));
        commands.addSubCommand(new Register(hydra, commands));
        commands.addSubCommand(new Unregister(hydra, commands));
        commands.addSubCommand(new WhereIs(hydra, commands));
        commands.addSubCommand(new CLICommandHelp(hydra, commands));

        variable_dict = new HashMap<>();
    }

    /**
     * 
     * @param str
     *            command String
     */
    public final CLIResponse handleInputString(final String str) {
        String command;
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
            CLIResponse result = commands.parse(command);
            variable_dict.put(var, result.getVar());
            return result;
        } else {
            /* No need for assignment  */
            return commands.parse(command);
        }
    }

    /**
     * Stops the service.
     */
    @Override
    public final void onStop() {
        getSystem().getEventStream().unsubscribe(getSelf());
    }

    @Override
    public final void onStart() {
        try {
        output = getContext().getActor("/sys/out");
        getSystem().getEventStream().subscribe(getSelf(), InputEvent.class);
        output.tell("Hydra <" + Version.getVersionString() + ">  (Use shutdown or :sd to quit)\n", null);
        log.info("CLI Service initialised.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof InputEvent) {
            handle((InputEvent) message);
        } else if (message instanceof CLIResponse) {
            handleCR((CLIResponse) message);
        }
    }

    private void handleCR(CLIResponse message) {
        output.tell(message.getOutput(), getSelf());
    }

    public void handle(InputEvent message) {
        CLIResponse out = handleInputString(message.getInput());
        if(message.getSource() == null || message.getSource().equals("/sys/in")) {
            output.tell(out.getOutput(), getSelf());
        } else {
            System.out.println(message.getSource());
            ActorRef ref = getContext().getActor(message.getSource());
            ref.tell(out, getSelf());
        }
    }
}
