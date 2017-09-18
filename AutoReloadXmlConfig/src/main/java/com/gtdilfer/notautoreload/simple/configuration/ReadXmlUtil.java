package com.gtdilfer.notautoreload.simple.configuration;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ReadXmlUtil {
	private static XMLConfiguration config = null;
	static {
		loadProp();
	}

	private static void loadProp() {
		/**
		 * Reading this file works analogously to reading a properties file. Again a
		 * Configurations instance is needed (by the way, this class is thread-safe, and
		 * an instance can be shared and reused to read multiple configuration sources),
		 * but this time we use the xml() method rather than properties():
		 */
		Configurations configs = new Configurations();
		try {
			config = configs.xml("paths.xml");
			// access configuration properties
			// ...
		} catch (ConfigurationException cex) {
			// Something went wrong
		}

	}

	private static void readProp() {
		/**
		 * Accessing properties in a XML configuration (or any other hierarchical
		 * configuration) supports the same query methods as for regular configurations.
		 * There are some additional facilities that take the hierarchical nature of
		 * these sources into account. The properties in the example configuration can
		 * be read in the following way:
		 */
		String stage = config.getString("processing[@stage]");
		List<String> paths = config.getList(String.class, "processing.paths.path");

		/**
		 * The keys for properties are generated by concatening the possibly nested tag
		 * names in the XML document (ignoring the root element). For attributes, there
		 * is a special syntax as shown for the stage property. Because the path element
		 * appears multiple times it actually defines a list. With the getList() method
		 * all values can be queried at once.
		 * 
		 * Hierarchical configurations support an advanced syntax for keys that allows a
		 * navigation to a specific element in the source document. This is achieved by
		 * adding numeric indices in parentheses after the single key parts. For
		 * instance, in order to reference the second path element in the list, the
		 * following key can be used (indices are 0-based):
		 */

		String secondPath = config.getString("processing.paths.path(1)");

		System.out.println(stage);
		System.out.println(paths);
		System.out.println(secondPath);
		
		System.out.println(config.getList(String.class, "newProperty"));

	}

	

	public static void main(String[] args) {
		readProp();

//		loadProp();
//		readProp();
	}
}
