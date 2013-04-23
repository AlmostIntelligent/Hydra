package org.gethydrated.hydra.api.configuration;

import java.util.List;

/**
 * 
 * @author Hanno Sternberg
 */
public interface ConfigurationItem {

    /**
     * 
     * @return .
     */
    String getName();

    /**
     * 
     * @return .
     */
    Boolean hasChildren();

    /**
     * 
     * @return .
     * @throws ConfigItemTypeException .
     */
    List<ConfigurationItem> getChildren() throws ConfigItemTypeException;

    /**
     * 
     * @return .
     */
    Boolean hasValue();

    /**
     * 
     * @return .
     * @throws ConfigItemTypeException .
     */
    Object getValue() throws ConfigItemTypeException;

    @Override
    boolean equals(final Object other);

    /**
     * 
     * @return .
     */
    ConfigurationItem copy();
}
