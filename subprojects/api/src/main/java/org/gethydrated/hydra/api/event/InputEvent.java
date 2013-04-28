package org.gethydrated.hydra.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Input event. Mainly created by the standard input
 * actor.
 * 
 * @author Christian Kulpa
 * @since 0.2.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
public final class InputEvent implements SystemEvent {

    private String input;

    @XmlTransient
    private String source;

    /**
     * Constructor.
     * @param s input string.
     * @param source input source.
     */
    public InputEvent(final String s, final String source) {
        this.input = s;
        this.source = source;
    }

    @SuppressWarnings("unused")
    private InputEvent() {
    }

    /**
     * Returns the input source.
     * @return input source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the input source.
     * @param source input source.
     */
    public void setSource(final String source) {
        this.source = source;
    }

    /**
     * Returns the input string.
     * @return input string.
     */
    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "InputEvent: '" + input + "' <source: " + source + ">";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final InputEvent that = (InputEvent) o;

        if (input != null ? !input.equals(that.input) : that.input != null) {
            return false;
        }
        if (source != null ? !source.equals(that.source) : that.source != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = input != null ? input.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
