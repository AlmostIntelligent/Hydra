package org.gethydrated.hydra.test.api;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyTest {

    @Test
    public void dummyTest() {
        Logger logger = LoggerFactory.getLogger(DummyTest.class);
        logger.info("dummy");
        assertTrue(true);
    }
}
