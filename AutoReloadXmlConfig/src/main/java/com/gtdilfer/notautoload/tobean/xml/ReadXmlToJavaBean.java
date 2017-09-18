/**
 * 
 */
package com.gtdilfer.notautoload.tobean.xml;

import com.alibaba.fastjson.JSON;

/**
 * @author WUXIAOW
 * 
 *         <Pre>
 *         使用commons-configuration2包对xml文件读取
 *         如果需要直接将xml文件转换成javaBean则需要进行复杂的操作，或者是通过具体的javaBean内进行解析，才能实现。
 *         </pre>
 */
public class ReadXmlToJavaBean {
	public static void main(String[] args) throws InterruptedException {
		Config config = XmlFileFactory.loadFile("test.xml", Config.class);
		System.out.println(JSON.toJSONString(config, true));
		while (true) {
			System.out.println(config.getVersion());
			Thread.sleep(3000);
		}
	}
}
