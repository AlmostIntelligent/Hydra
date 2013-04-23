package org.gethydrated.hydra.api.event;

/**
 * Output event.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
public final class OutputEvent implements SystemEvent {

    private final String input;

    /**
     * Constructor.
     * @param s output string.
     */
    public OutputEvent(final String s) {
        input = s;
    }

    @Override
    public String toString() {
        return input;
    }
}
