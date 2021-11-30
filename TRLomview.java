package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TRLomview {
	
	private File file = new File("TXT/TR_lomview_log.txt");
	private List<String> lomviewContent = new ArrayList<>();
	
	public static void main(String[] args) {
		TRLomview trl = new TRLomview();
		trl.readLomviewFile();
	}
	
	private void readLomviewFile() {
		try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                lomviewContent.add(line);
                while(line != null) {
                    line = br.readLine();
                    lomviewContent.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
		analysisTRLomview();
	}
	
	@SuppressWarnings("unused")
	private void analysisTRLomview() {
		String socketLength = "";
		String c1 = "", c2 = "", c3 = "", c4 = "";
		String socket = "";
		for(String content : lomviewContent) {
			if(content.matches("^\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}(\\d{1,})$")) {
				socketLength = startWithTime(content);
			} else if(content.startsWith("   C")) {
				if(content.contains("C1")) {
					String[] cStrings = content.replaceAll("\\s{1,}", " ").split(" ");
					c1 = startWithC(cStrings[1]);
					c2 = startWithC(cStrings[2]);
				} else {
					String[] cStrings = content.replaceAll("\\s{1,}", " ").split(" ");
					c3 = startWithC(cStrings[1]);
					c4 = startWithC(cStrings[2]);
				}
			} else {
				
			}
		}
	}
	
	private String startWithTime(String inData) {
		String finalString = "";
		return finalString;
	}
	
	private String startWithC(String inData) {
		String finalString = inData.split("=")[1];
		return finalString;
	}
	
	@SuppressWarnings("unused")
	private String startWithLineCount(String inData) {
		String finalString = "";
		return finalString;
	}
}
