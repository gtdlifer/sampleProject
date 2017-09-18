/**
 * 
 */
package com.gtdilfer.autoreload.property.tobean;

import com.gtdilfer.autoreload.property.tobean.bean.PropertyConfig;
import com.gtdilfer.autoreload.xml.tobean.bean.XmlConfig;

/**
 * @author WUXIAOW
 * 
 *         <Pre>
 *         使用commons-configuration2包对properties文件读取
 *         如果需要直接将properties文件转换成javaBean则需要进行复杂的操作，或者是通过具体的javaBean内进行解析，才能实现。
 *         
 *         支持list
 *         
 *         </pre>
 */
public class AutoReloadPropertyConfigToBean {

	public static String propertyFilePath = "test.properties";

	@SuppressWarnings("rawtypes")
	public static Class propertyMappingClass = PropertyConfig.class;

	/**
	 * unit: second
	 */
	private static int autoReloadConfigInterval = 1;

	@SuppressWarnings("unchecked")
	public static AutoReloadPropertyFileFactory<PropertyConfig> configObject = new AutoReloadPropertyFileFactory<PropertyConfig>(
			propertyFilePath, propertyMappingClass, autoReloadConfigInterval);

	public static PropertyConfig getInstance() {
		return configObject.getInstance();
	}

	public static void main(String[] args) throws InterruptedException {
		
		while (true) {
			System.out.println(AutoReloadPropertyConfigToBean.getInstance().getVersion());
			System.out.println(AutoReloadPropertyConfigToBean.getInstance().getConfigVersion());
			System.out.println("names->"+AutoReloadPropertyConfigToBean.getInstance().getNames()[0]+", "+AutoReloadPropertyConfigToBean.getInstance().getNames()[1]);
		}

	}
}
