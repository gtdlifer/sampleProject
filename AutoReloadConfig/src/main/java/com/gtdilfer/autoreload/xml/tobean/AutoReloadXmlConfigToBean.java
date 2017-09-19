/**
 * 
 */
package com.gtdilfer.autoreload.xml.tobean;

import com.gtdilfer.autoreload.xml.tobean.bean.XmlConfig;

/**
 * @author WUXIAOW
 * 
 *         <Pre>
 *         使用commons-configuration2包对xml文件读取
 *         如果需要直接将xml文件转换成javaBean则需要进行复杂的操作，或者是通过具体的javaBean内进行解析，才能实现。
 *         </pre>
 */
public class AutoReloadXmlConfigToBean {

	public static String xmlFilePath = "test.xml";

	@SuppressWarnings("rawtypes")
	public static Class xmlMappingClass = XmlConfig.class;

	/**
	 * unit: second
	 */
	private static int autoReloadConfigInterval = 1;

	@SuppressWarnings("unchecked")
	public static AutoReloadXmlFileFactory<XmlConfig> configObject = new AutoReloadXmlFileFactory<XmlConfig>(
			xmlFilePath, xmlMappingClass, autoReloadConfigInterval);

	public static XmlConfig getInstance() {
		return configObject.getInstance();
	}

	public static void main(String[] args) throws InterruptedException {
		
		while (true) {
			System.out.println(AutoReloadXmlConfigToBean.getInstance().getVersion());
		}

	}
}
