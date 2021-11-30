package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLTime {
	
	private String path = "XML";
	private String selector = "div.message-wrapper";
	private String timeFormat = "yyyy-MM-ddHH:mm:ss,SSS";
	private String totalContent = "";
	
	private List<String> xmlContent = new ArrayList<>();
	private List<List<String>> xmlContentList = new ArrayList<>();
	private Map<String, String> inToOutO360SeqTime = new HashMap<>();
	private Map<String, String> outToInO360SeqTime = new HashMap<>();
	private Map<String, Integer> o360SeqCostTime = new HashMap<>(); 
	private List<Elements> elementsList = new ArrayList<>();
	
	public static void main(String[] args) {
		HTMLTime htmleTime = new HTMLTime();
		htmleTime.getAllNeed();
	}
	
	public HTMLTime() {
		readXML();
	}
	
	private void readXML() {
		File file = new File(path);
		String[] fileList = file.list();
		for(String s : fileList) {
			if(s.startsWith("page")) {
    			file = new File(path + "/" + s);
                try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
                    BufferedReader br = new BufferedReader(reader);
                    if(br != null) {
                        String line = br.readLine();
                        xmlContent.add(line);
                        while(line != null) {
                            line = br.readLine();
                            xmlContent.add(line);
                        }
                        br.close();
                    }
                } catch(IOException e) {
                    e.printStackTrace();
                }
                if(xmlContent.size() > 0) {
                	xmlContentList.add(xmlContent);
                	xmlContent = new ArrayList<>();
                }
			}
		}
	}
	
	public void getAllNeed() {
		for(List<String> contentList : xmlContentList) {
			String xmlContentString = "";
    		for(String content : contentList) {
    			xmlContentString += content + "\n";
    		}
    		Document doc = Jsoup.parse(xmlContentString);
    		
    		Elements elements = doc.select(selector);
    		elementsList.add(elements);
    		for(Element element : elements) {
    			String text = element.text();
    			if(!text.startsWith("2021-10")) {
    				continue;
    			}
    			totalContent += text + "\n";
    			boolean flag = false;
    			if(text.contains("(In)")) {
    				flag = true;
    			}
    			String[] textSplits = text.split(" ");
    			String time = textSplits[0] + textSplits[1];
    			String o360Seq = "";
    			for(String textSplit : textSplits) {
    				if(textSplit.contains("O360SEQ[")) {
    					textSplit = textSplit.replace("O360SEQ[", "").replace("]", "");
    					o360Seq = textSplit;
    					if(flag) {
    						inToOutO360SeqTime.put(o360Seq, time);
    					} else {
    						outToInO360SeqTime.put(o360Seq, time);
    					}
    					
    				}
    			}
    		}
		}
		outToInO360SeqTime.keySet().forEach(tToF -> {
			String time1 = inToOutO360SeqTime.get(tToF);
			String time2 = outToInO360SeqTime.get(tToF);
			o360SeqCostTime.put(tToF, countTime(time1, time2));
//			System.out.println(StringUtils.rightPad(tToF, 30, " ") + "cost time : " + countTime(time1, time2) + " ms");
		});
		
		o360SeqCostTime.keySet().forEach(cost -> {
			int time1 = o360SeqCostTime.get(cost);
			if(time1 > 15000) {
				String showString = cost + "\n";
				for(Elements elements : elementsList) {
		    		for(Element element : elements) {
		    			String text = element.text();
		    			if(text != null && text.contains(cost)) {
		    				showString += text + "\n";
		    			}
		    		}
				}
				showString += "\n";
				System.out.println(showString);
			}
		});
	}
	
	private int countTime(String time1, String time2) {
		
		SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
		if(time1 != null && time2 != null) {
    		try {
    			Date date1 = sdf.parse(time1);
    			Date date2 = sdf.parse(time2);
    			long diffTime = date1.getTime() - date2.getTime();
    			return Math.abs((int) diffTime);
    		} catch (ParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
		}
		
		return 0;
	}
}
