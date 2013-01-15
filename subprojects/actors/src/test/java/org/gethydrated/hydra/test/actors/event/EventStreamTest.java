package org.gethydrated.hydra.test.actors.event;

import org.gethydrated.hydra.actors.event.SystemEventStream;
import org.gethydrated.hydra.api.event.EventListener;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class EventStreamTest {

	@Test
	public void testSubscribe() throws InterruptedException {
		SystemEventStream eventStream = new SystemEventStream();
		boolean result = eventStream.subscribe(new EventListener() {
			@Override
			public void handle(Object event) {
				System.out.println(event.getClass());
			}}, Object.class);

		eventStream.publish(new Object());
		eventStream.publish("");
		eventStream.publish(new Integer(1));
		eventStream.publish(new Object());
		Thread.sleep(1000);
		
		assertTrue(result);
	}
}
