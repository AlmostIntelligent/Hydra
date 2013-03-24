package org.gethydrated.hydra.core.cli;

import org.gethydrated.hydra.api.event.SystemEvent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CLIResponse implements SystemEvent {

    private String output;

    private String var;

    public CLIResponse(String output, String var) {
        this.output = output;
        this.var = var;
    }

    private CLIResponse() {}

    public CLIResponse(String output) {
        this(output, "");
    }

    public String getOutput() {
        return output;
    }

    public String getVar() {
        return var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CLIResponse that = (CLIResponse) o;

        if (output != null ? !output.equals(that.output) : that.output != null) return false;
        if (var != null ? !var.equals(that.var) : that.var != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = output != null ? output.hashCode() : 0;
        result = 31 * result + (var != null ? var.hashCode() : 0);
        return result;
    }
}
