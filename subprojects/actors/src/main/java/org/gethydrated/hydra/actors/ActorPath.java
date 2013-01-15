package org.gethydrated.hydra.actors;

import org.gethydrated.hydra.actors.node.ActorNode;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Arrays;

public class ActorPath implements Serializable {

    private final String[] pathStack;

    /**
     * Root constructor.
     */
    public ActorPath() {
        pathStack = new String[0];
    }

    /**
     * Copy constructor.
     */
    public ActorPath(ActorPath path) {
        this.pathStack = new String[path.pathStack.length];
        System.arraycopy(path.pathStack, 0, pathStack, 0, path.pathStack.length);
    }

    private ActorPath(String[] path) {
        pathStack = new String[path.length];
        System.arraycopy(path, 0, pathStack, 0, path.length);
    }

    public boolean isRoot() {
        return (pathStack.length==0);
    }

    public ActorPath createChild(String name) {
        validateName(name);
        String[] newPathStack = new String[pathStack.length+1];
        System.arraycopy(pathStack, 0, newPathStack, 0, pathStack.length);
        newPathStack[pathStack.length] = name;
        return new ActorPath(newPathStack);
    }

    public ActorPath parent() {
        if(isRoot()) {
            throw new RuntimeException("There is no parent to root");
        }
        String[] newPathStack = new String[pathStack.length-1];
        System.arraycopy(pathStack, 0, newPathStack, 0, pathStack.length-1);
        return new ActorPath(newPathStack);
    }

    /**
     * Returns the target actors name.
     * @return actor name.
     */
    public String getName() {
        return pathStack[pathStack.length-1];
    }

    private boolean validateName(String name) {
        return name.matches("[a-zA-Z0-9_-]");
    }

    public String toString() {
        if(isRoot()) {
            return "/";
        }
        StringBuilder sb = new StringBuilder();
        for(String s : pathStack) {
            sb.append("/");
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof ActorNode) {
            return this.equals(((ActorNode) o).getPath());
        }

        if (getClass() != o.getClass()) return false;

        ActorPath actorPath = (ActorPath) o;

        return java.util.Arrays.equals(pathStack, actorPath.pathStack);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pathStack);
    }

    public static ActorPath apply(ActorPath path, String uri) throws MalformedURLException {
        ActorPath result = path;
        if(uri.startsWith("/")) {
            result = new ActorPath();
            uri = uri.substring(1);
        }
        String[] uris = uri.split("/");
        try{
            for(String s:uris) {
                if(s.equals("..")) {
                    result = result.parent();
                } else {
                    result = result.createChild(s);
                }
            }
            return result;
        } catch (Exception e) {
            throw new MalformedURLException("Cannot apply " + uri +" to '" + path + "'");
        }
    }
}
