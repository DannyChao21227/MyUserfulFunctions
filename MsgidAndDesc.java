package UsefulFunction;

import java.io.File;

public class MsgidAndDesc {
	
	private final String PATH = "D:\\Comgmb\\中台Spec";
	
	public static void main(String[] args) {
		MsgidAndDesc mad = new MsgidAndDesc();
		mad.run();
	}
	
	private void run() {
		File file = new File(PATH);
		if(file.exists()) {
			String[] fileList = file.list();
			String[] splitFileName;
			for(String fileName : fileList) {
				if(fileName.contains("-")) {
					splitFileName = fileName.split("_");
					splitFileName = splitFileName[1].split("-");
					System.out.println(splitFileName[0] + " " + splitFileName[1]);
				} else {
    				splitFileName = fileName.split("_");
    				System.out.println(splitFileName[1] + " " + splitFileName[2]);
				}
			}
		}
	}
}
