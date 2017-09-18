package com.gtdilfer.autoreload.property;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WUXIAOW
 *
 */
public class AutoReloadPropertyUtil {
	private static final Logger logger = LoggerFactory.getLogger(AutoReloadPropertyUtil.class);
	public static PropertiesConfiguration properties = null;
	public static ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> reloadbuilder = null;
	private boolean isSucess; 
	private boolean initializeConfig() {
		
		try {
			logger.debug("LOADING PROPERTIES........");
			Parameters params = new Parameters();
			File propertiesFile = new File(URLDecoder.decode(getPropertiesFilePath(), "utf-8"));

//			ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> 
			reloadbuilder = new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(
					PropertiesConfiguration.class)
							.configure(params.fileBased().setEncoding("utf-8").setFile(propertiesFile));
			logger.debug("SETTING PROPERTIES TO AUTO SAVE........");
			reloadbuilder.setAutoSave(true);
			PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(reloadbuilder.getReloadingController(),
					null, 1, TimeUnit.SECONDS);
			trigger.start();

			reloadbuilder.addEventListener(ConfigurationBuilderEvent.ANY, new EventListener<ConfigurationBuilderEvent>() {

	            public void onEvent(ConfigurationBuilderEvent event) {
	            	
	                System.err.println("Event:" + event);
	            }
	        });
			
			

		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException in AbstractConfigUtil.initializeConfig()", e);
		}
		isSucess = properties != null;
		return isSucess;
	}

	public boolean isSucess() {
		return isSucess;
	}
	
	public PropertiesConfiguration getConfig() {
		try {
			properties = (PropertiesConfiguration) reloadbuilder.getConfiguration();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
	}
	private String getPropertiesFilePath() {
		logger.info("LOAD PROPERTIES FROM: "
				+ Thread.currentThread().getContextClassLoader().getResource(getPropertiesFilename()).getPath());
		return Thread.currentThread().getContextClassLoader().getResource(getPropertiesFilename()).getPath();
	}

	// description 由子类提供properties文件名,properties文件必须位于resources目录下
	public String getPropertiesFilename() {
//	public abstract String getPropertiesFilename() {
		return "config2.properties";
	}
	public static void main(String[] args) {
		AutoReloadPropertyUtil util = new AutoReloadPropertyUtil();
		util.initializeConfig();
		
		while(true) {
			System.out.println(util.getConfig().getInt("database.port"));
			
		}
	}
}
