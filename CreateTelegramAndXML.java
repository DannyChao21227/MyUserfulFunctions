package UsefulFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CreateTelegramAndXML {
    
    private List<String> headers = new ArrayList<String>();
    private List<String> headerKeyList = new ArrayList<String>();
    private List<String> headerValueList = new ArrayList<String>();
    
    private List<String> conditions = new ArrayList<String>();
    private List<String> conditionKeyList = new ArrayList<String>();
    private List<String> conditionValueList = new ArrayList<String>();
    private List<List<String>> conditionsList = new ArrayList<>();
    private List<List<String>> conditionValueListList = new ArrayList<>();
    
    private String telegramString = "";
    private String xmlContent = "";
    private String socketString = "";
    private String csvContent = "";
    
    private boolean hasDatas = true;
    private boolean isCsv = false;
    private int count = 1;

    public static void main(String[] args) {
        CreateTelegramAndXML ctaXML = new CreateTelegramAndXML();
        ctaXML.getHeaderAndCondition();
        ctaXML.createFile(ctaXML.getTelegramString() + "\n\n" + ctaXML.getXmlContent());
    }
    
    public void getHeaderAndCondition() {
        File file = new File("TXT/header.txt");
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                headers.add(line);
                while(line != null) {
                    line = br.readLine();
                    headers.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        file = new File("TXT/condition.txt");
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                line = line.trim()
//                		.replaceAll("^[\s]{0,}\"", "\"")
//                		.replaceAll("\t", "")
                		.replace("\": \"", "=")
                		.replace("\":\"", "=")
                		.replace("\",", "")
                		.replaceAll("\"", "")
                		.replaceAll("</[A-Za-z0-9]{1,}>", "")
                		.replace("<", "").replace(">", "=");
                conditions.add(line);
                while(line != null) {
                    line = br.readLine();
                    line = line != null ? line.trim()
//                    		.replaceAll("[\\s]{0,}\"", "\"")
//                    		.replaceAll("\t", "")
                    		.replace("\": \"", "=")
                    		.replace("\":\"", "=")
                    		.replace("\",", "")
                    		.replaceAll("\"", "")
                    		.replaceAll("</[A-Za-z0-9]{1,}>", "")
                    		.replace("<", "")
                    		.replace(">", "=") : null;
                    
                    if((line == null || "".equals(line)) && isCsv) {
                    	conditionsList.add(conditions);
                    	conditions = new ArrayList<>();
                    } else {
                    	conditions.add(line);
                    }
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        if(!isCsv) {
        	handleList(headers, headerKeyList, headerValueList, true);
            handleList(conditions, conditionKeyList, conditionValueList, false);
            handleTelegramOutputString("MID-LX-EAI-01");
            handleTelegramOutputString();
            handleXMLOutputString();
        } else {
        	handleList(headers, headerKeyList, headerValueList, true);
        	for(List<String> conditionList : conditionsList) {
            	handleList(conditionList, conditionKeyList, conditionValueList, false);
            	conditionValueList = new ArrayList<>();
            }
        	handleCSVOutputString();
        }
//        handleSocketOutputString();
    }
    
    private void handleList(List<String> fileContents, List<String> key, List<String> value, boolean isHeader) {
        for(int i = 0, len = fileContents.size(); i < len; i++) {
            String nowString = fileContents.get(i);
            if(nowString == null) continue;
            String[] keyAndValue;
            if(nowString.contains("<")) {
//            	nowString = nowString.substring(0, nowString.indexOf("<"));
            	keyAndValue = nowString.split("=");
            } else {
//            	nowString = nowString.replaceAll("\",", "").replaceAll("\"", "");
            	keyAndValue = nowString.split("=");
            }
            
            if(keyAndValue.length == 2) {
            	if(count == 1) {
                    key.add(keyAndValue[0]);
            	}
            	value.add(keyAndValue[1]);
            } else if(keyAndValue.length == 1) {
                key.add(keyAndValue[0]);
                value.add("");
            }
        }
        if(!isHeader) {
            conditionValueListList.add(value);
        	count += 1;
        }
    }
    
    private void handleTelegramOutputString(String eai) {
    	telegramString += "{\n    \"MWHEADER\": {\n";
        for(int i = 0, len = headerKeyList.size(); i < len; i++) {
            if(i != len - 1) {
            	if(i == 1) {
            		telegramString += "    \"" + headerKeyList.get(i) + "\": \"MID-LX-EAI-01\",\n";
            	} else {
            		telegramString += "    \"" + headerKeyList.get(i) + "\": \"" + headerValueList.get(i) + "\",\n";
            	}
            }else {
                telegramString += "    \"" + headerKeyList.get(i) + "\": \"" + headerValueList.get(i) + "\"\n";
            }
        }
        if(hasDatas)
        	telegramString += "    },\n    \"TRANRQ\": {\n        \"Datas\": [{\n";
        else
        	telegramString += "    },\n    \"TRANRQ\": {\n";
        for(int i = 0, len = conditionKeyList.size(); i < len; i++) {
            if(i != len - 1) {
                telegramString += "        \"" + conditionKeyList.get(i) + "\": \"" + conditionValueList.get(i) + "\",\n";
            } else {
                telegramString += "        \"" + conditionKeyList.get(i) + "\": \"" + conditionValueList.get(i) + "\"\n";
            }
        }
        if(hasDatas)
        	telegramString += "        }]\n";
        telegramString += "    }\n}\n";
    }
    
    private void handleTelegramOutputString() {
        telegramString += "{\n    \"MWHEADER\": {\n";
        for(int i = 0, len = headerKeyList.size(); i < len; i++) {
            if(i != len - 1) {
                telegramString += "    \"" + headerKeyList.get(i) + "\": \"" + headerValueList.get(i) + "\",\n";
            } else {
                telegramString += "    \"" + headerKeyList.get(i) + "\": \"" + headerValueList.get(i) + "\"\n";
            }
        }
        if(hasDatas)
        	telegramString += "    },\n    \"TRANRQ\": {\n        \"Datas\": [{\n";
        else
        	telegramString += "    },\n    \"TRANRQ\": {\n";
        for(int i = 0, len = conditionKeyList.size(); i < len; i++) {
            if(i != len - 1) {
                telegramString += "        \"" + conditionKeyList.get(i) + "\": \"" + conditionValueList.get(i) + "\",\n";
            } else {
                telegramString += "        \"" + conditionKeyList.get(i) + "\": \"" + conditionValueList.get(i) + "\"\n";
            }
        }
        if(hasDatas)
        	telegramString += "        }]\n";
        telegramString += "    }\n}";
        System.out.println(telegramString);
    }
    
    private void handleXMLOutputString() {
    	xmlContent = "<![CDATA[<?xml version=\"1.0\" encoding=\"BIG5\"?>\n";
        xmlContent += "<CUBXML xmlns=\"http://www.cathaybk.com.tw/webservice/" + headerValueList.get(0) + "/\">\n\t<MWHEADER>\n\t";
        for(int i = 0, len = headerKeyList.size(); i < len; i++) {
            String key = headerKeyList.get(i);
            String value = headerValueList.get(i);
            if("".equals(value)) {
                xmlContent += "\t<" + key + "/>\n\t";
            } else {
                xmlContent += "\t<" + key + ">" + value + "</" + key + ">\n\t";
            }
        }
        xmlContent += "</MWHEADER>\n\t";
        xmlContent += "<TRANRQ>\n\t";
        if(hasDatas) {
        	xmlContent += "\t<Datas>\n\t\t\t<Data>\n\t\t\t";
            for(int i = 0, len = conditionKeyList.size(); i < len; i++) {
                String key = conditionKeyList.get(i);
                String value = conditionValueList.get(i);
                if("".equals(value)) {
                    xmlContent += "\t<" + key + ">" + "</" + key + ">\n\t\t\t";
                } else {
                    xmlContent += "\t<" + key + ">" + value + "</" + key + ">\n\t\t\t";
                }
            }
            xmlContent += "</Data>\n\t\t</Datas>\n\t";
        } else {
        	for(int i = 0, len = conditionKeyList.size(); i < len; i++) {
                String key = conditionKeyList.get(i);
                String value = conditionValueList.get(i);
                if("".equals(value)) {
                    xmlContent += "\t<" + key + ">" + "</" + key + ">\n\t";
                } else {
                    xmlContent += "\t<" + key + ">" + value + "</" + key + ">\n\t";
                }
            }
        }
        xmlContent += "</TRANRQ>\n</CUBXML>]]>";
        System.out.println(xmlContent);
    }
    
    public void handleSocketOutputString() {
    	socketString = "    XXXX";
    	socketString += headerValueList.get(0);
    	int msgidLen = headerValueList.get(0).length();
    	for(int i = 0, len = 20 - msgidLen; i < len; i++) {
    		socketString += " ";
    	}
    	socketString += headerValueList.get(1);
    	for(int i = 0; i < 136; i++) {
    		socketString += " ";
    	}
    	socketString += headerValueList.get(6);
    	for(int i = 0; i < 20; i++) {
    		socketString += " ";
    	}
    	String socketBody = "";
    	for(int i = 0, len = conditionValueList.size(); i < len; i++) {
    		socketBody += conditionValueList.get(i);
    	}
        System.out.println(socketString + socketBody);
    }
    
    public void handleCSVOutputString() {
    	for(int i = 0, len = conditionKeyList.size(); i < len; i++) {
    		if(i == len - 1) {
    			csvContent += conditionKeyList.get(i) + "\n";
    		} else {
    			csvContent += conditionKeyList.get(i) + ",";
    		}
    	}
    	for(List<String> valueList : conditionValueListList) {
        	for(int i = 0, len = valueList.size(); i < len; i++) {
        		String value = valueList.get(i);
        		if(i == len - 1) {
        			csvContent += value + "\n";
        		} else {
        			csvContent += value + ",";
        		}
        	}
    	}
    	System.out.println(csvContent);
    }
    
    public void createFile(String content) {
    	File file = new File("finalFile");
    	file.mkdir();
    	if(!isCsv) {
            file = new File("finalFile/" + headerValueList.get(0) + "-finalFile.txt");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Create File Failed!!!");
                }
            }
            
            try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
                BufferedWriter bw = new BufferedWriter(writer);
                bw.write(content);
                bw.flush();
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
    	} else {
            file = new File("csv/" + headerValueList.get(0) + ".csv");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    System.out.println("Create File Failed!!!");
                }
            }
            
            try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
                BufferedWriter bw = new BufferedWriter(writer);
                bw.write(csvContent);
                bw.flush();
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
    	}
    }

    public String getTelegramString() {
        return telegramString;
    }

    public String getXmlContent() {
        return xmlContent;
    }
    
}
