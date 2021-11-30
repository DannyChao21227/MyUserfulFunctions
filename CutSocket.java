package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CutSocket {
	
	private final String SOCKETFIELDLENGTHPATH = "TXT/SocketFieldLength.txt";
	private final String SOCKETTXTPATH = "TXT/Socket.txt";
	
	private List<Integer> socketFieldLengthList = new ArrayList<>();
	private List<String> socketFields = new ArrayList<>();
	private String socketContent = "";
	@SuppressWarnings("unused")
	private int totalLength = 0;
	private int maxFieldNameLength = 0;
	
	public static void main(String[] args) {
		CutSocket cs = new CutSocket();
		cs.readFile();
	}
	
	private void readFile() {
		File file = new File(SOCKETFIELDLENGTHPATH);
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                line = br.readLine();
                line = br.readLine();
                line = br.readLine();
                if(line != null && (!line.startsWith("#") || !line.startsWith("M"))) {
                	line = line.replaceAll("[ ]{2,}", " ");
                	String[] socketFieldInfo = line.substring(0, line.indexOf("#")).split(" ");
                	System.out.println(line);
                	if(socketFieldInfo.length >= 3) {
                		maxFieldNameLength = (socketFieldInfo[1] + socketFieldInfo[2]).length() + 1;
                    	socketFields.add(socketFieldInfo[1]);
                    	socketFieldLengthList.add(Integer.valueOf(socketFieldInfo[2]));
                        while(line != null) {
                            line = br.readLine();
                            System.out.println(line);
                            if(line != null && (!line.startsWith("#") || !line.startsWith("M"))) {
                                line = line.replaceAll("[ ]{2,}", " ");
                                socketFieldInfo = line.substring(0, line.indexOf("#")).split(" ");
                                if(socketFieldInfo.length >= 3) {
                                	maxFieldNameLength = (socketFieldInfo[1] + socketFieldInfo[2]).length() + 1 > maxFieldNameLength ? (socketFieldInfo[1] + socketFieldInfo[2]).length() + 1 : maxFieldNameLength;
                                	socketFields.add(socketFieldInfo[1]);
                                	socketFieldLengthList.add(Integer.valueOf(socketFieldInfo[2]));
                                }
                            }
                        }
                	}
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        file = new File(SOCKETTXTPATH);
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                socketContent = line;
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        for(int i : socketFieldLengthList) {
        	totalLength += i;
        }
        analysisField();
	}
	
	private void analysisField() {
		
		String fieldName = "";
		int fieldLength = 0;
		String fieldContent = "";
		String remainString = socketContent;
		int nowLength = 0;
		String whiteString = "";
		
		for(int i = 0; i < socketFields.size(); i++) {
			fieldName = socketFields.get(i);
			fieldLength = socketFieldLengthList.get(i);
			fieldContent = remainString.substring(0, fieldLength);
			remainString = remainString.substring(fieldLength);
			nowLength = (fieldName + fieldLength).length() + 2;
			whiteString = "";
			for(int j = 0; j < (maxFieldNameLength - nowLength) + 2; j++) {
				whiteString += " ";
			}
			System.out.println(fieldName + "(" + fieldLength + ")" + whiteString + "= [" + fieldContent + "]");
		}
		nowLength = ("UNKNOWN" + remainString.length()).length() + 2;
		whiteString = "";
		for(int j = 0; j < (maxFieldNameLength - nowLength) + 2; j++) {
			whiteString += " ";
		}
		System.out.println("UNKNOWN" + "(" + remainString.length() + ")" + whiteString + "= [" + remainString + "]");
	}
}
