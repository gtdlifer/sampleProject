package com.gtdilfer.notautoreload.simple.builder;

import java.io.File;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ReadSaveXmlUtil {
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

	
	private static void saveConfigration() {
		/**
		 * After a configuration has been manipulated, it should probably be saved again
		 * to make the changes persistent. Otherwise, the changes are only in memory. If
		 * configurations are to be changed, it is preferrable to obtain them via a
		 * different mechanism: a configuration builder. Builders are the most powerful
		 * and flexible way to construct configurations. They support many settings that
		 * impact the way the configuration data is loaded and the resulting
		 * configuration object behaves. Builders for file-based configurations also
		 * offer a save() method that writes all configuration data back to disk.
		 * Configuration builders are typically created using a fluent API which allows
		 * a convenient and flexible configuration of the builder. This API is described
		 * in the section Configuration builders. For simple use cases, the
		 * Configurations class we have already used has again some convenience methods.
		 * The following code fragment shows how a configuration is read via a builder,
		 * manipulated, and finally saved again:
		 */
		Configurations configs = new Configurations();
		try {
			// obtain the configuration
			FileBasedConfigurationBuilder<XMLConfiguration> builder = configs.xmlBuilder("paths.xml");
			XMLConfiguration config = builder.getConfiguration();

			// update property
			config.addProperty("newProperty", "newValue");

			// save configuration
			builder.save();
		} catch (ConfigurationException cex) {
			// Something went wrong
		}

	}

	public static void main(String[] args) {
	
//		loadProp();
//		readProp();
	}
}
