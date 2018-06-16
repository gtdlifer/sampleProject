import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Test {
	public static void parse() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		DocumentBuilder domBuilder = factory.newDocumentBuilder();
		Document dom = domBuilder.parse(new File("d:/test1.xml"));

		// Document dom = domBuilder.parse(new FileInputStream(new
		// File("d:/test1.xml")));
		// deviceInformation =
		// FilteredImageSource.readFile(fileName,UiUtilPlugin.PLUGIN_ID);

		// InputStream iStream= new
		// ByteArrayInputStream("<request><pap_pwd>&#160;aa&#160;aa&#160;bb&#160;&#160;&#160;&#160;</pap_pwd></request>".getBytes("utf-8"));
		// Reader reader = new InputStreamReader(iStream,"GB2312");
		// InputSource iSource = new InputSource(reader);
		// iSource.setEncoding("GB2312");
		// Document dom = domBuilder.parse(iSource);
		// Document dom = domBuilder.parse(iStream);

		Element root = dom.getDocumentElement();
		Node child = root.getFirstChild();
		System.out.println("---" + root.getElementsByTagName("pap_pwd").item(0).getTextContent() + "---");
	}

	/**
	 * @param month
	 *            1,2,3,4,5,6
	 * @param dbType
	 *            mysql, oracle
	 * 
	 *            CDR_Query=com.hpe.batch.bo.command.MysqlCDRsQueryCommand
	 */
	public static StringBuilder genShellScript(String loginListFilePath, int month, String dbType) {
		LineIterator it = null;
		StringBuilder sb = new StringBuilder();
		try {
			it = FileUtils.lineIterator(new File(loginListFilePath), "UTF-8");
			while (it.hasNext()) {
				final String line = it.nextLine();
				String login = line;
				String script = String.format("/opt/query10k/73%s/testCDRsQuery2.sh localhost 22058 %s 20180%s|grep '<Result>' > %s/%s_%s.log", ".mysql", login, month, dbType,login, month);
				sb.append(script).append("\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (it != null) {
				try {
					it.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sb;

	}

	public static final String db_type_mysql = "mysql";
	public static final String db_type_oracle = "oracle";

	public static void genTotalScript() {
		try {
			
			StringBuilder mysqlTotalStr = new StringBuilder();
			mysqlTotalStr.append("mkdir /opt/query10k/compare\r\n");
			mysqlTotalStr.append("mkdir /opt/query10k/compare/").append(db_type_mysql).append("\r\n");
			mysqlTotalStr.append("cd /opt/query10k/compare/")
//			.append(db_type_mysql)
			.append("\r\n");
			// parse();
			for (int i = 1; i <= 6; i++) {
				mysqlTotalStr.append(genShellScript("d:/db_" + i + ".txt", i, db_type_mysql));
			}

			
			StringBuilder oracleTotalStr = new StringBuilder();
			oracleTotalStr.append("mkdir /opt/query10k/compare\r\n");
			oracleTotalStr.append("mkdir /opt/query10k/compare/").append(db_type_oracle).append("\r\n");
			oracleTotalStr.append("cd /opt/query10k/compare/")
//			.append(db_type_oracle)
			.append("\r\n");
			for (int i = 1; i <= 6; i++) {
				oracleTotalStr.append(genShellScript("d:/db_" + i + ".txt", i, db_type_oracle));
			}

			
			try {
				Path scriptPath = Paths.get("d:/mysql_script.sh");
				Files.write(scriptPath, mysqlTotalStr.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
				
				scriptPath = Paths.get("d:/oracle_script.sh");
				Files.write(scriptPath, oracleTotalStr.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("script error.");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void rerun0Log(String logListFilePath, String dbType) {
		LineIterator it = null;
		StringBuilder sb = new StringBuilder();
		try {
			it = FileUtils.lineIterator(new File(logListFilePath), "UTF-8");
			while (it.hasNext()) {
				final String line = it.nextLine();
				String logFileName = line;
				String login = logFileName.substring(0, logFileName.indexOf("_"));
				String month = logFileName.substring(logFileName.indexOf("_")+1,logFileName.indexOf("."));
				
				String script = String.format("/opt/query10k/73%s/testCDRsQuery2.sh localhost 22058 %s 20180%s|grep '<Result>' > %s/%s_%s.log", ".mysql", login, month, dbType,login, month);
				sb.append(script).append("\r\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (it != null) {
				try {
					it.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
//		return sb;
		try {
			Path scriptPath = Paths.get("d:/0log_mysql_script.sh");
			Files.write(scriptPath, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
			
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("script error.");
		}


	}
	
	

	public static void main(String[] args) {
		genTotalScript();
//		rerun0Log("d:/0log.txt", db_type_mysql);
	}
}
