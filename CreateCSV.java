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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CreateCSV {
	
	private final String DATASPATH = "TXT/CSVDatas.txt";
	private String csvFileName = "MIDCIF0001.csv";
	
	private List<String> initialDatas = new ArrayList<>();
	
	private Set<String> headerSet = new HashSet<>();
	private List<String> headerList = new ArrayList<>();
	private List<Map<String, String>> datas = new ArrayList<>();
	
	public static void main(String[] args) {
		CreateCSV ccsv = new CreateCSV();
		ccsv.createCSV();
	}
	
	public CreateCSV() {
		readFile();
	}
	
	private void readFile() {
		File file = new File(DATASPATH);
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
            	String data = "";
                String line = br.readLine();
                line = line.replaceAll("\t", "").trim();
                line = line.replace("\",", "\"");
                data += line + "\n";
                while(line != null) {
                    line = br.readLine();
                    if(line != null && !"".equals(line)) {
                        line = line.trim();
                        line = line.replace("\",", "\"");
                        if("C".equals(line)) {
                        	initialDatas.add(data);
                        	data = "";
                        } else {
                        	data += line + "\n";
                        }
                    }
                }
            	initialDatas.add(data);
            	data = "";
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	private void analysisInitialData() {
		for(String data : initialDatas) {
			String[] columes = data.split("\n");
			Map<String, String> dataMap = new HashMap<>();
			for(String colume : columes) {
				String[] nameAndValue = colume.split(": ");
				if(nameAndValue.length == 2) {
					nameAndValue[0] = nameAndValue[0].replaceAll("\"", "");
					nameAndValue[1] = nameAndValue[1].replaceAll("\"", "");
					if(headerSet.add(nameAndValue[0])) {
						headerList.add(nameAndValue[0]);
					}
					dataMap.put(nameAndValue[0], nameAndValue[1]);
				}
			}
			datas.add(dataMap);
		}
	}
	
	public void createCSV() {
		analysisInitialData();
		String headerString = "";
		int headerListSize = headerList.size();
		for(int i = 0; i < headerListSize; i++) {
			if(i == headerListSize - 1) {
				headerString += headerList.get(i);
			} else {
				headerString += headerList.get(i) + ",";
			}
		}
		System.out.println(headerString);
		
		String valueString = "";
		int datasSize = datas.size();
		for(int i = 0; i < datasSize; i++) {
			Map<String, String> dataMap = datas.get(i);
			for(int j = 0; j < headerListSize; j++) {
				String headerString2 = headerList.get(j);
				if(j == headerListSize - 1) {
					valueString += (dataMap.get(headerString2) != null ? dataMap.get(headerString2) : "") + "\n";
				} else {
					valueString += dataMap.get(headerString2) + ",";
				}
			}
		}
		System.out.println(valueString);
		File file = new File("csv");
		file.mkdirs();
		file = new File("csv/" + csvFileName);
		try {
			file.createNewFile();
			if(file.exists()) {
    			OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
                BufferedWriter bw = new BufferedWriter(writer);
                bw.append(headerString + "\n");
                bw.append(valueString + "\n");
                bw.flush();
                bw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
