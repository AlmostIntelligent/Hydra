package org.gethydrated.hydra.test.core.coordinator;

import org.gethydrated.hydra.core.coordinator.Coordinator.Callback;
import org.gethydrated.hydra.core.coordinator.LocalCoordinator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class LocalCoordinatorTest {

    private LocalCoordinator coordinator;

    @Before
    public void setup() {
        coordinator = new LocalCoordinator(UUID.randomUUID());
    }

    @After
    public void tearDown() {
        coordinator = null;
    }

    @Test
    public void testIsLocal() {
        assertTrue(coordinator.isLocal());
    }

    @Test
    public void testAquireLock() {
        final UUID testId = UUID.randomUUID();
        final AtomicBoolean called = new AtomicBoolean(false);
        coordinator.setGrantedCallback(new Callback<UUID>() {
            @Override
            public void call(UUID uuid) {
                called.set(testId.equals(uuid));
            }
        });
        coordinator.acquireLock(testId);
        assertTrue(called.get());
    }

    @Test
    public void testNoReentrantAquireLock() {
        final UUID testId = UUID.randomUUID();
        final AtomicInteger called = new AtomicInteger(0);
        coordinator.setGrantedCallback(new Callback<UUID>() {
            @Override
            public void call(UUID uuid) {
                if(testId.equals(uuid)) {
                    called.incrementAndGet();
                }
            }
        });
        coordinator.acquireLock(testId);
        assertTrue(called.get() == 1);
        coordinator.acquireLock(testId);
        assertTrue(called.get() == 1);
    }

    @Test
    public void testAquireLockQeue() {
        final UUID testId1 = UUID.randomUUID();
        final UUID testId2 = UUID.randomUUID();
        final AtomicBoolean called1 = new AtomicBoolean(false);
        final AtomicBoolean called2 = new AtomicBoolean(false);
        coordinator.setGrantedCallback(new Callback<UUID>() {
            @Override
            public void call(UUID uuid) {
                if(testId1.equals(uuid)) {
                    called1.set(true);
                }
                if(testId2.equals(uuid)) {
                    called2.set(true);
                }
            }
        });
        coordinator.acquireLock(testId1);
        assertTrue(called1.get());
        assertFalse(called2.get());
        coordinator.acquireLock(testId2);
        assertTrue(called1.get());
        assertFalse(called2.get());
        coordinator.releaseLock(testId1);
        assertTrue(called1.get());
        assertTrue(called2.get());
    }

    @Test
    public void testRemoveQueue() {
        final UUID testId1 = UUID.randomUUID();
        final UUID testId2 = UUID.randomUUID();
        final AtomicBoolean called1 = new AtomicBoolean(false);
        final AtomicBoolean called2 = new AtomicBoolean(false);
        coordinator.setGrantedCallback(new Callback<UUID>() {
            @Override
            public void call(UUID uuid) {
                if(testId1.equals(uuid)) {
                    called1.set(true);
                }
                if(testId2.equals(uuid)) {
                    called2.set(true);
                }
            }
        });
        coordinator.acquireLock(testId1);
        assertTrue(called1.get());
        assertFalse(called2.get());
        coordinator.acquireLock(testId2);
        assertTrue(called1.get());
        assertFalse(called2.get());
        coordinator.releaseLock(testId2);
        coordinator.releaseLock(testId1);
        assertTrue(called1.get());
        assertFalse(called2.get());
    }

    @Test(expected = IllegalStateException.class)
    public void testCallbackError() {
        final UUID testId = UUID.randomUUID();
        coordinator.acquireLock(testId);
    }
}
