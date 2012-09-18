package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.System;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 *
 */
public final class SystemTest {

    private System actorSystem;
    
    @Before
    public void setup() {
        actorSystem = System.createSystem();
    }
    
    @After
    public void teardown() {
        actorSystem.shutdown();
        actorSystem.await();
    }
    
    @Test
    public void testActorSystemShutdown() {
        assertFalse(actorSystem.isTerminated());
        
        actorSystem.shutdown();
        actorSystem.await();
        
        assertTrue(actorSystem.isTerminated());
    }
    
}
