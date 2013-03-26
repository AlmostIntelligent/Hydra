package org.gethydrated.hydra.test.core.transport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.gethydrated.hydra.api.event.InputEvent;
import org.gethydrated.hydra.core.cli.CLIResponse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

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
        InputEvent ie = new InputEvent("test", null);
        String s = mapper.writeValueAsString(ie);
        assertEquals("{\"input\":\"test\"}",s);
        InputEvent iee = mapper.readValue(s, InputEvent.class);
        assertEquals(ie, iee);
    }

    @Test
    public void testCLIResponse() throws IOException {
        CLIResponse cr = new CLIResponse("test", "var");
        String s = mapper.writeValueAsString(cr);
        assertEquals("{\"output\":\"test\",\"var\":\"var\"}", s);
        CLIResponse crr = mapper.readValue(s, CLIResponse.class);
        assertEquals(cr, crr);
    }
}
