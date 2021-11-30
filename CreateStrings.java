package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateStrings {
	
	private String filePath = "TXT/CreateString.txt";
	private String fomatString = "%s(\"%s\", \"%s\", \"%s\"),";
	private List<String> fileContent = new ArrayList<>();
	
	public static void main(String[] args) {
		CreateStrings css = new CreateStrings();
		css.createString();
	}
	
	public CreateStrings() {
		readFile();
	}
	
	private void readFile() {
		File file = new File(filePath);
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                fileContent.add(line);
                while(line != null) {
                    line = br.readLine();
                    fileContent.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public void createString() {
		for(int i = 0, len = fileContent.size(); i < len; i++) {
			String content = fileContent.get(i);
			if(content != null && !"".equals(content)) {
				String[] keyValue = content.split(" ");
				System.out.println(String.format(fomatString, keyValue[0], keyValue[0], keyValue[0], keyValue[1]));
			}
		}
	}
}
