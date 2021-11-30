package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateXML {
    
    List<String> headers = new ArrayList<>();
    List<String> headerKeyList = new ArrayList<>();
    List<String> headerValueList = new ArrayList<>();
    
    List<String> contents = new ArrayList<>();
    List<String> contentKeyList = new ArrayList<>();
    List<String> contentValueList = new ArrayList<>();
    
    String msgId = "QueryForeignTrans";
    String sourceChannel = "FNM-NT-FDR-01";
    String txnSeq = "123456789012";
    String content = "";
    
    public static void main(String[] args) {
        CreateXML cXML = new CreateXML();
        cXML.createXML();
    }
    
    public void createXML() {
        readTXT();
        handleHeaderAndContents();
    }
    
    private void readTXT() {
        File file = new File("TXT/header.txt");
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
        
        file = new File("TXT/condition.txt");
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
    
    private void handleHeaderAndContents() {
        for(int i = 0, len = contents.size(); i < len; i++) {
            String nowString = contents.get(i);
            if(nowString == null) continue;
            String[] keyAndValue = nowString.split("=");
            if(keyAndValue.length == 2) {
                contentKeyList.add(keyAndValue[0]);
                contentValueList.add(keyAndValue[1]);
            } else if(keyAndValue.length == 1) {
                contentKeyList.add(keyAndValue[0]);
                contentValueList.add("");
            }
        }
    }
    
    public void setContent() {
        content = "<CUBXML>\n"
                + "\t<MWHEADER>\n\t\t<MSGID>" + msgId + "</MSGID>\n\t"
                + "\t<SOURCECHANNEL>" + sourceChannel + "</SOURCECHANNEL>\n\t"
                + "\t<RETURNCODE></RETURNCODE>\n\t\t<RETURNDESC></RETURNDESC>\n\t\t<RETURNCODECHANNEL>NBBW</RETURNCODECHANNEL>\n\t"
                + "\t<TXNSEQ>" + txnSeq + "</TXNSEQ>\n\t"
                + "\t<O360SEQ></O360SEQ>\n\t"
                + "</MWHEADER>\n\t"
                + "<TRANRQ>\n\t";
        for(int i = 0, len = contentKeyList.size(); i < len; i++) {
            String key = contentKeyList.get(i);
            String value = contentValueList.get(i);
            if("".equals(value)) {
                content += "\t<" + key + "/>\n\t";
            } else {
                content += "\t<" + key + ">" + value + "</" + key + ">\n\t";
            }
        }
        content += "</TRANRQ>\n</CUBXML>";
    }
    
//    private void createFile() {
//        File file = new File("finalXML.txt");
//        if(!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        
//        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
//            BufferedWriter bw = new BufferedWriter(writer);
//            content = "<CUBXML>\n"
//                    + "\t<MWHEADER>\n\t\t<MSGID>" + msgId + "</MSGID>\n\t"
//                    + "\t<SOURCECHANNEL>" + sourceChannel + "</SOURCECHANNEL>\n\t"
//                    + "\t<RETURNCODE></RETURNCODE>\n\t\t<RETURNDESC></RETURNDESC>\n\t\t<RETURNCODECHANNEL>NBBW</RETURNCODECHANNEL>\n\t"
//                    + "\t<TXNSEQ>" + txnSeq + "</TXNSEQ>\n\t"
//                    + "\t<O360SEQ></O360SEQ>\n\t"
//                    + "</MWHEADER>\n\t"
//                    + "<TRANRQ>\n\t";
//            bw.append(content);
//            for(int i = 0, len = keyList.size(); i < len; i++) {
//                String key = keyList.get(i);
//                String value = valueList.get(i);
//                if("".equals(value)) {
//                    content += "\t<" + key + "/>\n\t";
//                    bw.append("\t<" + key + "/>\n\t");
//                } else {
//                    content += "\t<" + key + ">" + value + "</" + key + ">\n\t";
//                    bw.append("\t<" + key + ">" + value + "</" + key + ">\n\t");
//                }
//            }
//            content += "</TRANRQ>\n</CUBXML>";
//            bw.append("</TRANRQ>\n</CUBXML>");
//            bw.flush();
//            bw.close();
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    public String getContent() {
        return content;
    }
}
