package org.gethydrated.hydra.api.platform;

/**
 * Marks the implementing class as platform aware.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public interface PlatformAware {
    /**
     * Sets a platform instance.
     * 
     * @param p
     *            platform instance.
     */
    void setPlatform(Platform p);
}
