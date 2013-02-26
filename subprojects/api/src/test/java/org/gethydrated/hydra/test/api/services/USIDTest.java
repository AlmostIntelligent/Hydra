package org.gethydrated.hydra.test.api.services;

import org.gethydrated.hydra.api.service.USID;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 *
 */
public class USIDTest {

    @Test
    public void testParse1() {
        USID usid = USID.parse("<0:0:0>");

        assertNotNull(usid);
        assertEquals(usid.nodeId,0);
        assertEquals(usid.typeId,0);
        assertEquals(usid.serviceId, 0);
    }

    @Test
    public void testParse2() {
        USID usid = USID.parse("<2:1:3>");

        assertNotNull(usid);
        assertEquals(usid.nodeId,2);
        assertEquals(usid.typeId,1);
        assertEquals(usid.serviceId, 3);
    }

    @Test(expected = RuntimeException.class)
    public void testParseFailure1() {
        USID usid = USID.parse(null);
    }

    @Test(expected = RuntimeException.class)
    public void testParseFailure2() {
        USID usid = USID.parse("");
    }

    @Test(expected = RuntimeException.class)
    public void testParseFailure3() {
        USID usid = USID.parse("notasid");
    }

    @Test(expected = RuntimeException.class)
    public void testParseFailure4() {
        USID usid = USID.parse("<ab:cd:ef>");
    }

    @Test(expected = RuntimeException.class)
    public void testParseFailure5() {
        USID usid = USID.parse("<0:2:0>");
    }
}
