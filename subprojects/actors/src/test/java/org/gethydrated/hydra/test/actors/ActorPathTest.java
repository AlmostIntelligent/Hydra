package org.gethydrated.hydra.test.actors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;

import org.gethydrated.hydra.actors.ActorPath;
import org.junit.Test;

public class ActorPathTest {

    @Test
    public void testRootActorPathToString() {
        final ActorPath a = new ActorPath();

        assertTrue(a.isRoot());
        assertEquals("/", a.toString());
    }

    @Test
    public void testRootActorPathEquals() {
        final ActorPath a = new ActorPath();
        final ActorPath b = new ActorPath();

        assertTrue(a.equals(b));
    }

    @Test
    public void testRootActorPathSerialization() throws IOException,
            ClassNotFoundException {
        final ActorPath a = new ActorPath();
        final byte[] sa = serialize(a);
        final ActorPath b = deserialize(sa);

        assertFalse(a == b);
        assertTrue(a.equals(b));
    }

    @Test
    public void testActorPathCreateChild() {
        final ActorPath a = new ActorPath();
        final ActorPath b = a.createChild("child");

        assertEquals("/child", b.toString());
    }

    @Test
    public void testActorPathChildOf() throws MalformedURLException {
        final ActorPath root = new ActorPath();
        final ActorPath a = ActorPath.apply(root, "child1");
        final ActorPath b = ActorPath.apply(root, "child2");
        final ActorPath c = ActorPath.apply(root, "child1/grandchild1");

        assertTrue(a.isChildOf(root));
        assertTrue(b.isChildOf(root));
        assertTrue(c.isChildOf(root));
        assertTrue(c.isChildOf(a));
        assertFalse(c.isChildOf(b));
        assertFalse(a.isChildOf(b));
        assertFalse(b.isChildOf(a));
        assertFalse(root.isChildOf(a));
        assertFalse(root.isChildOf(b));
        assertFalse(root.isChildOf(root));
        assertFalse(root.isChildOf(root));
    }

    @Test
    public void testActorPathApplyRoot() throws MalformedURLException {
        ActorPath a = new ActorPath();
        a = ActorPath.apply(a, "/child/test");

        assertEquals("/child/test", a.toString());
    }

    @Test
    public void testActorPathApply() throws MalformedURLException {
        ActorPath a = new ActorPath();
        a = a.createChild("test1");
        a = ActorPath.apply(a, "test2/test3");

        assertEquals("/test1/test2/test3", a.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testActorPathApplyError() throws MalformedURLException {
        ActorPath a = new ActorPath();
        a = ActorPath.apply(a, "../");
    }

    private byte[] serialize(final ActorPath path) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(path);
        oos.close();
        return baos.toByteArray();
    }

    private ActorPath deserialize(final byte[] byteArray) throws IOException,
            ClassNotFoundException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        final ObjectInputStream ois = new ObjectInputStream(bais);
        return (ActorPath) ois.readObject();
    }
}
