package org.gethydrated.hydra.core.io.transport;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Envelope serialization module.
 */
public class EnvelopeModule extends SimpleModule {

    private static final long serialVersionUID = 2674571143700022583L;

    /**
     * Constructor.
     */
    public EnvelopeModule() {
        addSerializer(Envelope.class, new EnvelopeSerializer());
        addDeserializer(Envelope.class, new EnvelopeDeserializer());
    }
}
