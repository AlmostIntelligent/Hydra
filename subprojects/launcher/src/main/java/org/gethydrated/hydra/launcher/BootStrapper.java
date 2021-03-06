package org.gethydrated.hydra.launcher;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
     * @param hydraHome
     *            hydra home directory.
     * @throws Exception
     *             on failure while loading or starting Hydra.
     */
    public static void bootstrap(final String[] args, final File hydraHome)
            throws Exception {
        final List<File> systemJars = new ArrayList<>();

        addFileSet(systemJars, new File(hydraHome, "lib"), ".jar");

        systemJars.add(new File(hydraHome, "conf"));

        final List<URL> hydra = new LinkedList<>();
        final List<URL> commons = new LinkedList<>();

        for (final File file : systemJars) {
            if (file.getName().contains("hydra-api")
                    || !file.getName().contains("hydra")) {
                commons.add(file.toURI().toURL());
            } else {
                hydra.add(file.toURI().toURL());
            }
        }

        final URLClassLoader commonsLoader = new URLClassLoader(
                convertList(commons), ClassLoader.getSystemClassLoader()
                        .getParent());
        final URLClassLoader rootLoader = new URLClassLoader(
                convertList(hydra), commonsLoader);

        final Class<?> mainClass = rootLoader
                .loadClass("org.gethydrated.hydra.launcher.HydraStarter");
        final Method mainMethod = mainClass.getMethod("start", String[].class);
        Thread.currentThread().setContextClassLoader(rootLoader);
        mainMethod.invoke(null, new Object[] {args});
    }

    /**
     * Adds files from an directory to a file list based on given file endings.
     * 
     * @param fileSet
     *            file list.
     * @param target
     *            target directory.
     * @param endings
     *            file endings.
     */
    private static void addFileSet(final List<File> fileSet, final File target,
            final String... endings) {
        final File[] files = target.listFiles(new FileFilter() {
            @Override
            public boolean accept(final File file) {
                for (final String s : endings) {
                    if (file.getAbsolutePath().endsWith(s)) {
                        return true;
                    }
                }
                return false;
            }
        });

        Collections.addAll(fileSet, files);
    }

    /**
     * Converts a list of URL to an arry.
     * 
     * @param list
     *            URL list.
     * @return URL array.
     */
    private static URL[] convertList(final List<URL> list) {
        final URL[] array = new URL[list.size()];
        int i = 0;
        for (final URL u : list) {
            array[i++] = u;
        }
        return array;
    }
}
