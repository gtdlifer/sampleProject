package com.gtdilfer.notautoreload.simple.configuration;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * @author WUXIAOW
 * 
 *         only load config one time, will not reload properties
 */
public class ReadPropertyUtil {

	private static Configuration config = null;
	static {
		loadProp();
	}

	private static void loadProp() {
		Configurations configs = new Configurations();
		try {
			config = configs.properties(new File("config.properties"));
			// access configuration properties
			// ...
		} catch (ConfigurationException cex) {
			// Something went wrong
		}
	}

	private static void readProp() {
		String dbHost = config.getString("database.host");
		int dbPort = config.getInt("database.port");
		String dbUser = config.getString("database.user");
		String dbPassword = config.getString("database.password", "secret"); // provide a default
		long dbTimeout = config.getLong("database.timeout");

		System.out.println(dbHost);
		System.out.println(dbPort);
		System.out.println(dbUser);
		System.out.println(dbPassword);
		System.out.println(dbTimeout);

		/**
		 * Note that the keys passed to the get methods match the keys contained in the
		 * properties file. If a key cannot be resolved, the default behavior of a
		 * configuration is to return null. (Methods that return a primitive type throw
		 * an exception because in this case there is no null value.) It is possible to
		 * provide a default value which is used when the key cannot be found.
		 */

	}

	/**
	 * update properties in memory
	 */
	private static void updateProp() {
		/**
		 * The Configuration interface defines some methods for manipulating
		 * configuration properties. Typical CRUD operations are available for all
		 * properties. The following code fragment shows how the example properties
		 * configuration can be changed. The port of the database is changed to a new
		 * value, and a new property is added:
		 */

		config.setProperty("database.port", 8200);
		config.addProperty("database.type", "production");

		/**
		 * addProperty() always adds a new value to the configuration. If the affected
		 * key already exists, the value is added to this key, so that it becomes a
		 * list. setProperty() in contrast overrides an existing value (or creates a new
		 * one if the key does not exist). Both methods can be passed an arbitrary value
		 * object. This can also be an array or a collection, which makes it possible to
		 * add multiple values in a single step.
		 */
	}

	// private static void saveConfig() {
	// Configurations configs = new Configurations();
	// try {
	// // obtain the configuration
	// FileBasedConfigurationBuilder<XMLConfiguration> builder =
	// configs.xmlBuilder("paths.xml");
	// XMLConfiguration config = builder.getConfiguration();
  	//
	// // update property
	// config.addProperty("newProperty", "newValue");
	//
	// // save configuration
	// builder.save();
	// } catch (ConfigurationException cex) {
	// // Something went wrong
	// }
	//
	// }
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
			FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs
					.propertiesBuilder("config.properties");
			PropertiesConfiguration config = builder.getConfiguration();

			// update property
			config.addProperty("newProperty", "newValue");

			// save configuration
			builder.save();
		} catch (ConfigurationException cex) {
			// Something went wrong
		}

	}

	public static void main(String[] args) throws InterruptedException {
		while (true) {
			readProp();
			// updateProp();
//			readProp();
			// saveConfigration();
			// Thread.sleep(3000);
		}

	}
}
