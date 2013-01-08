package org.gethydrated.hydra.core.service;

import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates parsed service information.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class ServiceInfo {
    /**
     * List of service jars.
     */
    private final List<URL> serviceJars = new LinkedList<>();

    /**
     * Service name.
     */
    private String name;

    /**
     * Service version.
     */
    private String version;

    /**
     * Service activator class name.
     */
    private String activator;

    /**
     * Getter name.
     * 
     * @return service name.
     */
    public final String getName() {
        return name;
    }

    /**
     * Setter name.
     * 
     * @param name
     *            service name.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Getter version.
     * 
     * @return service version.
     */
    public final String getVersion() {
        return version;
    }

    /**
     * Setter version.
     * 
     * @param version
     *            service version.
     */
    public final void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Getter service jars.
     * 
     * @return service jars urls.
     */
    public final URL[] getServiceJars() {
        int i = 0;
        final URL[] urls = new URL[serviceJars.size()];
        Iterator<URL> it = serviceJars.iterator();
        while (it.hasNext()) {
            urls[i] = serviceJars.get(i++);
            it.next();
        }
        return urls;
    }

    /**
     * Adds url to service.
     * @param serviceJar url.
     */
    public final void addServiceJar(final URL serviceJar) {
        this.serviceJars.add(serviceJar);
    }

    /**
     * Getter activator.
     * @return service activator.
     */
    public final String getActivator() {
        return activator;
    }

    /**
     * Setter activator.
     * @param activator service activator class name.
     */
    public final void setActivator(final String activator) {
        this.activator = activator;
    }
}
