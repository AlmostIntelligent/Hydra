package org.gethydrated.hydra.test.actors.event;

import static org.junit.Assert.assertTrue;

import org.gethydrated.hydra.actors.event.Event;
import org.gethydrated.hydra.actors.event.EventListener;
import org.gethydrated.hydra.actors.event.EventStream;
import org.junit.Test;

public class EventStreamTest {

	@Test
	public void testSubscribe() {
		EventStream eventStream = new EventStream();
		boolean result = eventStream.subscribe(new EventListener() {
			@Override
			public void handle(Event event) {
			}}, Event.class);
		
		assertTrue(result);
	}
}
