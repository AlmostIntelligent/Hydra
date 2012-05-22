package org.gethydrated.hydra.api;

public class HydraException extends Exception {

    public HydraException(Throwable e) {
        super(e);
    }

    public HydraException(String m) {
		super(m);
	}
    
    public HydraException(String m, Throwable e) {
		super(m, e);
	}

	/**
     * 
	 */
    private static final long serialVersionUID = 9050861266290634703L;

}
