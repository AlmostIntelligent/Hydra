package org.gethydrated.hydra.api.configuration;

import java.util.List;

/**
 * 
 * @author Hanno Sternberg
 * 
 */
public interface ConfigurationGetter {

        /**
         * 
         * @param name
         *                Item name.
         * @return List of sub items.
         * @throws ConfigItemNotFoundException .
         */
        List<String> list(final String name)
                        throws ConfigItemNotFoundException;

        /**
         * 
         * @param name
         *                Item name.
         * @return item value.
         * @throws ConfigItemNotFoundException .
         */
        Object get(final String name) throws ConfigItemNotFoundException;
        
        /**
         * 
         * @param name
         *                Item name.
         * @return item value.
         * @throws ConfigItemNotFoundException .
         */
        Boolean getBoolean(final String name) throws ConfigItemNotFoundException;

        /**
         * 
         * @param name
         *                Item name.
         * @return item value.
         * @throws ConfigItemNotFoundException .
         */
        Integer getInteger(final String name) throws ConfigItemNotFoundException;

        /**
         * 
         * @param name
         *                Item name.
         * @return item value.
         * @throws ConfigItemNotFoundException .
         */
        Double getFloat(final String name) throws ConfigItemNotFoundException;

        /**
         * 
         * @param name
         *                Item name.
         * @return item value.
         * @throws ConfigItemNotFoundException .
         */
        String getString(final String name) throws ConfigItemNotFoundException;

}
