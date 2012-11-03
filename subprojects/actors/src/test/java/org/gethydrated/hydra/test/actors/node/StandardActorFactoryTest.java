package org.gethydrated.hydra.test.actors.node;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.gethydrated.hydra.actors.Actor;
import org.gethydrated.hydra.actors.ActorFactory;
import org.gethydrated.hydra.actors.Context;
import org.gethydrated.hydra.actors.node.StandardActorFactory;
import org.junit.Test;

/**
 * Tests instantiation of different inner and nested classes.
 * 
 * @author Christian Kulpa
 * 
 */
public class StandardActorFactoryTest {

    /**
     * 'Normal' public static nested class.
     * 
     * @throws Exception
     *             on failure
     */
    @Test
    public void testStaticClassCreation() throws Exception {
        ActorFactory af = new StandardActorFactory(StaticActor.class);

        Actor ac = af.create();

        assertNotNull(ac);
        assertTrue(ac instanceof StaticActor);
    }

    /**
     * Inner class. Can't be instantiated by this factory due to the lack of a
     * reference to the parent class.
     * 
     * @throws Exception
     *             (expected)NoSuchMethodException no suitable constructor can
     *             be found.
     */
    @Test(expected = NoSuchMethodException.class)
    public void testNonStaticClassCreation() throws Exception {
        ActorFactory af = new StandardActorFactory(NonStaticActor.class);

        Actor ac = af.create();

        assertNotNull(ac);
    }

    /**
     * Private static class. Can't be instantiated due to visibly.
     * 
     * @throws Exception
     *             (expected)NoSuchMethodException no suitable constructor can
     *             be found.
     */
    @Test(expected = NoSuchMethodException.class)
    public void testPrivateClassCreation() throws Exception {
        ActorFactory af = new StandardActorFactory(PrivateActor.class);

        Actor ac = af.create();

        assertNotNull(ac);
    }

    /**
     * Tests if subsequent calls to create() will return different instances of
     * the given class.
     * 
     * @throws Exception
     *             on failure
     */
    @Test
    public void testInstanceNotEqual() throws Exception {
        ActorFactory af = new StandardActorFactory(StaticActor.class);

        Actor ac1 = af.create();
        Actor ac2 = af.create();

        assertNotNull(ac1);
        assertNotNull(ac2);
        assertTrue(ac1 instanceof StaticActor);
        assertTrue(ac2 instanceof StaticActor);
        assertFalse(ac1 == ac2);
    }

    /**
     * Public static test class.
     * @author Christian Kulpa
     *
     */
    public static class StaticActor extends Actor {

        @Override
        public void onReceive(Object message, Context ctx) throws Exception {
        }
    }

    /**
     * Public non-static test class.
     * @author Christian Kulpa
     *
     */
    public class NonStaticActor extends Actor {

        @Override
        public void onReceive(Object message, Context ctx) throws Exception {
        }

    }

    /**
     * Private static test class.
     * @author Christian Kulpa
     *
     */
    private static class PrivateActor extends Actor {

        @Override
        public void onReceive(Object message, Context ctx) throws Exception {
        }
    }
}
