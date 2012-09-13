package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorReference;
import org.gethydrated.hydra.actors.ActorSystem;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 *
 */
public final class ActorSystemTest {

    /**
     * 
     */
    @Test
    public void testStartShutdown() {
        ActorSystem sys = ActorSystem.createTopology();

        assertFalse(sys.isTerminated());

        sys.shutdown();
        sys.awaitTermination();
        
        assertTrue(sys.isTerminated());
    }

    /**
     * 
     */
    @Test
    public void testActorCreation() {
        ActorSystem sys = ActorSystem.createTopology();
        ActorReference ref = sys.spawn(new TestActor(), "test");
        
        assertNotNull(ref);
    }

    /**
     * 
     * @author Christian Kulpa
     *
     */
    private class TestActor extends Actor {

        @Override
        public void receive(final Object m) throws Exception {
            // TODO Auto-generated method stub

        }

    }
}
