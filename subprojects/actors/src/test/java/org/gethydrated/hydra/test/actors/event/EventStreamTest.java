package org.gethydrated.hydra.test.actors.event;

import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.api.event.EventListener;
import org.junit.Test;

public class EventStreamTest {

    @Test
    public void testSubscribe() throws InterruptedException {
        final SystemEventStream eventStream = new SystemEventStream();
        final boolean result = eventStream.subscribe(new EventListener() {
            @Override
            public void handle(final Object event) {
                System.out.println(event.getClass());
            }
        }, Object.class);

        eventStream.publish(new Object());
        eventStream.publish("");
        eventStream.publish(new Integer(1));
        eventStream.publish(new Object());
        Thread.sleep(1000);

        assertTrue(result);
    }
}
