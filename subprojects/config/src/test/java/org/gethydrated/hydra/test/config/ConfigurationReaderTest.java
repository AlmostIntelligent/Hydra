package org.gethydrated.hydra.test.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.files.XMLConfigurationReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testsuite for XML Configuration reader.
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigurationReaderTest {

    /**
     * Test set up.
     * 
     * @throws Exception .
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * Test tear down.
     * 
     * @throws Exception .
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test the load method.
     */
    @Test
    public final void testLoad() {
        final XMLConfigurationReader ld = new XMLConfigurationReader();

        try {
            final InputStream inputStream = this.getClass().getClassLoader()
                    .getResourceAsStream("testConfig.xml");
            final Configuration cfg = ld.parse(inputStream);
            assertEquals("Test-configuration", cfg.getString("name"));
            assertEquals((Integer) 1337, cfg.getInteger("network.port"));
            assertEquals("local", cfg.getString("network.host"));
        } catch (final ConfigItemNotFoundException e) {
            fail("Config load error");
        }

    }

}
