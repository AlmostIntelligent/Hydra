package org.gethydrated.hydra.core.io;

/**
 *
 */
public class Archive {

    String name;

    String version;

    public void setName(String name) {
        if(this.name==null) {
            this.name = name;
        }
    }

    public void setVersion(String version) {
        if(this.version==null) {
            this.version = version;
        }
    }

    public String toString() {
        return "<Archive:"+name+":"+version+">";
    }
}
