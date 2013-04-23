package org.gethydrated.hydra.core.io.transport;

import org.gethydrated.hydra.api.service.USID;

import java.io.UnsupportedEncodingException;

/**
 * Serialized object holder.
 */
public class SerializedObject {

    private String format;

    private USID sender;

    private USID target;

    private String className;

    private byte[] data;

    /**
     * Constructor.
     */
    public SerializedObject() {
    }

    /**
     * Returns the serialization format.
     * @return serialization format.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets the serialization format.
     * @param format serialization format.
     */
    public void setFormat(final String format) {
        this.format = format;
    }

    /**
     * Returns the class name of the serialized object.
     * @return class name.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the class name of the serialized object.
     * @param className class name.
     */
    public void setClassName(final String className) {
        this.className = className;
    }

    /**
     * Returns the sender usid.
     * @return sender usid.
     */
    public USID getSender() {
        return sender;
    }

    /**
     * Sets the sender usid.
     * @param sender sender usid.
     */
    public void setSender(final USID sender) {
        this.sender = sender;
    }

    /**
     * Returns the target usid.
     * @return target usid.
     */
    public USID getTarget() {
        return target;
    }

    /**
     * Sets the target usid.
     * @param target target usid.
     */
    public void setTarget(final USID target) {
        this.target = target;
    }

    /**
     * Returns the binary data.
     * @return binary data.
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the binary data.
     * @param data binary data.
     */
    public void setData(final byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        try {
            return "SerializedObject{" + "format='" + format + '\''
                    + ", sender=" + sender + ", target=" + target
                    + ", className='" + className + '\'' + ", data="
                    + new String(data, "UFT-8") + '}';
        } catch (final UnsupportedEncodingException e) {
            return "SerializedObject{" + "format='" + format + '\''
                    + ", sender=" + sender + ", target=" + target
                    + ", className='" + className + '\'' + ", data=" + data
                    + '}';
        }
    }
}
