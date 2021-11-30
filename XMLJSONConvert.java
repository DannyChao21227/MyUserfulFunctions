package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import org.json.XML;

public class XMLJSONConvert {
	
	private List<String> xmlContents = new ArrayList<String>();
	private List<String> jsonContents = new ArrayList<>();
	
	public static void main(String[] args) {
		XMLJSONConvert convert = new XMLJSONConvert();
		convert.readFile();
		System.out.println("XML to JSON = \n" + convert.xmlToJson());
//		System.out.println("JSON to XML = \n" + convert.jsonToXml());
	}
	
	public void readFile() {
		File file = new File("TXT/xml.txt");
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                xmlContents.add(line);
                while(line != null) {
                    line = br.readLine();
                    xmlContents.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        file = new File("TXT/json.txt");
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                jsonContents.add(line);
                while(line != null) {
                    line = br.readLine();
                    jsonContents.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public String xmlToJson() {
		String xmlContent = "";
		for(String s : xmlContents) {
			xmlContent += s + "\n";
		}
		
		JSONObject json = XML.toJSONObject(xmlContent);
		String jsonString = json.toString(4);
//		System.out.println(jsonString);
		return jsonString;
	}
	
	public String jsonToXml() {
		String jsonContent = "";
		for(String s : jsonContents) {
			jsonContent += s + "\n";
		}
		JSONObject json = new JSONObject(jsonContent);
		String xml = XML.toString(json);
//		System.out.println(xml);
		return xml;
	}
}
