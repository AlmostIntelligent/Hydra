/**
 * 
 */
package org.gethydrated.hydra.test.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.configuration.ConfigurationImpl;
import org.gethydrated.hydra.core.configuration.tree.ConfigList;
import org.gethydrated.hydra.core.configuration.tree.ConfigValue;
import org.gethydrated.hydra.core.configuration.tree.ConfigurationItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Hanno Sternberg
 * @since 0.1.0
 * 
 */
public class ConfigurationTest {

    /**
     * @var Test configuration.
     */
    private ConfigurationImpl cfg;

    /**
     * @throws java.lang.Exception .
     */
    @Before
    public final void setUp() throws Exception {
        cfg = new ConfigurationImpl();
    }

    /**
     * @throws java.lang.Exception .
     */
    @After
    public final void tearDown() throws Exception {

    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#set(java.lang.String, java.lang.Object)}
     * .
     */
    @Test
    public final void testSetValue() {
        try {
            cfg.set("Name", "test");
        } catch (Exception e) {
            fail("Got Exception");
        }
        assertEquals(cfg.getRoot().getChilds().size(), 1);
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#set(java.lang.String, java.lang.Object)}
     * .
     */
    @Test
    public final void testSetSubValue() {
        ConfigurationItem child = null;
        cfg.set("Network.Port", 1337);
        cfg.set("Network.Host", "local");
        assertEquals(cfg.getRoot().getChilds().size(), 1);
        try {
            child = cfg.getRoot().getChild("Network");
        } catch (ConfigItemNotFoundException e) {
            fail("SubList not created!");
        }
        assertNotNull(child);
        assertTrue(child.hasChildren());
        assertEquals(((ConfigList) child).getChilds().size(), 2);
        try {
            assertEquals(
                    ((ConfigValue<?>) ((ConfigList) child).getChild("Port"))
                            .value(),
                    1337);
            assertEquals(
                    ((ConfigValue<?>) ((ConfigList) child).getChild("Host"))
                            .value(),
                    "local");
        } catch (ConfigItemNotFoundException e) {
            fail("SubItem not created.");
        }

    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#list(java.lang.String)}
     * .
     */
    @Test
    public final void testList() {
        cfg.set("Name", "test");
        cfg.set("Network.Port", 1337);
        cfg.set("Network.Host", "local");

        assertEquals("[Name, Network]", cfg.list("").toString());
        assertEquals("[Port, Host]", cfg.list("Network").toString());
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#get(java.lang.String)}
     * . .
     */
    @Test
    public final void testGet() {
        cfg.set("Name", "test");
        cfg.set("Network.Port", 1337);
        cfg.set("Network.Host", "local");

        try {
            assertEquals(cfg.get("Name"), "test");
            assertEquals(cfg.get("Network.Host"), "local");
            assertEquals(cfg.get("Network.Port"), 1337);
        } catch (ConfigItemNotFoundException e) {
            fail("Item nor found");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#setBoolean(java.lang.String, java.lang.Boolean)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public final void testSetBoolean() {
        cfg.setBoolean("Active", true);
        try {
            assertTrue(((ConfigValue<Boolean>) cfg.getRoot().getChild("Active"))
                    .value());
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#getBoolean(java.lang.String)}
     * .
     */
    @Test
    public final void testGetBoolean() {
        cfg.setBoolean("Active", true);
        try {
            assertTrue(cfg.getBoolean("Active"));
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#setInteger(java.lang.String, java.lang.Integer)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public final void testSetInteger() {
        cfg.setInteger("A_Number", 1337);
        try {
            assertEquals(
                    ((ConfigValue<Integer>) cfg.getRoot().getChild("A_Number"))
                            .value(),
                    (Integer) 1337);
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#getInteger(java.lang.String)}
     * .
     */
    @Test
    public final void testGetInteger() {
        cfg.setInteger("A_Number", 1337);
        try {
            assertEquals(cfg.getInteger("A_Number"), (Integer) 1337);
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#setFloat(java.lang.String, java.lang.Double)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public final void testSetFloat() {
        cfg.setFloat("A_Double", (Double) 13.37);
        try {
            assertEquals(
                    ((ConfigValue<Double>) cfg.getRoot().getChild("A_Double"))
                            .value(),
                    (Double) 13.37);
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#getFloat(java.lang.String)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public final void testGetFloat() {
        cfg.setFloat("A_Double", (Double) 13.37);
        try {
            assertEquals(
                    ((ConfigValue<Double>) cfg.getRoot().getChild("A_Double"))
                            .value(),
                    (Double) 13.37);
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#setString(java.lang.String, java.lang.String)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public final void testSetString() {
        cfg.setString("A_String", "foobar");
        try {
            assertEquals(
                    ((ConfigValue<String>) cfg.getRoot().getChild("A_String"))
                            .value(),
                    "foobar");
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for
     * {@link org.gethydrated.hydra.core.configuration.ConfigurationImpl#getString(java.lang.String)}
     * .
     */
    @Test
    public final void testGetString() {
        cfg.setString("A_String", "foobar");
        try {
            assertEquals(cfg.getString("A_String"), "foobar");
        } catch (ConfigItemNotFoundException e) {
            fail("Value not set");
        }
    }

    /**
     * Test method for copy.
     */
    @Test
    public final void testCopy() {
        ConfigurationImpl cp = cfg.copy();
        assertFalse(cp == cfg);
        assertEquals(cfg, cp);
    }

}
