package org.gethydrated.hydra.core.configuration;

import org.gethydrated.hydra.api.configuration.ConfigItemNotFoundException;
import org.gethydrated.hydra.api.configuration.Configuration;

/**
 *
 * @author Hanno Sternberg
 */
public class ConfigMerger {

    public static Configuration merge(final Configuration left, final Configuration right) {
        Configuration cfg = new ConfigurationImpl();
        cfg.setRoot(left.getRoot().copy());
        try{
            for (String itm : right.list()) {
                Object val = right.get(itm);
                if (!cfg.has(itm) || !left.get(itm).equals(val)) {
                    cfg.set(itm, val);
                }
            }
        } catch (ConfigItemNotFoundException e) {

        }
        return cfg;
    }
}
