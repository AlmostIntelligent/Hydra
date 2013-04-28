package org.gethydrated.hydra.api.service;

/**
 * Service id factory.
 */
public interface SIDFactory {
    /**
     * Returns a service id from a given string.
     * @param sid service id as string.
     * @return service id.
     */
    SID fromString(String sid);

    /**
     * Returns a service id from a given usid.
     * @param usid service usid.
     * @return service id.
     */
    SID fromUSID(USID usid);
}
