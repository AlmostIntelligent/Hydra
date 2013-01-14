package org.gethydrated.hydra.test.actors;

import org.gethydrated.hydra.actors.ActorPath;
import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;

import static org.junit.Assert.*;

public class ActorPathTest {

    @Test
    public void testRootActorPathToString() {
        ActorPath a = new ActorPath();

        assertTrue(a.isRoot());
        assertEquals("/", a.toString());
    }

    @Test
    public void testRootActorPathEquals() {
        ActorPath a = new ActorPath();
        ActorPath b = new ActorPath();

        assertTrue(a.equals(b));
    }

    @Test
    public void testRootActorPathSerialization() throws IOException, ClassNotFoundException {
        ActorPath a = new ActorPath();
        byte[] sa = serialize(a);
        ActorPath b = deserialize(sa);

        assertFalse(a == b);
        assertTrue(a.equals(b));
    }

    @Test
    public void testActorPathCreateChild() {
        ActorPath a = new ActorPath();
        ActorPath b = a.createChild("child");

        assertEquals("/child", b.toString());
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

        assertEquals("/test1/test2/test3",a.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testActorPathApplyError() throws MalformedURLException {
        ActorPath a = new ActorPath();
        a = ActorPath.apply(a, "../");
    }

    private byte[] serialize(ActorPath path) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(path);
        oos.close();
        return baos.toByteArray();
    }

    private ActorPath deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (ActorPath) ois.readObject();
    }
}
