package org.gethydrated.hydra.actors.actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.api.event.InputEvent;
import org.slf4j.Logger;

/**
 * Standard input actor.
 * 
 * @author Christian Kulpa
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class StdInActor extends Actor {

    private volatile boolean running = true;

    private final Logger logger = getLogger(StdInActor.class);

    @Override
    public void onReceive(final Object message) throws Exception {
        if (message instanceof String) {
            getSystem().getEventStream().publish(
                    new InputEvent((String) message, getSelf().toString()));
        }
    }

    @Override
    public void onStart() {
        final Runnable r = new Runnable() {

            @Override
            public void run() {
                final BufferedReader in = new BufferedReader(
                        new InputStreamReader(System.in));
                while (running) {
                    String line;
                    try {
                        line = in.readLine();
                        if (line == null) {
                            running = false;
                        } else {
                            if (!line.isEmpty()) {
                                getSelf().tell(line, null);
                            }
                        }
                    } catch (final IOException e) {
                        logger.warn("Error reading system.in.", e);
                        running = false;
                    }
                }
            }

        };
        final Thread t = new Thread(r);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void onStop() {
        running = false;
    }
}
