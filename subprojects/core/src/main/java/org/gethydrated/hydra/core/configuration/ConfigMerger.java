package org.gethydrated.hydra.core.configuration;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.config.ConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Hanno Sternberg
 */
public final class ConfigMerger {

    private ConfigMerger() { }
    
    /**
     * Merges two configurations.
     * @param left config one.
     * @param right config two.
     * @return resulting config.
     */
    public static Configuration merge(final Configuration left,
            final Configuration right) {
        final Configuration cfg = new ConfigurationImpl();
        cfg.setRoot(left.getRoot().copy());
        try {
            for (final String itm : right.list()) {
                final Object val = right.get(itm);
                if (!cfg.has(itm) || !left.get(itm).equals(val)) {
                    cfg.set(itm, val);
                }
            }
        } catch (final ConfigItemNotFoundException e) {
            final Logger logger = LoggerFactory.getLogger(ConfigMerger.class);
            logger.error(e.getMessage(), e);
        }
        return cfg;
    }
}
