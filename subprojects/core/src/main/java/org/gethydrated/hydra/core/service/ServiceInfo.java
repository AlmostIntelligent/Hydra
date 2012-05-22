package org.gethydrated.hydra.core.service;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class ServiceInfo {
    private List<URL> serviceJars = new LinkedList<>();

    private String name;

    private String version;
    
    private String activator;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public URL[] getServiceJars() {
        int i = 0;
        URL[] urls = new URL[serviceJars.size()];
        for(URL u : serviceJars) {
            urls[i] = serviceJars.get(i++);
        }
        return urls;
    }

    public void addServiceJar(URL serviceJar) {
        this.serviceJars.add(serviceJar);
    }

    public String getActivator() {
        return activator;
    }

    public void setActivator(String activator) {
        this.activator = activator;
    }
}
