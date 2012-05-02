package org.gethydrated.hydra.api.test;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.gethydrated.hydra.launcher.Launcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LauncherTest {

	private final ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	private static final String lineSep = System.getProperty( "line.separator" ); 
	
	@Before
	public void setup() {
		System.setOut(new PrintStream(output));
	}
	
	@Test
	public void testUsage() {
		Launcher.printUsage();
		assertTrue(output.toString().equals("Hydra Usage:"+lineSep));
	}
	
	@After
	public void teardown() {
		System.setOut(null);
	}
}
