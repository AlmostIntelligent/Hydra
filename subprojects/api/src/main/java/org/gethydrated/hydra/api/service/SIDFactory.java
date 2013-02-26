package org.gethydrated.hydra.api.service;

/**
 *
 */
public interface SIDFactory {
    SID fromString(String sid);

    SID fromUSID(USID usid);
}
