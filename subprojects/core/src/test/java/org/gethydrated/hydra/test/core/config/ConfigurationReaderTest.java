package org.gethydrated.hydra.test.core.config;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.gethydrated.hydra.core.config.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.config.Configuration;
import org.gethydrated.hydra.core.config.XMLConfigurationReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class ConfigurationReaderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testLoad() {
        XMLConfigurationReader ld = new XMLConfigurationReader();

        try {
            Configuration cfg = ld.load("test/testConfig.xml");
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
