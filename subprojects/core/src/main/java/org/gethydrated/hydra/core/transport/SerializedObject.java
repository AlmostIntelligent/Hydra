package org.gethydrated.hydra.core.transport;

import org.gethydrated.hydra.api.service.USID;

import java.io.UnsupportedEncodingException;

/**
 *
 */
public class SerializedObject {

    private String format;

    private USID sender;

    private USID target;

    private String className;

    private byte[] data;

    public SerializedObject() {}

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public USID getSender() {
        return sender;
    }

    public void setSender(USID sender) {
        this.sender = sender;
    }

    public USID getTarget() {
        return target;
    }

    public void setTarget(USID target) {
        this.target = target;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        try {
            return "SerializedObject{" +
                    "format='" + format + '\'' +
                    ", sender=" + sender +
                    ", target=" + target +
                    ", className='" + className + '\'' +
                    ", data=" + new String(data, "UFT-8") +
                    '}';
        } catch (UnsupportedEncodingException e) {
            return "SerializedObject{" +
                    "format='" + format + '\'' +
                    ", sender=" + sender +
                    ", target=" + target +
                    ", className='" + className + '\'' +
                    ", data=" + data +
                    '}';
        }
    }
}
