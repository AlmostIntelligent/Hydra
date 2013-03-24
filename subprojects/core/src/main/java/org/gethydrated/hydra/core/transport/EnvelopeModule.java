package org.gethydrated.hydra.core.transport;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 *
 */
public class EnvelopeModule extends SimpleModule {

    public EnvelopeModule() {
        addSerializer(Envelope.class, new EnvelopeSerializer());
        addDeserializer(Envelope.class, new EnvelopeDeserializer());
    }
}
