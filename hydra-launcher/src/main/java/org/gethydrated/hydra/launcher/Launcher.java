package org.gethydrated.hydra.launcher;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Launcher {

	private static String hydraDir;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(setWorkingDirectory()) {
			configureLogback();
			Logger logger = LoggerFactory.getLogger(Launcher.class);
			logger.info("Starting Hydra.");
			logger.debug("Logback configured.");
		}
	}

	private static boolean setWorkingDirectory() {
		try {
			String path =Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			path = path.substring(0, path.lastIndexOf('/'));
			hydraDir = path.substring(0, path.lastIndexOf('/')+1);
			return true;
		} catch (URISyntaxException e) {
			System.err.println("Could not set working directory");
			return false;
		}
	}
	
	private static void configureLogback() {
		Map<String,String> properties = new HashMap<>();
		properties.put("HYDRA_HOME", hydraDir);
		LogbackConfigurator.configure(hydraDir+"conf/logback.xml", properties);
	}
	
	public static void printUsage() {
		System.out.println("Hydra Usage:");
	}
}
