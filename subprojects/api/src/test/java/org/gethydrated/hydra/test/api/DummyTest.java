package org.gethydrated.hydra.test.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dummy test class.
 * @author Christian Kulpa
 * @since 0.1.0
 *
 */
public final class DummyTest {

    /**
     * Dummy test.
     */
    @Test
    public void dummyTest() {
        Logger logger = LoggerFactory.getLogger(DummyTest.class);
        logger.info("dummy");
        assertTrue(true);
    }
}
