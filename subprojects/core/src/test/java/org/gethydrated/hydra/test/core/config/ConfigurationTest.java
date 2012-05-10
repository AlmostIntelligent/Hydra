/**
 * 
 */
package org.gethydrated.hydra.test.core.config;

import static org.junit.Assert.*;

import org.gethydrated.hydra.core.config.ConfigItemNotFoundException;
import org.gethydrated.hydra.core.config.Configuration;
import org.gethydrated.hydra.core.config.ConfigurationItem;
import org.gethydrated.hydra.core.config.ConfigList;
import org.gethydrated.hydra.core.config.ConfigValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Hanno Sternber
 * @since 0.1.0
 *
 */
public class ConfigurationTest {

	Configuration cfg;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cfg = new Configuration();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#set(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testSetValue() {
		try {
			cfg.set("Name", "test");
		} catch (Exception e){
			fail("Got Exception");
		}
		assertEquals(cfg.getRoot().getChilds().size(), 1);
	}
	
	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#set(java.lang.String, java.lang.Object)}.
	 */
	@Test
	public void testSetSubValue() {
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
		assertEquals(((ConfigList)child).getChilds().size(),2);
		try {
			assertEquals(((ConfigValue<?>)((ConfigList)child).getChild("Port")).value(),1337);
			assertEquals(((ConfigValue<?>)((ConfigList)child).getChild("Host")).value(),"local");
		} catch (ConfigItemNotFoundException e) {
			fail("SubItem not created.");
		}
		
	}
	
	

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
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
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#setBoolean(java.lang.String, java.lang.Boolean)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetBoolean() {
		cfg.setBoolean("Active", true);
		try {
			assertTrue(((ConfigValue<Boolean>)cfg.getRoot().getChild("Active")).value());
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#getBoolean(java.lang.String)}.
	 */
	@Test
	public void testGetBoolean() {
		cfg.setBoolean("Active", true);
		try {
			assertTrue(cfg.getBoolean("Active"));
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#setInteger(java.lang.String, java.lang.Integer)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetInteger() {
		cfg.setInteger("A_Number", 1337);
		try {
			assertEquals(((ConfigValue<Integer>)cfg.getRoot().getChild("A_Number")).value(), (Integer)1337);
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#getInteger(java.lang.String)}.
	 */
	@Test
	public void testGetInteger() {
		cfg.setInteger("A_Number", 1337);
		try {
			assertEquals(cfg.getInteger("A_Number"), (Integer)1337);
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#setFloat(java.lang.String, java.lang.Double)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetFloat() {
		cfg.setFloat("A_Double",(Double) 13.37);
		try {
			assertEquals(((ConfigValue<Double>)cfg.getRoot().getChild("A_Double")).value(),(Double) 13.37);
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#getFloat(java.lang.String)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testGetFloat() {
		cfg.setFloat("A_Double",(Double) 13.37);
		try {
			assertEquals(((ConfigValue<Double>)cfg.getRoot().getChild("A_Double")).value(), (Double)13.37);
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#setString(java.lang.String, java.lang.String)}.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testSetString() {
		cfg.setString("A_String", "foobar");
		try {
			assertEquals(((ConfigValue<String>)cfg.getRoot().getChild("A_String")).value(), "foobar");
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}

	/**
	 * Test method for {@link org.gethydrated.hydra.core.config.Configuration#getString(java.lang.String)}.
	 */
	@Test
	public void testGetString() {
		cfg.setString("A_String", "foobar");
		try {
			assertEquals(cfg.getString("A_String"), "foobar");
		} catch (ConfigItemNotFoundException e) {
			fail("Value not set");
		}
	}
	
	
	@Test
	public void testCopy() {
		Configuration cp = cfg.copy();
		assertFalse(cp == cfg);
		assertEquals(cfg, cp);
	}

}
