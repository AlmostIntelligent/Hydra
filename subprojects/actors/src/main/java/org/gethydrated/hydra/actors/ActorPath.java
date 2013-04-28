package org.gethydrated.hydra.actors;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.gethydrated.hydra.actors.node.ActorNode;

/**
 * Immutable path that can be used to describe actor addresses.
 */
public class ActorPath implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6954568935779532441L;
    /**
     * String array that represents the name hierarchy.
     */
    private final String[] pathStack;

    /**
     * Root constructor.
     */
    public ActorPath() {
        pathStack = new String[0];
    }

    /**
     * Copy constructor.
     * 
     * @param path
     *            source path
     */
    private ActorPath(final ActorPath path) {
        this.pathStack = new String[path.pathStack.length];
        System.arraycopy(path.pathStack, 0, pathStack, 0, path.pathStack.length);
    }

    /**
     * Creates a new ActorPath from a given name hierarchy.
     * 
     * @param path
     *            name hierarchy represented by a string array.
     */
    public ActorPath(final String[] path) {
        pathStack = new String[path.length];
        System.arraycopy(path, 0, pathStack, 0, path.length);
    }

    /**
     * Returns true, if the path is a root "/" path.
     * 
     * @return true, if the path is root.
     */
    public final boolean isRoot() {
        return (pathStack.length == 0);
    }

    /**
     * Creates a new child path under the current path.
     * 
     * @param name
     *            child name.
     * @return child path.
     */
    public final ActorPath createChild(final String name) {
        validateName(name);
        final String[] newPathStack = new String[pathStack.length + 1];
        System.arraycopy(pathStack, 0, newPathStack, 0, pathStack.length);
        newPathStack[pathStack.length] = name;
        return new ActorPath(newPathStack);
    }

    /**
     * Returns a path to the parent of the current path.
     * 
     * @return the parents path.
     */
    public final ActorPath parent() {
        if (isRoot()) {
            throw new RuntimeException("There is no parent to root");
        }
        final String[] newPathStack = new String[pathStack.length - 1];
        System.arraycopy(pathStack, 0, newPathStack, 0, pathStack.length - 1);
        return new ActorPath(newPathStack);
    }

    /**
     * Checks if the current path is an ancestor of the given path.
     * 
     * @param parent
     *            ancestor path.
     * @return true, if given path is a valid ancestor.
     */
    public final boolean isChildOf(final ActorPath parent) {
        if (this.pathStack.length <= parent.pathStack.length) {
            return false;
        }
        for (int i = 0; i < parent.pathStack.length; i++) {
            if (!pathStack[i].equals(parent.pathStack[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the target actors name.
     * 
     * @return actor name.
     */
    public String getName() {
        return pathStack[pathStack.length - 1];
    }

    @Override
    public String toString() {
        if (isRoot()) {
            return "/";
        }
        final StringBuilder sb = new StringBuilder();
        for (final String s : pathStack) {
            sb.append("/");
            sb.append(s);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof ActorNode) {
            return this.equals(((ActorNode) o).getSelf().getPath());
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final ActorPath actorPath = (ActorPath) o;
        return java.util.Arrays.equals(pathStack, actorPath.pathStack);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pathStack);
    }

    public static ActorPath apply(final ActorPath path, final String uri)
            throws MalformedURLException {
        ActorPath result = path;
        String tmp = uri;
        if (tmp.startsWith("/")) {
            result = new ActorPath();
            tmp = tmp.substring(1);
        }
        final String[] uris = tmp.split("/");
        try {
            for (final String s : uris) {
                if (s.equals("..")) {
                    result = result.parent();
                } else {
                    result = result.createChild(s);
                }
            }
            return result;
        } catch (final Exception e) {
            throw new MalformedURLException("Cannot apply " + uri + " to '"
                    + path + "'");
        }
    }

    public static ActorPath apply(final String uri)
            throws MalformedURLException {
        return apply(new ActorPath(), uri);
    }

    private static boolean validateName(final String name) {
        return name.matches("[a-zA-Z0-9_-]");
    }

    public List<String> toList() {
        return new LinkedList<>(Arrays.asList(pathStack));
    }
}
