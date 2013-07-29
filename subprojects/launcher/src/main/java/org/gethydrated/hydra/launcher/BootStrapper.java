package org.gethydrated.hydra.launcher;

import org.jboss.modules.Module;

import java.lang.reflect.Method;

/**
 * Bootstraps Hydra.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 */
public final class BootStrapper {

    /**
     * Hide constructor to prevent instanciation.
     */
    private BootStrapper() {
    }

    /**
     * Gathers all Hydra related jars and attemps to lauch afterwards.
     * 
     * @param args
     *            arguments.
     * @throws Exception
     *             on failure while loading or starting Hydra.
     */
    public static void bootstrap(final String[] args)
            throws Exception {
        Module module = Module.forClass(BootStrapper.class);

        final Class<?> mainClass = module.getClassLoader()
                .loadClass("org.gethydrated.hydra.launcher.HydraStarter");
        final Method mainMethod = mainClass.getMethod("start", String[].class);
        Thread.currentThread().setContextClassLoader(module.getClassLoader());
        mainMethod.invoke(null, new Object[] {args});
    }
}
