import java.awt.image.FilteredImageSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
 

public class Test {
	public static void parse() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    factory.setValidating(false);
	    DocumentBuilder domBuilder = factory.newDocumentBuilder();
	    Document dom =  domBuilder.parse(new File("d:/test1.xml")); 
	    
//	    Document dom = domBuilder.parse(new FileInputStream(new File("d:/test1.xml")));
//	    deviceInformation = FilteredImageSource.readFile(fileName,UiUtilPlugin.PLUGIN_ID);
	    
	    
//	    InputStream iStream= new ByteArrayInputStream("<request><pap_pwd>&#160;aa&#160;aa&#160;bb&#160;&#160;&#160;&#160;</pap_pwd></request>".getBytes("utf-8"));
//	    Reader reader = new InputStreamReader(iStream,"GB2312");
//	    InputSource iSource = new InputSource(reader);
//	    iSource.setEncoding("GB2312");
//	    Document dom = domBuilder.parse(iSource);
//	    Document dom = domBuilder.parse(iStream);
	    
	    Element root = dom.getDocumentElement();
	    Node child = root.getFirstChild();
	    System.out.println("---"+root.getElementsByTagName("pap_pwd").item(0).getTextContent()+"---");
	}
	public static void main(String[] args) {		
		try {
			parse();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
