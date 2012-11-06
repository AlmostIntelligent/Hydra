package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertEquals;
import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.ActorRef;
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
        actorSystem = ActorSystem.create();
    }

    @After
    public void teardown() {
        actorSystem.shutdown();
        actorSystem.await();
    }

    @Test
    public void testActorClassCreation() {
        ActorRef actor = actorSystem.spawnActor(TestActor.class, "test");

        assertEquals(actor.getName(), "test");
    }

    @Test
    public void testActorFactoryCreation() {
        ActorRef actor = actorSystem.spawnActor(new ActorFactory() {
            @Override
            public Actor create() {
                return new TestActor();
            }
        }, "test");

        assertEquals(actor.getName(), "test");
    }

    public static class TestActor extends Actor {

        @Override
        public void onReceive(Object message) throws Exception {
        }

    }
}
