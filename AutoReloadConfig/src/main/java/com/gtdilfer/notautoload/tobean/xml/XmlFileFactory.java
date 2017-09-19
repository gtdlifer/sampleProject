package com.gtdilfer.notautoload.tobean.xml;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;

import com.alibaba.fastjson.JSON;

/**
 * @author WUXIAOW
 *
 */
public class XmlFileFactory {
	/***
	 * 将文件加载为文件对应的对象
	 * 
	 * @param filePath
	 * @param clazz
	 * @return
	 */
	public static <T> T loadFile(String filePath, Class<T> clazz) {
		T obj = null;
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<XMLConfiguration> builder = new FileBasedConfigurationBuilder<XMLConfiguration>(
				XMLConfiguration.class).configure(params.xml().setFileName(filePath));

		try {
			XMLConfiguration config = builder.getConfiguration();
			obj = loadObj(config, "", clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
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
	private static <T> T loadObj(XMLConfiguration config, String preProperty, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		T obj = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();
		int num = 0;
		for (Field field : fields) {
			field.setAccessible(true); // 设置些属性是可以访问的
			String name = field.getName();
			String key = preProperty + name;
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
								key + "." + toLowerFirst(componentType.getSimpleName()) + "(" + i + ").",
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
									key + "." + toLowerFirst(genericClazz.getSimpleName()) + "(" + i + ").",
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

	public static void main(String[] args) throws InterruptedException {
		Config config = XmlFileFactory.loadFile("test.xml", Config.class);
		System.out.println(JSON.toJSONString(config,true));
		while(true) {
			System.out.println(config.getVersion());
			Thread.sleep(3000);
		}

	}
}
