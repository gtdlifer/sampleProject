package com.gtdilfer.autoreload.property.tobean;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ConfigurationBuilderEvent;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gtdilfer.notautoload.tobean.xml.Config;

/**
 * @author WUXIAOW
 * 
 *         <pre>
 *
 * properties file content	
 * config.version=2111
 * version=1
 * 
 * mapping class
 * 
 * private int configVersion;
 * private int version;
 *
 *         </pre>
 *
 */
public class AutoReloadPropertyFileFactory<T> {

	private static final Logger logger = LoggerFactory.getLogger(AutoReloadPropertyFileFactory.class);

	private String filePath;

	private Class<T> clazz; // config obj

	private int secondsToReload = 1;

	private ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder = null;
	private PeriodicReloadingTrigger trigger = null;

	private T instance;

	/**
	 * @return
	 */
	private ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> getBuilder() {
		return this.builder;
	}

	/**
	 * @param filePath
	 * @param clazz
	 * @param secondsToReload
	 */
	public AutoReloadPropertyFileFactory(final String filePath, final Class<T> clazz, int secondsToReload) {
		super();
		this.filePath = filePath;
		this.clazz = clazz;
		this.secondsToReload = secondsToReload;

		initObject(filePath, clazz);

	}

	/**
	 * 将文件加载为文件对应的对象
	 * 
	 * @param filePath
	 * @param clazz
	 */
	private void initObject(final String filePath, final Class<T> clazz) {
		builder = new ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
				.configure(new Parameters().properties().setEncoding("utf-8").setURL(Thread.currentThread().getContextClassLoader().getResource(filePath)));
		trigger = new PeriodicReloadingTrigger(builder.getReloadingController(), null, this.secondsToReload,
				TimeUnit.SECONDS);
		trigger.start();

		builder.addEventListener(ConfigurationBuilderEvent.ANY, new EventListener<ConfigurationBuilderEvent>() {

			public void onEvent(ConfigurationBuilderEvent event) {
				try {

					if (event.getEventType() == ConfigurationBuilderEvent.RESET) {
						instance = loadObj(builder.getConfiguration(), "", clazz);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		trigger.start();
		try {
			this.instance = loadObj(builder.getConfiguration(), "", clazz);
		} catch (InstantiationException e) {
			logger.error("", e);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回xml配置映射的对象
	 * 
	 * @return
	 */
	public T getInstance() {
		return instance;
	}

	/****
	 * 加载对象
	 * 
	 * @param config
	 * @param preProperty
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T> T loadObj(PropertiesConfiguration config, String preProperty, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		T obj = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		int num = 0;
		for (Field field : fields) {
			field.setAccessible(true); // 设置些属性是可以访问的
			String name = field.getName();

			// String key = preProperty + name;
			String key = preProperty + camelCaseToDotSeperate(name);

			Class<?> cls = field.getType();
			// 如果是枚举类型，直接跳过
			if (cls.isEnum()) {
				continue;
			}
			Object configValue = null;
			if (isBaseDataType(cls)) {// 如果是基础数据类型
				configValue = config.get(cls, key);
			} else if (cls.isArray()) {// 是否是数组
				Class<?> componentType = cls.getComponentType();
				if (isBaseDataType(componentType)) {
					configValue = config.getArray(componentType, key);
				} else {
					List list = new ArrayList();
					int i = 0;
					while (true) {
						Object loadObj = loadObj(config,
								key + "." + toLowerFirstEnhance(componentType.getSimpleName()) + "(" + i + ").",
								componentType);
						if (loadObj == null) {
							break;
						}
						list.add(loadObj);
						i++;
					}
					if (list.size() > 0) {
						Object array = Array.newInstance(componentType, list.size());
						for (int j = 0; j < list.size(); j++) {
							Array.set(array, j, list.get(j));
						}
						configValue = array;
					}
				}
			} else if (List.class.isAssignableFrom(cls)) {// 判断是否为list
				Type fc = field.getGenericType();
				if (fc == null) {
					continue;
				}
				if (fc instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) fc;
					Class<?> genericClazz = (Class<?>) pt.getActualTypeArguments()[0];
					if (isBaseDataType(genericClazz)) {
						configValue = config.getCollection(genericClazz, key, null);
					} else {
						List list = new ArrayList();
						int i = 0;
						while (true) {
							Object loadObj = loadObj(config,
									key + "." + toLowerFirstEnhance(genericClazz.getSimpleName()) + "(" + i + ").",
									genericClazz);
							if (loadObj == null) {
								break;
							}
							list.add(loadObj);
							i++;
						}
						configValue = list;
					}
				}
			} else {// 其他则认为是对象
				configValue = loadObj(config, key + ".", cls);
			}
			if (configValue != null) {
				field.set(obj, configValue);
				num++;
			}
		}
		if (num == 0) {
			return null;
		}
		return obj;
	}

	/***
	 * 首字母小写
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerFirst(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'A' && ch[0] <= 'Z') {
			ch[0] = (char) (ch[0] + 32);
		}
		return new String(ch);
	}

	public static String toUpperFirst(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	/**
	 * @param str
	 * @return
	 */
	public static String camelCaseToDotSeperate(String str) {

		char[] ch = str.toCharArray();
		char[] newCh = new char[ch.length * 2];

		int j = 0;
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] >= 'A' && ch[i] <= 'Z') {
				ch[i] = (char) (ch[i] + 32);
				// list.add('.');
				newCh[j++] = '.';
			} else {

			}
			// list.add(ch[i]);
			newCh[j++] = ch[i];
		}

		return new String(newCh).substring(0, j);
	}

	/**
	 * 首字母小写，去掉点号，并将点号后第一个字母改成大写
	 * 
	 * 如: config.version - > configVersion
	 * 
	 * @param str
	 * @return
	 */
	public static String toLowerFirstEnhance(String str) {
		if (str != null) {
			StringBuffer name = new StringBuffer();
			/**
			 * <pre>
			 * 1、如果用“.”作为分隔的话,必须是如下写法,String.split("\\."),这样才能正确的分隔开,不能用String.split(".");
			 *
			 * 2、如果用“|”作为分隔的话,必须是如下写法,String.split("\\|"),这样才能正确的分隔开,不能用String.split("|");
			 *
			 *“.”和“|”都是转义字符,必须得加"\\";
			 * </pre>
			 */
			String[] parts = str.split("\\.");

			for (int i = 0; i < parts.length; i++) {
				if (i == 0) {
					name.append(toLowerFirst(parts[i]));
				} else {
					name.append(toUpperFirst(parts[i]));
				}

			}
			return name.toString();
		}

		return null;
	}

	/***
	 * 判断是否是基础数据类型
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean isBaseDataType(Class clazz) {
		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class)
				|| clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.isPrimitive());
	}

	/**
	 * 
	 * raw Configuration. 根据字段名查询字段值
	 * 
	 * @return
	 */
	public PropertiesConfiguration getConfig() {
		try {
			return this.builder.getConfiguration();
		} catch (ConfigurationException e) {
			logger.error("", e);
		}
		return null;
	}

	public static void main(String[] args) throws InterruptedException {

		System.out.println(camelCaseToDotSeperate("InterruptedException"));
		System.out.println(camelCaseToDotSeperate("main"));
		System.out.println(camelCaseToDotSeperate("XmlFileFactory"));
		// if(true)return ;

		// Config config = XmlFileFactory.loadFile("test.xml", Config.class);
		// System.out.println(JSON.toJSONString(config,true));
		// while(true) {
		// System.out.println(config.getVersion());
		// Thread.sleep(3000);
		// }
		// if (true) return;
		// System.out.println(toLowerFirstEnhance("config.version"));
		// System.out.println(toLowerFirstEnhance("config.version.test"));
		// System.out.println(toLowerFirstEnhance("Tconfig.trsion"));

		AutoReloadPropertyFileFactory<Config> propertyConfigFacotry = new AutoReloadPropertyFileFactory<Config>(
				"test.properties", Config.class, 1);
		// System.out.println(propertyConfigFacotry.getConfig().getInt("configVersion"));
		System.out.println(propertyConfigFacotry.getConfig().getInt("config.version"));
		System.out.println(propertyConfigFacotry.getInstance().getConfigVersion());
		System.out.println("names->" + propertyConfigFacotry.getInstance().getNames()[0] + ", "
				+ propertyConfigFacotry.getInstance().getNames()[1]);
		// if (true)
		// return;
		while (true) {
			Thread.sleep(5000);
			System.out.println("------");
			System.out.println("config version->" + propertyConfigFacotry.getInstance().getConfigVersion());
			System.out.println("version->" + propertyConfigFacotry.getInstance().getVersion());

			System.out.println("version->" + propertyConfigFacotry.getConfig().getInt("version"));

		}

	}
}
