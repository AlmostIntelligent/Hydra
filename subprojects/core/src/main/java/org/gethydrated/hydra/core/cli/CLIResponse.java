package org.gethydrated.hydra.core.cli;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.gethydrated.hydra.api.event.SystemEvent;

/**
 * CLI response.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CLIResponse implements SystemEvent {

    private String output;

    private String var;

    /**
     * Constructor.
     * @param output cli output.
     * @param var cli var output.
     */
    public CLIResponse(final String output, final String var) {
        this.output = output;
        this.var = var;
    }

    @SuppressWarnings("unused")
    private CLIResponse() {
    }

    /**
     * Constructor.
     * @param output cli output.
     */
    public CLIResponse(final String output) {
        this(output, "");
    }

    /**
     * Returns the output.
     * @return output.
     */
    public String getOutput() {
        return output;
    }

    /**
     * Returns the var output.
     * @return var output.
     */
    public String getVar() {
        return var;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CLIResponse that = (CLIResponse) o;

        if (output != null ? !output.equals(that.output) : that.output != null) {
            return false;
        }
        if (var != null ? !var.equals(that.var) : that.var != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = output != null ? output.hashCode() : 0;
        result = 31 * result + (var != null ? var.hashCode() : 0);
        return result;
    }
}
