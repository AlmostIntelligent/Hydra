package org.gethydrated.hydra.launcher;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class BootStrapper {

    public static void bootstrap(String[] args, File hydraHome)
            throws Exception {
        List<File> systemJars = new ArrayList<File>();

        addFileSet(systemJars, new File(hydraHome, "lib"), ".jar");
        // addFileSet(systemJars, new File(hydraHome, "conf"), ".xml", ".ini",
        // ".properties");

        systemJars.add(new File(hydraHome, "conf"));

        int i = 0;
        URL[] urls = new URL[systemJars.size()];
        for (File file : systemJars) {
            urls[i++] = file.toURI().toURL();
        }

        URLClassLoader rootLoader = new URLClassLoader(urls, null);
        Class<?> mainClass = rootLoader
                .loadClass("org.gethydrated.hydra.launcher.HydraStarter");
        Method mainMethod = mainClass.getMethod("start", String[].class);
        Thread.currentThread().setContextClassLoader(rootLoader);
        mainMethod.invoke(null, new Object[] { args });
    }

    private static void addFileSet(List<File> fileSet, File target,
            final String... endings) {
        File[] files = target.listFiles(new FileFilter() {
            public boolean accept(File file) {
                for (String s : endings) {
                    if (file.getAbsolutePath().endsWith(s)) {
                        return true;
                    }
                }
                return false;
            }
        });

        for (File f : files) {
            fileSet.add(f);
        }
    }
}
