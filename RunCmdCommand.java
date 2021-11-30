package UsefulFunction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RunCmdCommand {
	
	private final String DIRECTORYPATH = "D:\\Comgmb\\TR電文檔案\\ALL jar";// "D:\\Users\\NT88603\\eclipse202006-workspace\\eai-svc-comgmb\\src\\main\\java\\com\\cub\\eai\\service\\" + country;
	@SuppressWarnings("unused")
	private final String REPLACESTRING = ".jar";// country.toUpperCase() + "Svc.java";
	private List<String> msgidList = new ArrayList<>();
	private String selectCountry = "PH";
	
	public static void main(String[] args) {
		RunCmdCommand rcc = new RunCmdCommand();
//		rcc.testRun();
//		rcc.run();
		rcc.specifyMsgid();
	}
	
	@SuppressWarnings("unused")
	private void testRun() {
		Runtime rt = Runtime.getRuntime();
		try {
			Process proc = rt.exec("cmd /c dir", null, new File(DIRECTORYPATH));
			BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charset.forName("950")));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
			System.out.println("Here is the standard output of the command:\n");
			String s = null;
			while ((s = input.readLine()) != null) {
			    System.out.println(new String(s.getBytes(), "UTF-8"));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		File file = new File(DIRECTORYPATH);
		if (file.isDirectory()) {
			String[] folderList = file.list();
			int fileCount = folderList.length;
			Runtime rt = Runtime.getRuntime();
			List<String> fileContent = new ArrayList<>();
			try {
				fileContent.add("Here is the standard output of the command:\n");
				int i = 1;
    			for(String fileName : folderList) {
    //				fileName = fileName.replace(REPLACESTRING, "");
    //				System.out.println(fileName.toUpperCase());
    				Process proc = rt.exec("cmd /c " + "jar -tvf " + fileName, null, new File(DIRECTORYPATH));
					BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charset.forName("950")));
					fileContent.add(fileName + "：\n");
					String s = null;
					System.out.println(i + " / " + fileCount);
					i += 1;
					while ((s = input.readLine()) != null) {
						fileContent.add("\t" + new String(s.getBytes(), "UTF-8"));
					}
    			}
    			File files = new File("JarContent.txt");
    			FileWriter fw = new FileWriter(files);
    			i = 1;
    			for(String s : fileContent) {
    				fw.write(s + "\n");
    				i += 1;
    				if(i % 20 == 0) {
    					fw.flush();
    				}
    			}
    			fw.flush();
    			fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void specifyMsgid() {
		File file = new File("TXT/SpecifyMsgid.txt");
        try(InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
            BufferedReader br = new BufferedReader(reader);
            if(br != null) {
                String line = br.readLine();
                msgidList.add(line);
                while(line != null) {
                    line = br.readLine();
                    if(line != null || !"".equals(line)) {
                    	msgidList.add(line);
                    }
                }
                br.close();
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        
		int fileCount = msgidList.size();
		Runtime rt = Runtime.getRuntime();
		List<String> fileContent = new ArrayList<>();
		try {
			fileContent.add("Here is the standard output of the command:\n");
			int i = 1;
			for(String fileName : msgidList) {
//				fileName = fileName.replace(REPLACESTRING, "");
//				System.out.println(fileName.toUpperCase());
				Process proc = rt.exec("cmd /c " + "jar -tvf " + fileName + ".jar", null, new File(DIRECTORYPATH));
				BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream(), Charset.forName("950")));
//				fileContent.add(fileName + "：\n");
//				System.out.println(i + " / " + fileCount);
//				System.out.println(fileName + "：\n");
				String s = null;
				i += 1;
				while ((s = input.readLine()) != null) {
					String finalString = new String(s.getBytes(), "UTF-8");
//					System.out.println(finalString);
					if(finalString.contains(selectCountry) && !finalString.endsWith(".class")) {
//						fileContent.add("\t" + new String(s.getBytes(), "UTF-8"));
						finalString = finalString.substring(finalString.indexOf(selectCountry)).replace("Process.java", "");
						if(finalString.contains("/")) finalString = finalString.split("/")[1];
						if(!finalString.startsWith(selectCountry)) continue;
						if(finalString.contains("TxnMain")) continue;
						finalString = "Select O360SYSDATETIME, count(*) as num from (select * from txnlogh where MWMSGID = '" + finalString + "') group by o360SYSDATETIME order by num desc";
						System.out.println(finalString);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
