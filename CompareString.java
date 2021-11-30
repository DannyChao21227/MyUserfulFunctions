package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompareString {
	
	private String filePath = "TXT\\CompareString.txt";
	private List<String> contents = new ArrayList<>();
	private Set<String> field1 = new HashSet<>();
	private Set<String> field2 = new HashSet<>();
	private Map<String, String> field1Map = new HashMap<>();
	private Map<String, String> field2Map = new HashMap<>();
	
	public static void main(String[] args) {
		CompareString cs = new CompareString();
		
	}
	
	public CompareString() {
		readFile();
	}
	
	private void readFile() {
		File file = new File(filePath);
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                contents.add(line);
                while(line != null) {
                    line = br.readLine();
                    contents.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public void analysisString(String content) {
		
	}
}
