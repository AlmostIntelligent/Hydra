package org.gethydrated.hydra.core.service.locator;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;

import org.gethydrated.hydra.api.configuration.Configuration;
import org.gethydrated.hydra.core.service.ServiceInfo;
import org.gethydrated.hydra.core.service.ServiceInfoParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * System service locator.
 * 
 * @author Christian Kulpa
 * @since 0.1.0
 * 
 */
public class SystemServiceLocator implements ServiceLocator {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory
            .getLogger(SystemServiceLocator.class);

    /**
     * System service dir.
     */
    private final String systemServiceDir;

    /**
     * Constructor.
     * 
     * @param cfg
     *            Configuration
     */
    public SystemServiceLocator(final Configuration cfg) {
        systemServiceDir = System.getProperty("hydra.home") + "/service/system";
    }

    @Override
    public final ServiceInfo locate(final String name, final String version)
            throws IOException {
        LOG.debug("Trying to locate service - name: {} version: {}", name, version);
        File[] dir = new File(systemServiceDir).listFiles(new FileFilter() {
            public boolean accept(final File file) {
                return file.getAbsolutePath().endsWith(".jar");
            }
        });
        for (File f : dir) {
            System.out.println(f.getName());
            ServiceInfo si = checkJarInfo(f, name, version);
            if (si != null) {
                System.out.println("found: " + f.getName());
                return si;
            }
        }
        return null;
    }

    @Override
    public final ServiceInfo locate(final String name) throws IOException {
        return locate(name, null);
    }

    /**
     * 
     * @param f
     *            file
     * @param name
     *            service name
     * @param version
     *            service version
     * @return true if service name is found
     */
    private ServiceInfo checkJarInfo(final File f, final String name,
            final String version) {
        try (JarFile jf = new JarFile(f);
                InputStream is = jf.getInputStream(jf
                        .getEntry("HYDRA-INF/service.xml"))) {
            if (is != null) {
                ServiceInfo si = ServiceInfoParser.parse(is, name, version);
                if (si != null) {
                    si.addServiceJar(f.toURI().toURL());
                    return si;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
