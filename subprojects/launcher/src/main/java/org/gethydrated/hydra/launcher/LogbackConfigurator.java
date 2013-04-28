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

/**
 * Utilityclass for Logback configuration.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public final class LogbackConfigurator {

    /**
     * Hide constructor to prevent instanziation.
     */
    private LogbackConfigurator() {
    }

    /**
     * 
     * @param confFile
     *            config file name.
     * @param properties
     *            additional properties.
     */
    public static void configure(final String confFile,
            final Map<String, String> properties) {
        try (FileInputStream input = new FileInputStream(confFile)) {
            configure(input, properties);
        } catch (final IOException e) {
            throw new IllegalStateException(
                    "Could not load logback configuration: " + confFile, e);
        }
    }

    /**
     * 
     * @param file
     *            config file.
     * @param properties
     *            additional properties.
     */
    public static void configure(final URL file,
            final Map<String, String> properties) {
        try (InputStream input = file.openStream()) {
            configure(input, properties);
        } catch (final IOException e) {
            throw new IllegalStateException(
                    "Could not load logback configuration: " + file, e);
        }
    }

    /**
     * 
     * @param input
     *            config data.
     * @param properties
     *            additional properties.
     */
    public static void configure(final InputStream input,
            final Map<String, String> properties) {
        final LoggerContext lc = (LoggerContext) LoggerFactory
                .getILoggerFactory();
        try {
            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(configureContext(lc, properties));
            configurator.doConfigure(input);
        } catch (final JoranException e) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
    }

    /**
     * 
     * @param context
     *            Initial logger context.
     * @param properties
     *            additional properties.
     * @return logger context.
     */
    private static LoggerContext configureContext(final LoggerContext context,
            final Map<String, String> properties) {
        context.reset();
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            context.putProperty(entry.getKey(), entry.getValue());
        }
        return context;
    }
}
