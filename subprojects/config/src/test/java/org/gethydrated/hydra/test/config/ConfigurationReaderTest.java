package org.gethydrated.hydra.test.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.files.XMLConfigurationReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

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
        XMLConfigurationReader ld = new XMLConfigurationReader();

        try {
            Configuration cfg = ld.load("src/test/resources/testConfig.xml");
            assertEquals("Test-configuration", cfg.getString("name"));
            assertEquals((Integer) 1337, cfg.getInteger("network.port"));
            assertEquals("local", cfg.getString("network.host"));
        } catch (SAXException e) {
            fail("SAX error");
        } catch (IOException e) {
            fail("File not found");
        } catch (ParserConfigurationException e) {
            fail("Parser Error");
        } catch (ConfigItemNotFoundException e) {
            fail("Config load error");
        }

    }

}
