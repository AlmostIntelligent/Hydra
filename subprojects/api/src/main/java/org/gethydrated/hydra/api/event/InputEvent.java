package org.gethydrated.hydra.api.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
public final class InputEvent implements SystemEvent {

	private String input;

    @XmlTransient
    private String source;
	
	public InputEvent(String s, String source) {
		this.input = s;
        this.source = source;
	}

    private InputEvent() {}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

	public String getInput() {
		return input;
	}

    public String toString() {
        return "InputEvent: '" + input + "' <source: " + source + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InputEvent that = (InputEvent) o;

        if (input != null ? !input.equals(that.input) : that.input != null) return false;
        if (source != null ? !source.equals(that.source) : that.source != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = input != null ? input.hashCode() : 0;
        result = 31 * result + (source != null ? source.hashCode() : 0);
        return result;
    }
}
