package org.gethydrated.hydra.core.internal;

import java.net.URLClassLoader;

/**
 *
 */
public class Service {
    private String name;
    private String activator;
    private Archive parent;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVersion(String version) {
    }

    public void addDependency(Dependency dep) {

    }

    public void setActivator(String activator) {
        this.activator = activator;
    }

    public String getActivator() {
        return activator;
    }

    public String toString() {
        return "<Service:"+name+">";
    }

    public void setArchive(Archive archive) {
        parent = archive;
    }

    public ClassLoader getClassLoader() {
        return new URLClassLoader(parent.getArchiveURLs(),
                this.getClass().getClassLoader().getParent());
    }
}
