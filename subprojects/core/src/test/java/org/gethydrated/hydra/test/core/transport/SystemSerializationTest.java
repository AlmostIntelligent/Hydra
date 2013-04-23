package org.gethydrated.hydra.test.core.transport;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;

import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

/**
 *
 */
public class SystemSerializationTest {

    private static ObjectMapper mapper;

    @BeforeClass
    public static void setup() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JaxbAnnotationModule());
    }

    @Test
    public void testInputEvent() throws IOException {
        final InputEvent ie = new InputEvent("test", null);
        final String s = mapper.writeValueAsString(ie);
        assertEquals("{\"input\":\"test\"}", s);
        final InputEvent iee = mapper.readValue(s, InputEvent.class);
        assertEquals(ie, iee);
    }

    @Test
    public void testCLIResponse() throws IOException {
        final CLIResponse cr = new CLIResponse("test", "var");
        final String s = mapper.writeValueAsString(cr);
        assertEquals("{\"output\":\"test\",\"var\":\"var\"}", s);
        final CLIResponse crr = mapper.readValue(s, CLIResponse.class);
        assertEquals(cr, crr);
    }
}
