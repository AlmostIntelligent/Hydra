package org.gethydrated.hydra.launcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

public class LogbackConfigurator {

	private LogbackConfigurator() {}
	
	public static void configure(String confFile, Map<String,String> properties) {
		try( FileInputStream input = new FileInputStream(confFile)) {
			configure(input, properties);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load logback configuration: "+confFile, e);
		}
	}
	
	public static void configure(URL file, Map<String,String> properties){
		try( InputStream input = file.openStream()) {
			configure(input, properties);
		} catch (IOException e) {
			throw new IllegalStateException("Could not load logback configuration: "+file, e);
		}
	}
	
	public static void configure(InputStream input, Map<String,String> properties) {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    try {
	      JoranConfigurator configurator = new JoranConfigurator();
	      configurator.setContext(configureContext(lc, properties));
	      configurator.doConfigure(input);
	    } catch (JoranException e) {
	        // StatusPrinter will handle this
	    }
	    StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}
	
	private static LoggerContext configureContext(LoggerContext context, Map<String, String> properties) {
	    context.reset();
	    for (Map.Entry<String, String> entry : properties.entrySet()) {
	      context.putProperty(entry.getKey(), entry.getValue());
	    }
	    return context;
	  }
}
