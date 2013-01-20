package org.gethydrated.hydra.core.internal;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Archive {

    private String name;

    private String version;

    private Map<String, Service> services = new HashMap<>();

    private final List<URL> archiveJars = new LinkedList<>();

    public void setName(String name) {
        if(this.name==null) {
            this.name = name;
        }
    }

    public String getName() {
        return name;
    }

    public void setVersion(String version) {
        if(this.version==null) {
            this.version = version;
        }
    }

    public String toString() {
        return "<Archive:"+name+":"+version+" containing: "+services+">";
    }

    public void addService(Service service) {
        service.setArchive(this);
        services.put(service.getName(), service);
    }

    public Service getService(String serviceName) {
        return services.get(serviceName);
    }

    public void addArchiveURL(URL url) {
        archiveJars.add(url);
    }

    public URL[] getArchiveURLs() {
        int i = 0;
        final URL[] urls = new URL[archiveJars.size()];
        for(URL url : archiveJars) {
            urls[i] = archiveJars.get(i++);
        }
        return urls;
    }
}
