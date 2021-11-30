import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SearchFolderOrFile {
    
    private String inputPathsString = "";
    private String inputNamesString = "";
    private String inputTypesString = "";
    
    private String[] searchNames;
    private String[] searchPaths;
    private String[] searchTypes;
    
    private List<File> allFolder = new ArrayList<>();
    private List<File> allFile = new ArrayList<>();
    private List<List<File>> allFileResultByPath = new ArrayList<>();
    private List<List<File>> allFolderResultByPath = new ArrayList<>();
    private List<List<String>> resultFilesByPath = new ArrayList<>();
    private List<List<String>> resultFoldersByPath = new ArrayList<>();
    
    private int countFolder = 0;
    private int countFile = 0;
    private int totalFolder = 0;
    private int totalFile = 0;
    
    private String searchTime = "";
    private List<String> contents = new ArrayList<>();
    private String answerTXTPath = "SearchResult.txt";
    private int maxFileNameLength = 0;
    private int maxFolderNameLength = 0;
    private int maxLength = 0;
    private long maxPathLength = 0;
    private final String filterString;
    
    public static void main(String[] args) {
        String[] strings;
        if(args.length < 2) {
//            strings = new String[] {"D:\\Users\\NT88603\\SVN\\CUBMW\\04.資料庫規格;D:\\Users\\NT88603\\SVN\\CUBMW\\05.電文規格;"
//                    + "D:\\Users\\NT88603\\SVN\\CUBMW\\06.系統分析;D:\\Users\\NT88603\\SVN\\CUBMW\\19.系統設計", "all", "MIDT24TXN0004", "all"};
        	strings = new String[] {"D:\\", "all", "archetype-quickstart", "all"};
//            strings = new String[] {"D:\\Users\\NT88603\\SVN", "all", "auth", "all"};
//        	strings = new String[] {"D:\\Users\\NT88603\\GitFolder\\MIDR6OTR01-MWTR\\MW1\\etcfiles\\copy", "all", "0021", "all"};
        } else {
            strings = args;
        }
        SearchFolderOrFile sfof = new SearchFolderOrFile(strings);
        sfof.start();
    }
    
    public SearchFolderOrFile(String[] conditions) {
        if(conditions != null && conditions.length == 4) {
            inputPathsString = conditions[0];
            filterString = conditions[1];
            inputNamesString = conditions[2];
            inputTypesString = conditions[3];
            searchPaths = inputPathsString.split(";");
            searchNames = inputNamesString.split(":");
            searchTypes = inputTypesString.split(":");
        } else {
            filterString = "";
        }
    }
    
    public void start() {
        long startTime = System.currentTimeMillis();
        
        startSearch();
        formattResultList();
        String s = "";
        for(int i = 0; i < maxPathLength; i++) {
            s += "=";
        }
        contents.add("Search Conditions : " + inputNamesString.replaceAll(":", ", "));
        contents.add("Total folder count : " + totalFolder);
        contents.add("Total file count : " + totalFile + "\n");
        int count = 0;
        for(List<String> list : resultFoldersByPath) count += list.size();
        contents.add("Total folder found : " + count);
        count = 0;
        for(List<String> list : resultFilesByPath) count += list.size();
        contents.add("Total file found : " + count + "\n");
        for(int i = 0, len = searchPaths.length; i < len; i++) {
            String path = searchPaths[i];
            setOutputString(path, i);
            if(searchPaths.length > 1) {
                contents.add(s);
                contents.add("");
            }
        }
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
        
        // Create Result File
        SimpleDateFormat formatter= new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        answerTXTPath = "SearchResult/" + formatter.format(date) + answerTXTPath;
        CreateTXT cTXT = new CreateTXT(new File(answerTXTPath), contents);
        cTXT.createTXT();
    }
    
    private void startSearch() {
        for(int i = 0, len = searchPaths.length; i < len; i++) {
            String path = searchPaths[i];
            File file = new File(path);
            if(file.exists()) {
                if(file.isDirectory()) {
                    ifFolder(file);
                } else {
                    ifFile(file);
                }
            } else {
                contents.add("Path : " + path + " not found!!!");
                System.out.println("Path : " + path + " not found!!!");
            }
            switch (filterString.toLowerCase()) {
                case "all":
                    forAll();
                    break;
                case "folder":
                    forFolder();
                    break;
                case "file":
                    forFile();
                    break;
            }
            allFolder.clear();
            allFile.clear();
        }
    }
    
    private void forAll() {
        List<File> folderResult = new ArrayList<>();
        List<File> fileResult = new ArrayList<>();
        
        
        for(File file : allFolder) {
            int nowLength = getResult(file, folderResult);
            maxFolderNameLength = nowLength > maxFolderNameLength ? nowLength : maxFolderNameLength;
        }
        for(File file : allFile) {
            int nowLength = getResult(file, fileResult);
            maxFileNameLength = nowLength > maxFileNameLength ? nowLength : maxFileNameLength;
        }
        
        allFolderResultByPath.add(folderResult);
        allFileResultByPath.add(fileResult);
    }
    
    private void forFolder() {
        List<File> folderResult = new ArrayList<>();
        for(File file : allFolder) {
            int nowLength = getResult(file, folderResult);
            maxFolderNameLength = nowLength > maxFolderNameLength ? nowLength : maxFolderNameLength;
        }
        
        allFolderResultByPath.add(folderResult);
    }
    
    private void forFile() {
        List<File> fileResult = new ArrayList<>();
        for(File file : allFile) {
            int nowLength = getResult(file, fileResult);
            maxFileNameLength = nowLength > maxFileNameLength ? nowLength : maxFileNameLength;
        }
        allFileResultByPath.add(fileResult);
    }
    
    private void ifFolder(File file) {
        totalFolder += 1;
        allFolder.add(file);
        String[] filesInFolder = file.list();
        if(filesInFolder != null) {
            for(String nowPath : filesInFolder) {
                File nowFile = new File(file.getAbsolutePath() + "\\" + nowPath);
                if(nowFile.isDirectory()) {
                    ifFolder(nowFile);
                } else {
                    ifFile(nowFile);
                }
            }
        } else {
            ifFile(file);
        }
    }
    
    private void ifFile(File file) {
        totalFile += 1;
        long length = file.getAbsolutePath().length();
        
        if(inputTypesString.equals("all")) {
            if(length > maxPathLength) maxPathLength = length;
            allFile.add(file);
        } else {
            for(String type : searchTypes) {
                String fileName = file.getName();
                int dotIndex = fileName.lastIndexOf(".");
                if(file.getName().substring(dotIndex >= 0 ? dotIndex : 0).contains(type)) {
                    if(length > maxPathLength) maxPathLength = length;
                    allFile.add(file);
                }
            }
        }
    }
    
    private int getResult(File file, List<File> result) {
        int maxStringLength = 0;
        String fileName = file.getName();
        fileName = fileName.toUpperCase();
        for(String name : searchNames) {
            name = name.toUpperCase();
            if(fileName.contains(name)) {
                if(fileName.length() > maxStringLength) {
                    maxStringLength = fileName.length();
                    
                }
                result.add(file);
                break;
            }
        }
        return maxStringLength;
    }
    
    private void formattResultList() {
        if(maxFileNameLength > maxFolderNameLength) {
            maxLength = maxFileNameLength;
        } else if(maxFolderNameLength > maxFileNameLength) {
            maxLength = maxFolderNameLength;
        }
        maxLength += 10;
        
        for(List<File> files : allFileResultByPath) {
            resultFilesByPath.add(new ArrayList<>());
            for(File file : files) {
                String name = file.getName();
                int count = name.length();
                for(int i = 0, length = name.length(); i < length; i++) {
                    String nowChar = name.charAt(i) + "";
                    if(nowChar.matches("[\\u2e80-\\ufffd]")) count += 1;
                }
                int numberOfSpace = maxLength - count;
                for(int i = 0; i < numberOfSpace; i++) name += " ";
                String finalString = name + file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\" + file.getName()));
                resultFilesByPath.get(resultFilesByPath.size() - 1).add(finalString);
            }
        }
        for(List<File> folders : allFolderResultByPath) {
            resultFoldersByPath.add(new ArrayList<>());
            for(File file : folders) {
                String name = file.getName();
                int count = name.length();
                for(int i = 0, length = name.length(); i < length; i++) {
                    String nowChar = name.charAt(i) + "";
                    if(nowChar.matches("[\\u2e80-\\ufffd]")) count += 1;
                }
                int numberOfSpace = maxLength - count;
                for(int i = 0; i < numberOfSpace; i++) name += " ";
                String finalString = name + file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\" + file.getName()));
                resultFoldersByPath.get(resultFoldersByPath.size() - 1).add(finalString);
            }
        }
        System.out.println("formattResultList end");
    }
    
    private void setOutputString(String path, int count) {
        switch (filterString.toLowerCase()) {
            case "all":
                countFolder = allFolderResultByPath.get(count).size();
                countFile = allFileResultByPath.get(count).size();
                contents.add("Path : " + path);
                contents.add("Total found : " + (countFolder + countFile));
                contents.add("Total found folders : " + countFolder);
                for(String getName : resultFoldersByPath.get(count)) {
                    contents.add(getName);
                }
                contents.add("");
                contents.add("Total found files : " + countFile);
                for(String getName : resultFilesByPath.get(count)) {
                    contents.add(getName);
                }
                contents.add("");
                break;
            case "folder":
                countFolder = allFolderResultByPath.get(count).size();
                countFile = 0;
                contents.add("Path : " + path);
                contents.add("Total found : " + (countFolder + countFile));
                contents.add("Total found folders : " + countFolder);
                for(String getName : resultFoldersByPath.get(count)) {
                    contents.add(getName);
                }
                contents.add("");
                break;
            case "file":
                countFolder = 0;
                countFile = allFileResultByPath.get(count).size();
                contents.add("Path : " + path);
                contents.add("Total found : " + (countFolder + countFile));
                contents.add("Total found files : " + countFile);
                for(String getName : resultFilesByPath.get(count)) {
                    contents.add(getName);
                }
                contents.add("");
                break;
        }
        
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

	public List<List<File>> getAllFileResultByPath() {
		return allFileResultByPath;
	}

	public List<List<File>> getAllFolderResultByPath() {
		return allFolderResultByPath;
	}

	public List<List<String>> getResultFilesByPath() {
		return resultFilesByPath;
	}

	public List<List<String>> getResultFoldersByPath() {
		return resultFoldersByPath;
	}

	public List<String> getContents() {
		return contents;
	}
	
}
