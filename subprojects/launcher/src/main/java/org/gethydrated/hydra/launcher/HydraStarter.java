package org.gethydrated.hydra.launcher;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.gethydrated.hydra.api.Hydra;
import org.gethydrated.hydra.core.HydraFactory;

public class HydraStarter {

	public static void start(String[] args) throws Exception {
		configureLogback(System.getProperty("hydra.home"));
		Hydra hydra = HydraFactory.getHydra();
        hydra.start();
	}
	
	private static void configureLogback(String hydraHome) throws Exception {
		Map<String,String> properties = new HashMap<>();
		properties.put("HYDRA_HOME", hydraHome);
		URL file = HydraStarter.class.getResource("/logging.xml");
		if(file!=null) {
			LogbackConfigurator.configure(file, properties);
		} else {
			throw new IllegalStateException("Could not find logback.xml file");
		}
	}
}

