package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertEquals;
import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.Context;
import org.gethydrated.hydra.actors.Reference;
import org.gethydrated.hydra.actors.ActorSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class ActorTest {

    private ActorSystem actorSystem;

    @Before
    public void setup() {
        ActorSystem.createSystem("testSystem");
    }

    @After
    public void teardown() {
        actorSystem.shutdown();
        actorSystem.await();
    }

    @Test
    public void testActorClassCreation() {
        Reference actor = actorSystem.spawnActor(TestActor.class, "test");

        assertEquals(actor.getName(), "test");
    }

    @Test
    public void testActorFactoryCreation() {
        Reference actor = actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() {
                return new TestActor();
            }
        }, "test");

        assertEquals(actor.getName(), "test");
    }

    public static class TestActor implements Actor {

        @Override
        public void receive(Object message, Context ctx) throws Exception {
        }

        @Override
        public void onStart(Context ctx) throws Exception {
        }

        @Override
        public void onStop(Context ctx) throws Exception {
        }

    }
}
