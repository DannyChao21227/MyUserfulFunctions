package UsefulFunction;

import java.io.File;

public class PrintDirectorFolders {
	
//	private String country = "vn";
	private final String DIRECTORYPATH = "D:\\Comgmb\\TR電文檔案\\ALL jar";// "D:\\Users\\NT88603\\eclipse202006-workspace\\eai-svc-comgmb\\src\\main\\java\\com\\cub\\eai\\service\\" + country;
	private final String REPLACESTRING = ".jar";// country.toUpperCase() + "Svc.java";
	
	public static void main(String[] args) {
		PrintDirectorFolders pdf = new PrintDirectorFolders();
		pdf.print();
	}
	
	private void print() {
		File file = new File(DIRECTORYPATH);
		if (file.isDirectory()) {
			String[] folderList = file.list();
			for(String fileName : folderList) {
				fileName = fileName.replace(REPLACESTRING, "");
				System.out.println(fileName.toUpperCase());
			}
		}
	}
}
