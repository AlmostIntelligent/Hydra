package org.gethydrated.hydra.api;

/**
 * Hydra exception hierarchy root.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class HydraException extends Exception {

    /**
     * Constructor.
     * 
     * @param e
     *            Throwable e.
     */
    public HydraException(final Throwable e) {
        super(e);
    }

    /**
     * Constructor.
     * @param m Message m.
     */
    public HydraException(final String m) {
        super(m);
    }

    /**
     * Constructor.
     * @param m Message m.
     * @param e Throwable e.
     */
    public HydraException(final String m, final Throwable e) {
        super(m, e);
    }

    /**
     * Serialization id.
     */
    private static final long serialVersionUID = 9050861266290634703L;

}
