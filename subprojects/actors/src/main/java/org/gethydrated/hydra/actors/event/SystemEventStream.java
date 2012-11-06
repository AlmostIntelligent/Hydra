package org.gethydrated.hydra.actors.event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.gethydrated.hydra.actors.ActorRef;

public class SystemEventStream implements EventStream {

	BlockingQueue<Object> events = new LinkedBlockingQueue<>();
	
	ExecutorService threadpool;
	
	List<EventDispatcher> dispatchers = new LinkedList<>();
	
	Map<Class<?>, List<EventListener>> listeners = new ConcurrentHashMap<>();
	
	@Override
	public boolean subscribe(final ActorRef subscriber, Class<?> classifier) {
		return subscribe(new EventListener() {
			@Override
			public void handle(Object event) {
				subscriber.tell(event, null);
			}}, classifier);
	}

	@Override
	public boolean subscribe(EventListener subscriber, Class<?> classifier) {
		List<EventListener> l = listeners.get(classifier);
		if(l == null) {
			List<EventListener> li = new LinkedList<>();
			li.add(subscriber);
			listeners.put(classifier, li);
			return true;
		} else {
			if(l.contains(subscriber)) {
				return false;
			} else {
				l.add(subscriber);
				return true;
			}
		}
	}

	@Override
	public boolean unsubscribe(ActorRef subscriber, Class<?> classifier) {
		//TODO:
		return false;
	}

	@Override
	public boolean unsubscribe(EventListener subscriber, Class<?> classifier) {
		//TODO:
		return false;
	}

	@Override
	public boolean unsubscribe(ActorRef subscriber) {
		//TODO:
		return false;
	}

	@Override
	public boolean unsubscribe(EventListener subscriber) {
		//TODO:
		return false;
	}

	@Override
	public boolean publish(Object event) {
		return events.offer(event);
	}

	public void startEventHandling(int d) {
		if(threadpool == null) {
			threadpool = Executors.newCachedThreadPool();
			for(int i = 0; i < d; i++) {
				EventDispatcher e = new EventDispatcher();
				dispatchers.add(new EventDispatcher());
				threadpool.execute(e);
			}
		}
	}
	
	public void stopEventHandling() {
		if(threadpool != null) {
			for(EventDispatcher e : dispatchers) {
				e.stop();
			}
			threadpool.shutdownNow();
			threadpool = null;
		}
	}
	
	public boolean hasRemainingEvents() {
		return !events.isEmpty();
	}
	
	public List<Object> getRemainingEvents() {
		return new LinkedList<>(events);
	}
	
	private void dispatch(Object event) {
		for (Class<?> c : listeners.keySet()) {
			if(c.isInstance(event)) {
				for(EventListener l : listeners.get(c)) {
					l.handle(event);
				}
			}
		}
	}
	
	private class EventDispatcher implements Runnable {

		private AtomicBoolean running = new AtomicBoolean(true);
		
		@Override
		public void run() {
			while (running.get()) {
				try {
					Object o = events.take();
					dispatch(o);
				} catch (InterruptedException e) {
				}
			}
		}
		
		public void stop() {
			running.set(false);
		}
	}
}
