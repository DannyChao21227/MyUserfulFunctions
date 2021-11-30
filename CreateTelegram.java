package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateTelegram {
    
    final String RAST = "Rast";
    final String SOAP = "Soap";
    private String finalTelegram = "";
    List<String> headerKeyList = new ArrayList<>();
    List<String> headerValueList = new ArrayList<>();
    List<String> requestKeyList = new ArrayList<>();
    List<String> requestValueList = new ArrayList<>();

    public static void main(String[] args) {
        CreateTelegram ct = new CreateTelegram();
        ct.createTelegram(ct.RAST);
    }
    
    public CreateTelegram() {
        readTXT();
    }
    
    private void readTXT() {
        File file = new File("TXT/header.txt");
        InputStreamReader reader;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String readString = "";
            readString = br.readLine();
            if(readString != null) {
                String[] splitStrings = readString.split("=");
                headerKeyList.add(splitStrings[0]);
                headerValueList.add(splitStrings[1]);
                while(readString != null) {
                    readString = br.readLine();
                    if(readString != null) {
                        splitStrings = readString.split("=");
                        headerKeyList.add(splitStrings[0]);
                        headerValueList.add(splitStrings[1]);
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        file = new File("TXT/condition.txt");
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            BufferedReader br = new BufferedReader(reader);
            String readString = "";
            readString = br.readLine();
            if(readString != null) {
                String[] splitStrings = readString.split("=");
                requestKeyList.add(splitStrings[0]);
                requestValueList.add(splitStrings[1]);
                while(readString != null) {
                    readString = br.readLine();
                    if(readString != null) {
                        splitStrings = readString.split("=");
                        requestKeyList.add(splitStrings[0]);
                        requestValueList.add(splitStrings[1]);
                    }
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void createTelegram(String teleType) {
        switch (teleType) {
            case RAST:
                createRAST();
                break;
            case SOAP:
                createSOAP();
                break;
        }
    }
    
    private void createRAST() {
        finalTelegram += "{\n    \"MWHEADER\": {\n";
        for(int i = 0, len = headerKeyList.size(); i < len; i++) {
            if(i != len - 1) {
                finalTelegram += "    \"" + headerKeyList.get(i) + "\": \"" + headerValueList.get(i) + "\",\n";
            } else {
                finalTelegram += "    \"" + headerKeyList.get(i) + "\": \"" + headerValueList.get(i) + "\"\n";
            }
        }
        finalTelegram += "    },\n    \"TRANRQ\": {\n";
        for(int i = 0, len = requestKeyList.size(); i < len; i++) {
            if(i != len - 1) {
                finalTelegram += "    \"" + requestKeyList.get(i) + "\": \"" + requestValueList.get(i) + "\",\n";
            } else {
                finalTelegram += "    \"" + requestKeyList.get(i) + "\": \"" + requestValueList.get(i) + "\"\n";
            }
        }
        finalTelegram += "    }\n}";
        System.out.println(finalTelegram);
    }
    
    private void createSOAP() {
        System.out.println(finalTelegram);
    }
    
    public String getFinalTelegram() {
        return finalTelegram;
    }
}
