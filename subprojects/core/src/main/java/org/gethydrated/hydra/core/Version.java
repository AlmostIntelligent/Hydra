package org.gethydrated.hydra.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 */
public class Version {

    private static final Properties props = new Properties();

    static {
        try (InputStream is = Version.class.getResourceAsStream("version.properties")) {
            props.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getVersionString() {
        return props.getProperty("version");
    }

    public static String getMajorVersion() {
        String version = props.getProperty("version");
        return version.substring(0, version.indexOf("."));
    }

    public static String getMinorVersion() {
        String version = props.getProperty("version");
        return version.substring(version.indexOf(".")+1, version.lastIndexOf("."));
    }

    public static String getPatchVersion() {
        String version = props.getProperty("version");
        if(version.contains("-")) {
            version = version.substring(0, version.indexOf("-"));
        }
        return version.substring(version.lastIndexOf(".")+1);
    }

    public static boolean isSnapshot() {
        return props.getProperty("version").toLowerCase().contains("snapshot");
    }

    public static void main(String[] args) {
        System.out.println(Version.getVersionString());
        System.out.println(Version.getMajorVersion());
        System.out.println(Version.getMinorVersion());
        System.out.println(Version.getPatchVersion());
        System.out.println(Version.isSnapshot());
    }
}
