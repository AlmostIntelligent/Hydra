package org.gethydrated.hydra.launcher;

import java.net.URISyntaxException;

public class Launcher {

	private static String hydraDir;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if(setWorkingDirectory()) {
			System.out.println(hydraDir);
		}
	}

	private static boolean setWorkingDirectory() {
		try {
			String path =Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			path = path.substring(0, path.lastIndexOf('/'));
			hydraDir = path.substring(0, path.lastIndexOf('/')+1);
			return true;
		} catch (URISyntaxException e) {
			System.err.println("Could not set working directory");
			return false;
		}
	}
	
	public static void printUsage() {
		System.out.println("Hydra Usage:");
	}
}
