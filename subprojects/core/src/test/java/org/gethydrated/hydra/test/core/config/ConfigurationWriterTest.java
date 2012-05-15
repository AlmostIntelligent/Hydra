package org.gethydrated.hydra.test.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.gethydrated.hydra.core.config.Configuration;
import org.gethydrated.hydra.core.config.PlainConfigurationWriter;
import org.gethydrated.hydra.core.config.XMLConfigurationWriter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Hanno Sternberg
 * @since 0.1.0
 */
public class ConfigurationWriterTest {

    /**
     * @var Configuration.
     */
    private Configuration cfg;

    /**
     * Test setup.
     * 
     * @throws Exception .
     */
    @Before
    public final void setUp() throws Exception {
        cfg = new Configuration();
        cfg.set("Name", "test");
        cfg.set("Network.Port", 1337);
        cfg.set("Network.Host", "local");
    }

    /**
     * Test tear down.
     * 
     * @throws Exception .
     */
    @After
    public final void tearDown() throws Exception {
    }

    /**
     * Tests the XML writer.
     */
    @Test
    public final void testXML() {
        XMLConfigurationWriter dut = new XMLConfigurationWriter(cfg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        dut.saveToStream(ps);
        try {
            assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
                    + "<Configuration>\r\n" + "\t<Name>test</Name>\r\n"
                    + "\t<Network>\r\n" + "\t\t<Port>1337</Port>\r\n"
                    + "\t\t<Host>local</Host>\r\n" + "\t</Network>\r\n"
                    + "</Configuration>\r\n", baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            fail("");
        }

    }

    /**
     * Test the plain text writer.
     */
    @Test
    public final void testPlain() {
        PlainConfigurationWriter dut = new PlainConfigurationWriter(cfg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        dut.saveToStream(ps);
        try {
            assertEquals("Configuration.Name=test\r\n"
                    + "Configuration.Network.Port=1337\r\n"
                    + "Configuration.Network.Host=local\r\n",
                    baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            fail("");
        }

    }

}
