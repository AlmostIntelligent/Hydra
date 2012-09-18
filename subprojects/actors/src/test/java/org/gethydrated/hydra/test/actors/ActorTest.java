package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertEquals;
import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.Reference;
import org.gethydrated.hydra.actors.System;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 *
 */
public final class ActorTest {

    private System actorSystem;
    
    @Before
    public void setup() {
        System.createSystem("testSystem");
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
            }}, "test");
        
        assertEquals(actor.getName(), "test");
    }
    
    public static class TestActor implements Actor {

        @Override
        public void receive(Object message) throws Exception {
            // TODO Auto-generated method stub
            
        }
        
    }
}
