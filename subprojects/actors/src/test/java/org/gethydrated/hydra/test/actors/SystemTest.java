package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.ActorSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 * 
 */
public final class SystemTest {

    private ActorSystem actorSystem;

    @Before
    public void setup() {
        actorSystem = ActorSystem.create();
    }

    @After
    public void teardown() throws InterruptedException {
        actorSystem.shutdown();
        actorSystem.await();
    }

    @Test
    public void testActorSystemShutdown() throws InterruptedException {
        assertFalse(actorSystem.isTerminated());

        actorSystem.shutdown();
        actorSystem.await();

        assertTrue(actorSystem.isTerminated());
    }

}
