package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.ActorSystem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 *
 */
public final class ActorBasicTest {

    /**
     * 
     */
    private ActorSystem topology;

    /**
     * 
     */
    @Before
    public void setup() {
        topology = ActorSystem.createTopology();
    }

    /**
     * 
     */
    @After
    public void teardown() {
        topology.shutdown();
    }

    /**
     * 
     */
    @Test
    public void testActorCreation() {
    }

}
