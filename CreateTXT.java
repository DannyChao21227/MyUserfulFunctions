package UsefulFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CreateTXT {
    
    private File file;
    private List<String> contents = new ArrayList<>();
    
    public CreateTXT(File file, List<String> contents) {
        this.file = file;
        this.contents = contents;
    }
    
    public void createTXT() {
        System.out.println("檔案存放於：" + file.getAbsolutePath());
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
            BufferedWriter bw = new BufferedWriter(writer);
            for(String content : contents) {
                bw.append(content + "\n");
            }
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
    }
}
