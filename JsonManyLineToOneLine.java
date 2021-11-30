package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonManyLineToOneLine {
	
	private final String PATH = "TXT/JsonManyLineToOneLine.txt";
	private List<List<String>> contents = new ArrayList<>();
	
	public static void main(String[] args) {
		JsonManyLineToOneLine jmltol = new JsonManyLineToOneLine();
		for(List<String> content : jmltol.getContents()) {
			jmltol.manyToOne(content);
		}
	}
	
	public JsonManyLineToOneLine() {
		readFile();
	}
	
	private void readFile() {
		File file = new File(PATH);
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
            	List<String> content = new ArrayList<>();
                String line = br.readLine();
                content.add(line);
                while(line != null) {
                    line = br.readLine();
                    if("C".equals(line)) {
                    	content.add(line);
                    	contents.add(content);
                    	content = new ArrayList<>();
                    } else {
                    	content.add(line);
                    }
                }
                contents.add(content);
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	private void manyToOne(List<String> content) {
		String finalString = "";
		for(String s : content) {
			if(s != null && !s.equals("null")) {
				finalString += s;
			}
		}
		System.out.println(finalString);
	}
	
	public List<List<String>> getContents(){
		return contents;
	}
}
