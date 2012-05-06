package org.gethydrated.hydra.launcher;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.core.HydraFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Launcher {

	private static String hydraDir;
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		setWorkingDirectory();
		configureLogback();
		Logger logger = LoggerFactory.getLogger(Launcher.class);
		logger.info("Launching Hydra platform.");
		logger.debug("Logback configured.");
		logger.debug("Set Hydra directory to: "+hydraDir);
		Hydra hydra = HydraFactory.getHydra();
		hydra.start();
		//Thread.currentThread().join();
	}

	private static void setWorkingDirectory() {
		try {
			String path =Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			path = path.substring(0, path.lastIndexOf('/'));
			hydraDir = path.substring(0, path.lastIndexOf('/')+1);
		} catch (URISyntaxException e) {
			throw new IllegalStateException("Could not detect Hydra directory", e);
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
