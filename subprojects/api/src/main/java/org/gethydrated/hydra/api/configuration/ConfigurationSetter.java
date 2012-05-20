package org.gethydrated.hydra.api.configuration;


/**
 * 
 * @author Hanno Sternberg
 * @since 0.1.0
 *
 */
public interface ConfigurationSetter {

            /**
             * 
             * @param name item name.
             * @param value item value.
             */
            public void setBoolean(final String name, final Boolean value);

            /**
             * 
             * @param name item name.
             * @param value item value.
             */
            public void setInteger(final String name, final Integer value);

            /**
             * 
             * @param name item name.
             * @param value item value.
             */
            public void setFloat(final String name, final Double value);

            /**
             * 
             * @param name item name.
             * @param value item value.
             */
            public void setString(final String name, final String value);

}
