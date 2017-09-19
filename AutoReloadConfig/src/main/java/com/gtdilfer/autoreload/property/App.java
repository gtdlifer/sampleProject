package com.gtdilfer.autoreload.property;

import java.io.File;

/**
 * Simple App to test auto reloading properties
 *
 */
public class App {
    public static void main( String[] args) throws InterruptedException {
	File propertiesFile = new File("config2.properties");

    	AutoReloadingConfiguration autoReloadingConfig = new AutoReloadingConfiguration(propertiesFile, 1);
        while (true) {
        	System.out.println( "database.port " + autoReloadingConfig.getInt("database.port"));
        	Thread.sleep(2000);
        }
    }
}
