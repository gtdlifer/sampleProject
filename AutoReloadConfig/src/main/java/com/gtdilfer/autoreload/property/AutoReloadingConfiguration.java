package com.gtdilfer.autoreload.property;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.ConfigurationDecoder;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;

public class AutoReloadingConfiguration implements ImmutableConfiguration {
	
	private ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder;
	private PeriodicReloadingTrigger trigger;

	public AutoReloadingConfiguration(File propertiesFile, int secondsToReload) {
		builder = new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
				.configure(new Parameters().fileBased().setFile(propertiesFile));
		trigger = new PeriodicReloadingTrigger(builder.getReloadingController(), null, secondsToReload, TimeUnit.SECONDS);
		trigger.start();
		builder.addEventListener(ConfigurationBuilderEvent.ANY, new EventListener<ConfigurationBuilderEvent>() {

            public void onEvent(ConfigurationBuilderEvent event) {
            	
                System.err.println("Event:" + event);
            }
        });
	}
	
	private ImmutableConfiguration getConfig() {
		try {
			return builder.getConfiguration();
		} catch (ConfigurationException e) {
			throw new RuntimeException("Reloading config", e);
		}
	}
	
	public void shutdown() {
		trigger.shutdown();
	}

	public boolean isEmpty() {
		return getConfig().isEmpty();
	}

	public int size() {
		return getConfig().size();
	}

	public boolean containsKey(String key) {
		return getConfig().containsKey(key);
	}

	public Object getProperty(String key) {
		return getConfig().getProperty(key);
	}

	public Iterator<String> getKeys(String prefix) {
		return getConfig().getKeys(prefix);
	}

	public Iterator<String> getKeys() {
		return getConfig().getKeys();
	}

	public Properties getProperties(String key) {
		return getConfig().getProperties(key);
	}

	public boolean getBoolean(String key) {
		return getConfig().getBoolean(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return getConfig().getBoolean(key, defaultValue);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		return getConfig().getBoolean(key, defaultValue);
	}

	public byte getByte(String key) {
		return getConfig().getByte(key);
	}

	public byte getByte(String key, byte defaultValue) {
		return getConfig().getByte(key, defaultValue);
	}

	public Byte getByte(String key, Byte defaultValue) {
		return getConfig().getByte(key, defaultValue);
	}

	public double getDouble(String key) {
		return getConfig().getDouble(key);
	}

	public double getDouble(String key, double defaultValue) {
		return getConfig().getDouble(key, defaultValue);
	}

	public Double getDouble(String key, Double defaultValue) {
		return getConfig().getDouble(key, defaultValue);
	}

	public float getFloat(String key) {
		return getConfig().getFloat(key);
	}

	public float getFloat(String key, float defaultValue) {
		return getConfig().getFloat(key, defaultValue);
	}

	public Float getFloat(String key, Float defaultValue) {
		return getConfig().getFloat(key, defaultValue);
	}

	public int getInt(String key) {
		return getConfig().getInt(key);
	}

	public int getInt(String key, int defaultValue) {
		return getConfig().getInt(key, defaultValue);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return getConfig().getInt(key, defaultValue);
	}

	public long getLong(String key) {
		return getConfig().getLong(key);
	}

	public long getLong(String key, long defaultValue) {
		return getConfig().getLong(key, defaultValue);
	}

	public Long getLong(String key, Long defaultValue) {
		return getConfig().getLong(key, defaultValue);
	}

	public short getShort(String key) {
		return getConfig().getShort(key);
	}

	public short getShort(String key, short defaultValue) {
		return getConfig().getShort(key, defaultValue);
	}

	public Short getShort(String key, Short defaultValue) {
		return getConfig().getShort(key, defaultValue);
	}

	public BigDecimal getBigDecimal(String key) {
		return getConfig().getBigDecimal(key);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return getConfig().getBigDecimal(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return getConfig().getBigInteger(key);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return getConfig().getBigInteger(key, defaultValue);
	}

	public String getString(String key) {
		return getConfig().getString(key);
	}

	public String getString(String key, String defaultValue) {
		return getConfig().getString(key, defaultValue);
	}

	public String getEncodedString(String key, ConfigurationDecoder decoder) {
		return getConfig().getEncodedString(key, decoder);
	}

	public String getEncodedString(String key) {
		return getConfig().getEncodedString(key);
	}

	public String[] getStringArray(String key) {
		return getConfig().getStringArray(key);
	}

	public List<Object> getList(String key) {
		return getConfig().getList(key);
	}

	public List<Object> getList(String key, List<?> defaultValue) {
		return getConfig().getList(key, defaultValue);
	}

	public <T> T get(Class<T> cls, String key) {
		return getConfig().get(cls, key);
	}

	public <T> T get(Class<T> cls, String key, T defaultValue) {
		return getConfig().get(cls, key, defaultValue);
	}

	public Object getArray(Class<?> cls, String key) {
		return getConfig().getArray(cls, key);
	}

	@SuppressWarnings("deprecation")
	public Object getArray(Class<?> cls, String key, Object defaultValue) {
		return getConfig().getArray(cls, key, defaultValue);
	}

	public <T> List<T> getList(Class<T> cls, String key) {
		return getConfig().getList(cls, key);
	}

	public <T> List<T> getList(Class<T> cls, String key, List<T> defaultValue) {
		return getConfig().getList(cls, key, defaultValue);
	}

	public <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target) {
		return getConfig().getCollection(cls, key, target);
	}

	public <T> Collection<T> getCollection(Class<T> cls, String key, Collection<T> target, Collection<T> defaultValue) {
		return getConfig().getCollection(cls, key, target, defaultValue);
	}

	public ImmutableConfiguration immutableSubset(String prefix) {
		return getConfig().immutableSubset(prefix);
	}
	
}
