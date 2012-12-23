package org.gethydrated.hydra.api.configuration;

import java.util.List;

/**
 *
 * @author Hanno Sternberg
 */
public interface ConfigurationItem {

    String getName();
    Boolean hasChildren();
    List<ConfigurationItem> getChildren() throws ConfigItemTypeException;
    Boolean hasValue();
    Object getValue() throws ConfigItemTypeException;
    boolean equals(final Object other);
    ConfigurationItem copy();
}
