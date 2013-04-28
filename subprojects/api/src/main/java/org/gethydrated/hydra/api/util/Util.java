package org.gethydrated.hydra.api.util;

/**
 * Utility class.
 */
public final class Util {

    private Util() { }
    
    /**
     * Checks if the given throwable is considered fatal. Based on scalas
     * scala.util.control.NonFatal object.
     * 
     * Throwables considered fatal are `VirtualMachineError` (e.g.
     * `OutOfMemoryError`), `ThreadDeath` or `LinkageError`.
     * 
     * @param t
     *            Throwable to match.
     * @return true if the given Throwable is considered non fatal.
     */
    public static boolean isNonFatal(final Throwable t) {
        if (t instanceof VirtualMachineError) {
            return false;
        }
        if (t instanceof ThreadDeath) {
            return false;
        }
        if (t instanceof LinkageError) {
            return false;
        }
        return true;
    }

    /**
     * Rethrows a checked Exception as unchecked, without altering the stack
     * trace.
     * @param checkedException source exception.
     */
    public static void throwUnchecked(final Throwable checkedException) {
        Util.<RuntimeException>thrownInsteadOf(checkedException);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void thrownInsteadOf(final Throwable t)
            throws T {
        throw (T) t;
    }
}
