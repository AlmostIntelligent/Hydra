package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.gethydrated.hydra.actors.ActorReference;
import org.gethydrated.hydra.actors.Message;
import org.junit.Test;

/**
 * 
 * @author Christian Kulpa
 *
 */
public final class MessageTest {

    /**
     * 
     */
    @Test
    public void testMessage() {
        ActorReference t = new ActorReference();
        ActorReference s = new ActorReference();
        Message m = new Message("test", t, s);
        assertEquals(m.getMessage(), "test");
        assertEquals(m.getTarget(), t);
        assertEquals(m.getSender(), s);
        assertEquals(m.getId(), new Integer(0));
    }

    /**
     * 
     */
    @Test
    public void testMessageWithId() {
        ActorReference t = new ActorReference();
        ActorReference s = new ActorReference();
        Integer id = 12;
        Message m = new Message("test", t, s, id);
        assertEquals(m.getMessage(), "test");
        assertEquals(m.getTarget(), t);
        assertEquals(m.getSender(), s);
        assertEquals(m.getId(), id);
    }
    
    /**
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSenderNull() {
        Message m = new Message(null, new ActorReference(), null);
        assertNotNull(m);
    }

    /**
     * 
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTargetNull() {
        Message m = new Message(null, null, new ActorReference());
        assertNotNull(m);
    }
}
