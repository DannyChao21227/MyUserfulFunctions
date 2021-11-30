package UsefulFunction;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class AnalysisErrorXML {
	
	private String xmlFilePath = "TXT/ErrorXML.xml";
	private List<String> phError = new ArrayList<>();
	private List<String> loError = new ArrayList<>();
	private List<String> lbError = new ArrayList<>();
	private List<String> phErrorString = new ArrayList<>();
	private List<String> loErrorString = new ArrayList<>();
	private List<String> lbErrorString = new ArrayList<>();
	private Set<String> phErrorMsgid = new HashSet();
	private Set<String> loErrorMsgid = new HashSet();
	private Set<String> lbErrorMsgid = new HashSet();
	private Set<String> phErrorCode = new HashSet();
	private Set<String> loErrorCode = new HashSet();
	private Set<String> lbErrorCode = new HashSet();
	
	public static void main(String[] args) {
		AnalysisErrorXML aXML = new AnalysisErrorXML();
		aXML.readXML();
	}
	
	private void readXML() {
		File file = new File(xmlFilePath);
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(file);
			System.out.println("Root element : " + document.getRootElement().getName());
			System.out.println("============================================================");
			Element classElement = document.getRootElement();
			List<Node> nodes = classElement.selectNodes("httpSample");
			System.out.println(nodes.size());
			int msgidCount08 = 1, codeCount08 = 1;
			int msgidCount09 = 1, codeCount09 = 1;
			int msgidCount10 = 1, codeCount10 = 1;
			for(Node node : nodes) {
				String queryString = node.selectSingleNode("queryString").getText();
				String responseDataString = node.selectSingleNode("responseData").getText();
				queryString = queryString.replaceAll(" ", "");
				String msgid = queryString.substring(queryString.indexOf("\"MSGID\""));
				msgid = msgid.substring(0, msgid.indexOf(",")).split(":")[1].replaceAll("\"", "");
				String returncode = responseDataString.substring(responseDataString.indexOf("\"RETURNCODE\""));
				returncode = returncode.substring(0, returncode.indexOf(",")).split(":")[1].replaceAll("\"", "");
				if(queryString.contains("\"RegionId\":\"0008\"")) {
					if(phErrorMsgid.add(msgid)) {
						phErrorString.add(msgid + " / " + msgidCount08);
						msgidCount08 = 1;
					}
					if(phErrorCode.add(returncode)) {
						phErrorString.add(returncode + " / " + codeCount08);
						codeCount08 = 1;
					}
					phError.add("MSGID = " + msgid + " / " + "RegionId = 0008 / RETURNCODE = " + returncode);
					msgidCount08 += 1;
					codeCount08 += 1;
				} else if(queryString.contains("\"RegionId\":\"0009\"")) {
					if(loErrorMsgid.add(msgid)) {
						loErrorString.add(msgid + " / " + msgidCount09);
						msgidCount09 = 1;
					}
					if(loErrorCode.add(returncode)) {
						loErrorString.add(returncode + " / " + codeCount09);
						codeCount09 = 1;
					}
					loError.add("MSGID = " + msgid + " / " + "RegionId = 0009 / RETURNCODE = " + returncode);
					msgidCount09 += 1;
					codeCount09 += 1;
				} else {
					if(lbErrorMsgid.add(msgid)) {
						lbErrorString.add(msgid + " / " + msgidCount10);
						msgidCount10 = 1;
					}
					if(lbErrorCode.add(returncode)) {
						lbErrorString.add(returncode + " / " + codeCount10);
						codeCount10 = 1;
					}
					lbError.add("MSGID = " + msgid + " / " + "RegionId = 0010 / RETURNCODE = " + returncode);
					msgidCount10 += 1;
					codeCount10 += 1;
				}
			}
			System.out.println("phError.size() / loError.size() / lbError.size() = " + phError.size() + " / " + loError.size() + " / " + lbError.size());
			System.out.println("PH Error : ");
			for(String error : phErrorString) {
				System.out.println(error);
			}
			for(String error : phError) {
				System.out.println(error);
			}
			System.out.println("LO Error : ");
			for(String error : loErrorString) {
				System.out.println(error);
			}
			for(String error : loError) {
				System.out.println(error);
			}
			System.out.println("LB Error : ");
			for(String error : lbErrorString) {
				System.out.println(error);
			}
			for(String error : lbError) {
				System.out.println(error);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
