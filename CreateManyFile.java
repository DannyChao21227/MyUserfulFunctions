package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CreateManyFile {
	
	List<String> fileNames = new ArrayList<>();
	final String CREATEPATH = "D:\\海外IFX\\越南\\雲峰\\";
	final String DESC = "比對結果";
	final String FILETYPE = ".xlsx";
	
	public static void main(String[] args) {
		CreateManyFile cmf = new CreateManyFile();
		cmf.getManyFiles();
		cmf.createFiles();
	}
	
	public void getManyFiles() {
		File file = new File("TXT/ManyFiles.txt");
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                fileNames.add(line);
                while(line != null) {
                    line = br.readLine();
                    fileNames.add(line);
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public void createFiles() {
		for(String fileName : fileNames) {
			if(fileName != null && fileName.length() > 0) {
    			File file = new File(CREATEPATH + fileName + "-" + DESC + FILETYPE);
    			if(!file.exists()) {
    				try {
    					if(file.createNewFile()) {
    						System.out.println("Create " + CREATEPATH + fileName + "-" + DESC + FILETYPE + " file Success!!");
    					}
    				} catch (IOException e) {
    					System.out.println("Create " + CREATEPATH + fileName + "-" + DESC + FILETYPE + " file Fail!!");
    				}
    			}
			}
		}
	}
}
