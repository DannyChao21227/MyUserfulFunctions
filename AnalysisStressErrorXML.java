package UsefulFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import Entity.TransactionInfo;

public class AnalysisStressErrorXML {

	private String path = "XML/ErrorContent.xml";
	private File file = new File(path);
	private StringBuilder contentString = new StringBuilder();
	private double totalError = 0D;

	private List<String> fileContentList = new ArrayList<>();
	private List<TransactionInfo> tranInfo = new ArrayList<>();
	private Map<String, List<TransactionInfo>> resultMap = new HashMap<>();
	private StringBuilder allLogs = new StringBuilder();

	public static void main(String[] args) {
		String repo = "eai-bl-bos";
		int threads = 51;
		int time = 900;
		String version = "Exclusive";
//		String version = "UAT";
//		String version = "PROD";
//		String version = "useMessageAsID";
		String title = "\t\tTest and Report informations\r\n" + 
				"\r\n" + 
				"Source file	\"20211129154624.csv\"\r\n" + 
				"Start Time	\"11/29/21 3:46 PM\"\r\n" + 
				"End Time	\"11/29/21 4:01 PM\"\r\n" + 
				"Argument	-Jthreads=17 -JrampUp=10 -Jloop=-1 -Jduration=900 -JconnTimeout=35000 -JrespTimeout=35000\r\n" + 
				"enableScheduler = true | enableQPM = false | enableError_Content = true | enableSuccess_Content = false | enableKafka =\n\n";
		title = title.replaceAll("([0-9]{2})/([0-9]{2})/([0-9]{2})", "20$3/$1/$2");
		AnalysisStressErrorXML errorXML = new AnalysisStressErrorXML();
		errorXML.analysis();
		errorXML.showResult();
		System.out.println(title + errorXML.getAllLogs().toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hhmmss");
		Date date = new Date();
		String today = sdf.format(date);
		File file = new File("StressError/" + today + repo + "StressError" + "TR-" + threads + "TI-" + time + "VE-" + version);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Create File Failed!!!");
            }
        }
        
        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(title + errorXML.getAllLogs().toString());
            bw.flush();
            bw.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
	}

	public AnalysisStressErrorXML() {
		readFile();
	}

	private void readFile() {
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
			BufferedReader br = new BufferedReader(reader);
			if (br != null) {
				String line = br.readLine();
				fileContentList.add(line);
				// logs.add(line.replace("\"", "").replaceAll("\",\"2021-10", " ; 2021-10"));
				while (line != null) {
					line = br.readLine();
					if (line != null) {
						fileContentList.add(line);
					}

				}
				br.close();
//				System.out.println("Total File Content Lines = " + fileContentList.size());
				allLogs.append("Total File Content Lines = " + fileContentList.size() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int i = 0, len = fileContentList.size(); i < len; i++) {
			contentString.append(fileContentList.get(i));
		}
	}

	private void analysis() {
		Document doc = Jsoup.parse(contentString.toString().replaceAll("<[/]{0,1}html>", "").replaceAll("<[/]{0,1}body>", "").replaceAll("<[/]{0,1}h1>", ""));
		Elements elements = doc.select("httpSample");
//		System.out.println("Total Error : " + elements.size());
		allLogs.append("Total Error : " + elements.size() + "\n");
		totalError = elements.size();
		for (Element element : elements) {
			TransactionInfo tInfo = new TransactionInfo();
			tInfo.setMsgId(element.attr("lb"));
			tInfo.setCostTime(element.attr("t"));
			tInfo.setUp(getRequest(element));
			tInfo.setDown(getResponse(element));
			tInfo.setReturnCode(getReturnCode(tInfo.getDown()));
			tInfo.setReturnDesc(getReturnDesc(tInfo.getDown()));
			tInfo.setRegionId(getRegionId(tInfo.getUp()));
			tInfo.setO360Seq(getO360Seq(tInfo.getDown()));
			tranInfo.add(tInfo);
		}
		analysisResult();
	}

	private void analysisResult() {
		for (TransactionInfo info : tranInfo) {
			if (resultMap.get(info.getReturnCode()) == null) {
				resultMap.put(info.getReturnCode(), new ArrayList<>());
			}
			resultMap.get(info.getReturnCode()).add(info);
		}
	}

	public void showResult() {
		resultMap.keySet().forEach(returnCode -> {
			List<TransactionInfo> list = resultMap.get(returnCode);
//			System.out.println(returnCode + " / " + list.get(0).getReturnDesc() + " : ");
			allLogs.append(returnCode + " : " + list.get(0).getReturnDesc() + "\n");
			int totalSize = list.size();
			DecimalFormat df = new DecimalFormat("0.00");
//			System.out.println("總共 : " + totalSize + "筆 (" + df.format((double) totalSize * 100 / totalError) + " %)");
			allLogs.append("總共 : " + totalSize + "筆 (" + df.format((double) totalSize * 100 / totalError) + " %)" + "\n");
			Map<String, List<TransactionInfo>> msgIdList = new HashMap<>();
			for (TransactionInfo info : list) {
				if (msgIdList.get(info.getMsgId()) == null) {
					msgIdList.put(info.getMsgId(), new ArrayList<>());
				}
				msgIdList.get(info.getMsgId()).add(info);
			}
			Map<String, List<TransactionInfo>> msgIdTree = new TreeMap<>(msgIdList);
			msgIdTree.keySet().forEach(msgId -> {
				List<TransactionInfo> tranList = msgIdList.get(msgId);
//				System.out.println(StringUtils.rightPad(msgId, 20) + " : Total = " + tranList.size());
				allLogs.append(StringUtils.rightPad(msgId, 20) + " : Total = " + tranList.size() + "\n");
//				int vnCount = 0, sgCount = 0, phCount = 0, shCount = 0;
//				for (TransactionInfo info : tranList) {
//					switch (info.getRegionId()) {
//						case "0004":
//							sgCount += 1;
//							break;
//						case "0006":
//							shCount += 1;
//							break;
//						case "0007":
//							vnCount += 1;
//							break;
//						case "0008":
//							phCount += 1;
//							break;
//					}
//				}
//				System.out.println("VN :" + StringUtils.leftPad(vnCount + "", (totalSize + "").length() + 1) 
//					+ ", SG :" + StringUtils.leftPad(sgCount + "", (totalSize + "").length() + 1)
//					+ ", PH :" + StringUtils.leftPad(phCount + "", (totalSize + "").length() + 1)
//					+ ", SH :" + StringUtils.leftPad(shCount + "", (totalSize + "").length() + 1));
				if(tranList.size() > 5 && totalSize >200) {
					Set<Integer> randomSet = new HashSet<>();
    				while(randomSet.size() < 5) {
    					randomSet.add((int) (tranList.size() * Math.random()));
    				}
    				for(int random : randomSet) {
    					allLogs.append("O360SEQ : " + StringUtils.rightPad(tranList.get(random).getO360Seq(), 20) + "\n");
    				}
				} else {
					for (int i = 0, len = tranList.size(); i < len; i++) {
    					TransactionInfo info = tranList.get(i);
//						System.out.println("O360SEQ : " + StringUtils.rightPad(info.getO360Seq(), 25));//  + StringUtils.rightPad(info.getMsgId(), 20) + info.getRegionId() + " " + info.getRegionIdDesc());
    					allLogs.append("O360SEQ : " + StringUtils.rightPad(info.getO360Seq(), 20) + "\n");
    				}
				}
				
			});
//			System.out.println();
			allLogs.append("\n");
		});
	}

	private String getRequest(Element element) {
		Elements elements = element.getElementsByTag("queryString");
		String request = "";
		for (Element e : elements) {
			request = e.text();
		}
		return request;
	}

	private String getResponse(Element element) {
		Elements elements = element.getElementsByTag("responseData");
		String response = "";
		for (Element e : elements) {
			response = e.text();
		}
		return response;
	}

	private String getReturnCode(String response) {
		if (response.contains("504 Gateway")) {
			return "504 Gateway";
		} else if (response.contains("java.net.SocketException")) {
			return "SocketException";
		} else if(response.contains("java.net.SocketTimeoutException")) {
			return "SocketTimeoutException";
		}
		String returnCode = "";
		String[] responseSplit = response.split(",");
		for (String s : responseSplit) {
			if (s.contains("\"RETURNCODE\"")) {
				s = s.replaceAll("\"", "");
				String[] sSplit = s.split(":");
				returnCode = sSplit[1];
				break;
			}
		}
		return returnCode.trim();
	}

	private String getReturnDesc(String response) {
		if (response.contains("504 Gateway")) {
			return response;
		} else if (response.contains("java.net.SocketException")) {
			if(response.contains("jmeter")) {
				return "Jmeter -> " + response.substring(0, 72) + " ...";
			} else {
				return response;
			}
			
		} else if (response.contains("java.net.SocketTimeoutException")) {
				return response;
		}
		String returnDesc = "";
		String[] responseSplit = response.split(",");
		for (String s : responseSplit) {
			if (s.contains("\"RETURNDESC\"")) {
				s = s.replaceAll("\"", "");
//				String[] sSplit = s.split(":");
				returnDesc = s.substring(s.indexOf(":") + 1);//Split[1];
				if(returnDesc.length() >= 90) {
					returnDesc = returnDesc.substring(0, 90) + "...";
				}
				break;
			}
		}
		return returnDesc;
	}

	private String getRegionId(String request) {
		String regionId = "";
		String[] requestSplit = request.split(",");
		for (String s : requestSplit) {
			if (s.contains("\"RegionId\"")) {
				s = s.replaceAll("\"", "");
				s = s.substring(s.indexOf("RegionId"));
				String[] sSplit = s.split(":");
				regionId = sSplit[1];
				break;
			}
		}
		return regionId.trim();
	}

	private String getO360Seq(String response) {
		String o360Seq = "";
		String[] responseSplit = response.split(",");
		for (String s : responseSplit) {
			if (s.contains("\"O360SEQ\"")) {
				s = s.replaceAll("\"", "").replaceAll("}", "");
				String[] sSplit = s.split(":");
				o360Seq = sSplit[1];
				break;
			}
		}
		return o360Seq.trim();
	}

	public StringBuilder getAllLogs() {
		return allLogs;
	}
	
}
