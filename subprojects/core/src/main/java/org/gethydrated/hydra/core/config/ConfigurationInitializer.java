package org.gethydrated.hydra.core.config;

public class ConfigurationInitializer {

	private Configuration cfg;
	
	public ConfigurationInitializer(Configuration cfg) {
		this.cfg = cfg;
	}
	
	public void configure() throws ConfigItemNotFoundException {
		//TODO: xml configurator
		BasicConfigurator.configure(cfg);
	}
}
