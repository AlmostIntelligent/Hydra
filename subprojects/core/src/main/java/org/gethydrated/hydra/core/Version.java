package org.gethydrated.hydra.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Hydra version information.
 */
public final class Version {

    private Version() { }
    
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = Version.class
                .getResourceAsStream("version.properties")) {
            PROPS.load(is);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the full version string.
     * @return full version string.
     */
    public static String getVersionString() {
        return PROPS.getProperty("version");
    }

    /**
     * Returns the major version number.
     * @return major version number.
     */
    public static String getMajorVersion() {
        final String version = PROPS.getProperty("version");
        return version.substring(0, version.indexOf("."));
    }

    /**
     * Returns the minor version number.
     * @return minor version number.
     */
    public static String getMinorVersion() {
        final String version = PROPS.getProperty("version");
        return version.substring(version.indexOf(".") + 1,
                version.lastIndexOf("."));
    }

    /**
     * Returns the patch number.
     * @return patch number.
     */
    public static String getPatchVersion() {
        String version = PROPS.getProperty("version");
        if (version.contains("-")) {
            version = version.substring(0, version.indexOf("-"));
        }
        return version.substring(version.lastIndexOf(".") + 1);
    }

    /**
     * Returns if the version is a snapshot or release version.
     * @return true if the version is a snapshot.
     */
    public static boolean isSnapshot() {
        return PROPS.getProperty("version").toLowerCase().contains("snapshot");
    }
}
