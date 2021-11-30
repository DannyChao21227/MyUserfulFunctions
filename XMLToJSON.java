package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.XML;
import Entity.XMLTag;

public class XMLToJSON {
    
    private String path = "TXT/XMLToJSON.txt";
    @SuppressWarnings("unused")
	private String content = "";
    private List<String> contents = new ArrayList<>();
    @SuppressWarnings("unused")
	private XMLTag xmlTag = new XMLTag();
    private Set<String> tagName = new HashSet<>();
    
    public static void main(String[] args) {
        XMLToJSON xml = new XMLToJSON();
//        xml.readFile();
        System.out.println(xml.xmlToJson());
    }
    
    public String xmlToJson() {
        try {
            FileInputStream input = new FileInputStream(new File(path));
            String xmlStr = IOUtils.toString(input, "UTF-8");
            JSONObject json = XML.toJSONObject(xmlStr);
            return json.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "";
    }
    
    public void readFile() {
        try {
            InputStream in = new FileInputStream(new File(path));
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            content = line + "\n";
            analysisTag(line != null ? line : "");
            contents.add(line);
            while(line != null) {
                if(line.equals("")) continue;
                line = br.readLine();
                analysisTag(line != null ? line : "");
                content += line + "\n";
                contents.add(line);
            }
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        analysisContent();
    }
    
    @SuppressWarnings("unused")
	private void analysisTag(String line) {
        if("".equals(line)) return;
        XMLTag tag = new XMLTag();
        String tagNameString = "";
        boolean setAddSucess = false;
        if(line.contains("</")) {
            tagNameString = line.substring(line.lastIndexOf("</") + 2, line.length() - 1);
            System.out.println(tagNameString);
            setAddSucess = tagName.add(tagNameString);
        } else {
            if(line.contains("/>")) {
                tagNameString = line.substring(line.indexOf("<") + 1, line.lastIndexOf("/"));
            } else {
                tagNameString = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
            }
            if(tagNameString.contains(" ")) {
                tagNameString = tagNameString.substring(0, tagNameString.indexOf(" "));
            }
            System.out.println(tagNameString);
            setAddSucess = tagName.add(tagNameString);
        }
        if(setAddSucess) tag.setTagName(tagNameString);
        String tagContent = "";
        
        
    }
    
    private void analysisContent() {
        
    }
}
