package org.gethydrated.hydra.api.configuration;


/**
 * 
 * @author Hanno Sternberg
 *
 */
public interface ConfigurationGetter {

    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public Boolean getBoolean(final String name) throws Exception;
    
    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public Integer getInteger(final String name) throws Exception;
    
    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public Double getFloat(final String name) throws Exception;
    
    /**
     * 
     * @param name Item name.
     * @return item value.
     * @throws ConfigItemNotFoundException .
     */
    public String getString(final String name) throws Exception;


}
