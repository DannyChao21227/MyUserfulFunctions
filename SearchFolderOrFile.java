package UsefulFunction;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFolderOrFile {
    
    private String inputPathsString = "";
    private String inputNamesString = "";
    private String[] searchNames;
    private String[] searchPaths;
    private List<String> answerFolderPaths = new ArrayList<>();
    private List<String> answerFilePaths = new ArrayList<>();
    private int countFolder = 0;
    private int countFile = 0;
    private int totalFolder = 0;
    private int totalFile = 0;
    private String searchTime = "";
    private List<String> contents = new ArrayList<>();
    private String answerTXTPath = "TXT/SearchResult.txt";
    private int maxFileNameLength = 0;
    private int maxFolderNameLength = 0;
    private int maxLength = 0;
    private final String filterString;
    
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        String[] strings;
        if(args.length < 2)
            strings = new String[] {"D:\\Users\\NT88603", "folder", "TR"};
        else
            strings = args;
        SearchFolderOrFile sfof = new SearchFolderOrFile(strings);
    }
    
    public SearchFolderOrFile(String[] args) {
        if(args != null && args.length == 3) {
            inputPathsString = args[0];
            filterString = args[1];
            inputNamesString = args[2];
            searchPaths = inputPathsString.split(":");
            searchNames = inputNamesString.split(":");
            long startTime = System.currentTimeMillis();
            SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date(System.currentTimeMillis());
            answerTXTPath = "SearchResult/" + formatter.format(date) + answerTXTPath;
            startSearch();
            formattResultList();
            setCount();
            contents.add("Search Path : " + inputPathsString);
            String searchConditions = "";
            for(int i = 0, len = searchNames.length; i < len; i++) {
                String condition = searchNames[i];
                if(i == len - 1) {
                    searchConditions += condition;
                } else {
                    searchConditions += condition + ", ";
                }
            }
            contents.add("Search Conditions : " + searchConditions);
            contents.add("This path : " + inputPathsString + " has total folder count : " + totalFolder);
            contents.add("This path : " + inputPathsString + " has total file count : " + totalFile + "\n");
            contents.add("Total found : " + (countFolder + countFile));
            contents.add("Total found folders : " + countFolder);
            for(String getName : answerFolderPaths) {
                contents.add(getName);
            }
            contents.add("");
            contents.add("Total found files : " + countFile);
            for(String getName : answerFilePaths) {
                contents.add(getName);
            }
            contents.add("");
            setStringTime(System.currentTimeMillis() - startTime);
            contents.add("This search took about : " + searchTime);
            if((countFolder + countFile) <= 200) {
                for(String content : contents) {
                    System.out.println(content);
                }
                System.out.println("搜尋結果存放於以下檔案：");
            } else {
                System.out.println("因為搜尋結果太多，所以請去以下檔案察看結果：");
            }
            CreateTXT cTXT = new CreateTXT(new File(answerTXTPath), contents);
            cTXT.createTXT();
        } else {
            filterString = "";
        }
    }
    
    private void startSearch() {
        for(String path : searchPaths) {
            File file = new File(path);
            if(file.exists()) {
                getAllFilesAndFolders(file);
            } else {
                contents.add("Path : " + path + " not found!!!");
                System.out.println("Path : " + path + " not found!!!");
            }
        }
    }
    
    private void getAllFilesAndFolders(File file) {
        String[] filesInFolder = file.list();
        if(filesInFolder != null) {
            int numberOfFiles = filesInFolder.length;
            String nowPath = file.getAbsolutePath() + "\\";
            if(filterString.toLowerCase().equals("all")) {
                for(int i = 0; i < numberOfFiles; i++) {
                    File nowFile = new File(nowPath + filesInFolder[i]);
                    if(nowFile.isDirectory()) {
                        ifFolder(nowFile);
                    } else {
                        ifFile(nowFile);
                    }
                }
            } else if(filterString.toLowerCase().equals("file")) {
                for(int i = 0; i < numberOfFiles; i++) {
                    File nowFile = new File(nowPath + filesInFolder[i]);
                    if(nowFile.isDirectory()) {
                        ifFolder(nowFile);
                    }
                }
            } else if(filterString.toLowerCase().equals("folder")) {
                for(int i = 0; i < numberOfFiles; i++) {
                    File nowFile = new File(nowPath + filesInFolder[i]);
                    if(!nowFile.isDirectory()) {
                        ifFile(nowFile);
                    }
                }
            }
        } else {
            ifFile(file);
        }
    }
    
    private void ifFolder(File file) {
        totalFolder += 1;
        String path = file.getAbsolutePath();
        String fileName = file.getName();
        fileName = fileName.toUpperCase();
        for(String name : searchNames) {
            name = name.toUpperCase();
            if(fileName.contains(name)) {
                if(fileName.length() > maxFolderNameLength) {
                    maxFolderNameLength = fileName.length();
                }
                answerFolderPaths.add(path);
                break;
            }
        }
        getAllFilesAndFolders(file);
    }
    
    private void ifFile(File file) {
        totalFile += 1;
        String path = file.getAbsolutePath();
        String fileName = file.getName();
        fileName = fileName.toUpperCase();
        for(String name : searchNames) {
            name = name.toUpperCase();
            if(fileName.contains(name)) {
                if(fileName.length() > maxFileNameLength) {
                    maxFileNameLength = fileName.length();
                }
                answerFilePaths.add(path);
                break;
            }
        }
    }
    
    private void formattResultList() {
        if(maxFileNameLength > maxFolderNameLength) {
            maxLength = maxFileNameLength;
        } else if(maxFolderNameLength > maxFileNameLength) {
            maxLength = maxFolderNameLength;
        }
        maxLength += 10;
        for(int i = 0, len = answerFilePaths.size(); i < len; i++) {
            String filePath = answerFilePaths.get(i);
            int lastSlash = filePath.lastIndexOf("\\") + 1;
            String fileName = filePath.substring(lastSlash);
            String folderName = filePath.substring(0, lastSlash - 1);
            String finalFileName = fileName;
            int count = finalFileName.length();
            for(int j = 0, length = finalFileName.length(); j < length; j++) {
                String nowChar = finalFileName.charAt(j) + "";
                if(nowChar.matches("[\\u2e80-\\ufffd]")) count += 1;
            }
            int numberOfSpace = maxLength - count;
            for(int j = 0; j < numberOfSpace; j++) finalFileName += " ";
            String finalString = finalFileName + folderName;
            answerFilePaths.set(i, finalString);
        }
        for(int i = 0, len = answerFolderPaths.size(); i < len; i++) {
            String filePath = answerFolderPaths.get(i);
            int lastSlash = filePath.lastIndexOf("\\") + 1;
            String lastFolderName = filePath.substring(lastSlash);
            String folderName = filePath.substring(0, lastSlash - 1);
            String finalFolderName = lastFolderName;
            int count = finalFolderName.length();
            for(int j = 0, length = finalFolderName.length(); j < length; j++) {
                String nowChar = finalFolderName.charAt(j) + "";
                if(nowChar.matches("[\\u2e80-\\ufffd]")) count += 1;
            }
            int numberOfSpace = maxLength - count;
            for(int j = 0; j < numberOfSpace; j++) finalFolderName += " ";
            String finalString = finalFolderName + folderName;
            answerFolderPaths.set(i, finalString);
        }
    }
    
    private void setCount() {
        countFile = answerFilePaths.size();
        countFolder = answerFolderPaths.size();
    }
    
    private void setStringTime(long time) {
        if(time < 1000) {
            searchTime = time + " ms";
        } else {
            searchTime = (time % 1000) + " ms";
            time = time - time % 1000;
            time = time / 1000;
            if(time >= 60) {
                searchTime = (time % 60) + " sec "  + searchTime;
                time = time - time % 60;
                if(time >= 3600) {
                    searchTime = (time % 3600 / 60) + " min " + searchTime;
                    time = time - time % 3600;
                    searchTime = time + " h " + searchTime;
                } else {
                    searchTime = (time / 60) + " min " + searchTime;
                }
            } else {
                searchTime = time + " s "  + searchTime;
            }
        }
    }
}
