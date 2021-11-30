package UsefulFunction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import Entity.EAILog;
import Entity.Repo_Log;

public class OpenShiftTime {

//	private String path = "csv/Old/All-Messages-search-result (1).csv";
	private String path = "csv/BOSBOSTStress.csv";
	private String timeFormat = "yyyy-MM-ddHH:mm:ss,SSS";
	private long totalTime = 0L;
	private String fileTitle1 = "ComgmbProcessCostTime.txt";
	private String fileTitle2 = "ComgmbAllLog.txt";
	private double count = 0;
	private int overTimeCount = 0;
	private int beCostLonTimeCount = 0;
	private List<String> exceptionList = new ArrayList<>();
	private DecimalFormat df = new DecimalFormat("##.##");
	private long costMaxTime = 0L;
	private StringBuilder totalString = new StringBuilder();
	private StringBuilder totalo360Logs = new StringBuilder();
	private int overTime = 1000;

	private Set<String> o360Seqs = new HashSet<>();
	private Map<String, EAILog> o360SeqLogs = new HashMap<>();
	private List<EAILog> eaiLogs = new ArrayList<>();
	private List<Repo_Log> logList = new ArrayList<>();
	private List<String> logs = new ArrayList<>();
	private List<Long> diffTimeList = new ArrayList<>();

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		OpenShiftTime ost = new OpenShiftTime();
		ost.analysis();
		long finalTime = System.currentTimeMillis() - startTime;
		System.out.println("Total Cost Time : " + (finalTime >= 1000 ? (finalTime / 1000) + "s " + (finalTime % 1000) + "ms " : finalTime + "ms "));
	}

	public OpenShiftTime() {
		readXML();
	}

	private void readXML() {
		long startTime = System.currentTimeMillis();
		File file = new File(path);
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(file))) {
			BufferedReader br = new BufferedReader(reader);
			if (br != null) {
				String line = br.readLine();
				 logs.add(line.replace("\"", "").replaceAll("\",\"2021-11", " ; 2021-11").replaceAll("\"\"", "\""));
				while (line != null) {
					line = br.readLine();
					if(line != null && !line.contains("SocketDecoder")) {
						line = line.replaceAll("\",\"2021-11", " ; 2021-11").replaceAll("\"\"", "\"");
						line = line.substring(0, line.lastIndexOf("\"") < 0 ? line.length() : line.lastIndexOf("\""));
						line = line.replaceFirst("\"", "");
						logs.add(line);
					}
					
				}
				br.close();
				System.out.println("Total Log Lines = " + logs.size());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long finalTime = System.currentTimeMillis() - startTime;
		System.out.println("readXML Cost Time : " + (finalTime >= 1000 ? (finalTime / 1000) + "s " + (finalTime % 1000) + "ms " : finalTime + "ms "));
	}

	@SuppressWarnings("unchecked")
	private void analysis() {
		long startTime = System.currentTimeMillis();
		TimeCompare tc = new TimeCompare();
		for(String log : logs) {
			Repo_Log rl = new Repo_Log();
			if(log != null) {
				String[] datas = log.split(" ; ");
				if (datas.length == 2) {
					String o360Seq = "";
					String time = "";
					String[] logSplit = datas[1].split(" ");
					time = logSplit[0] + logSplit[1];
					for (String s : logSplit) {
						if (s.contains("O360SEQ[")) {
							o360Seq = s.replace("O360SEQ[", "").replace("]", "");
							break;
						} else if (s.contains("][")) {
							if(s.split("\\]\\[").length == 3) {
    							o360Seq = s.split("\\]\\[")[2];
    							break;
							} else if(s.split("\\]\\[").length == 7) {
								Pattern p = Pattern.compile("\\[[0-9]{20}\\]");
								Matcher m = p.matcher(datas[1]);
								m.find();
								o360Seq = m.group(0).replace("[", "").replace("]", "");
//								System.out.println(o360Seq);
    							break;
							} else {
								o360Seq = datas[1].split("\\]\\[")[3];
    							break;
							}
						} else if(s.contains("FUSE_ID=")) {
							o360Seq = s.replace("FUSE_ID=", "").replaceAll(",", "");
						} else if(s.contains("unknown")) {
							Pattern p = Pattern.compile("[0-9]{20}");
							Matcher m = p.matcher(datas[1]);
							m.find();
							o360Seq = m.group(0);
						}
					}
					rl.setRepo(datas[0].replaceAll("\"", ""));
					rl.setLog(datas[1]);
					rl.setTime(time);
					rl.setO360Seq(o360Seq);
					if(!"".equals(o360Seq) && o360Seqs.add(o360Seq)) {
						EAILog eaiLog = new EAILog();
						eaiLog.setO360seq(o360Seq);
						eaiLog.setLogList(new ArrayList<>());
						o360SeqLogs.put(o360Seq, eaiLog);
					}
					logList.add(rl);
				}
			}
		}
		if(logList != null && tc != null) {
			Collections.sort(logList, tc);
			for(Repo_Log rl : logList) {
				if(!"".equals(rl.getO360Seq())) {
					EAILog eaiL = o360SeqLogs.get(rl.getO360Seq());
					eaiL.getLogList().add(rl);
				}
			}
		}
		System.out.println("Initial Total O360 count = " + o360SeqLogs.size());
		Set<String> keys = o360SeqLogs.keySet();
		List<String> keyList = new ArrayList<>();
		for(String key : keys) {
			keyList.add(key);
		}
		for(String s : keyList) {
			if(o360SeqLogs.get(s).getLogList().size() < 0) {
				o360SeqLogs.remove(s);
			} else {
				EAILog eaiLog = o360SeqLogs.get(s);
				eaiLogs.add(eaiLog);
			}
		}
		System.out.println("Final Total O360 count = " + o360SeqLogs.size());
		long finalTime = System.currentTimeMillis() - startTime;
		System.out.println("analysis Cost Time : " + (finalTime >= 1000 ? (finalTime / 1000) + "s " + (finalTime % 1000) + "ms " : finalTime + "ms "));
		
		startTime = System.currentTimeMillis();
		settingMsgId();
		finalTime = System.currentTimeMillis() - startTime;
		System.out.println("settingMsgId Cost Time : " + (finalTime >= 1000 ? (finalTime / 1000) + "s " + (finalTime % 1000) + "ms " : finalTime + "ms "));
		
		startTime = System.currentTimeMillis();
		sortTime();
		finalTime = System.currentTimeMillis() - startTime;
		System.out.println("sortTime Cost Time : " + (finalTime >= 1000 ? (finalTime / 1000) + "s " + (finalTime % 1000) + "ms " : finalTime + "ms "));
		
	}
	
	private void settingMsgId() {
		for(int i = 0, len = eaiLogs.size(); i < len; i++) {
			List<Repo_Log> repoLog = eaiLogs.get(i).getLogList();
			int count = 1;
			for(Repo_Log rl : repoLog) {
				if(rl.getRepo().equals("eai-auth") && rl.getLog().contains("[R]")) {
					
					Pattern p = Pattern.compile("\\[([A-Za-z0-9]{1})]\\[([A-Za-z0-9-]{1,})]\\[([A-Za-z0-9-]{1,})]\\[([A-Za-z0-9]{1,})]\\[([A-Za-z0-9]{1,})]");
					Matcher m = p.matcher("[R]" + rl.getLog().split("\\[R]")[1]);
					m.matches();
					if(count == 1) {
						eaiLogs.get(i).setFrontMsgid(m.group(2));
						count += 1;
					} else {
						eaiLogs.get(i).setBackMsgId(m.group(2));
						break;
					}
					
				} else if(rl.getLog().contains("(In)") && rl.getLog().contains("MSGID[")) {
					String[] logSplit = rl.getLog().split("MSGID\\[");
					if(logSplit.length > 0) {
						eaiLogs.get(i).setFrontMsgid(logSplit[1].substring(0, logSplit[1].indexOf("]")));
					}
				} else if(rl.getLog().contains("unknown")) {
					String[] logSplit = rl.getLog().split(",");
					for(String s : logSplit) {
						if(s.contains("MSGID")) {
							eaiLogs.get(i).setFrontMsgid(s.replace("}", "").substring(s.indexOf("=")));
							break;
						}
					}
					
				}
			}
		}
	}
	
	private void sortTime() {
		
		File file1 = new File("TXT/ComgmbLogAnalysis/" + getDateTime() + fileTitle1);
        if(!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                System.out.println("Create File Failed!!!");
            }
        }
        
        File file2 = new File("TXT/ComgmbLogAnalysis/" + getDateTime() + fileTitle2);
        if(!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                System.out.println("Create File Failed!!!");
            }
        }
        try(FileWriter writer1 = new FileWriter(file1, true);
        		FileWriter writer2 = new FileWriter(file2, true);) {
        	df = new DecimalFormat("0.00");
    		BufferedWriter bw1 = new BufferedWriter(writer1);
    		BufferedWriter bw2 = new BufferedWriter(writer2);
    		double length = o360SeqLogs.size();
    		long outStartTime = System.currentTimeMillis();
    		o360SeqLogs.keySet().forEach(o360seq -> {
    			count = count + 1;
    			
    			EAILog eaiLog = o360SeqLogs.get(o360seq);
    			List<Repo_Log> logList = eaiLog.getLogList();
    			StringBuilder showString = new StringBuilder();
    			StringBuilder o360Logs = new StringBuilder();
    			
    			for(int i = 0, len = logList.size(); i < len; i++) {
    				if(i != len - 1) {
        				String time1 = logList.get(i).getTime();
    					String time2 = logList.get(i + 1).getTime();
    					timeDiff(time1, time2);
    				}
    				o360Logs.append(logList.get(i).getLog() + "\n");
    			}
    			eaiLog.setDiffTimeList(diffTimeList);
    			long startTime = System.currentTimeMillis();
    			totalTime = Math.abs(totalTime);
    			if(totalTime > (long)overTime) {
    				overTimeCount += 1;
    				showString.append("前端MSGID : " + eaiLog.getFrontMsgid() + "\n");
    				if(eaiLog.getBackMsgId() != null)
    					showString.append("後端MSGID : " + eaiLog.getBackMsgId() + "\n");
    				showString.append("O360SEQ : " + eaiLog.getO360seq() + "\n"
        					+ "Total Cost Time = " + totalTime + " ms" + "\n"
        					+ "中間最大耗時 : " + (costMaxTime >= 1000L ? (costMaxTime / 1000) + "s " + (costMaxTime % 1000) + "ms " : costMaxTime + "ms ") + "\n");
    				List<Long> timeList = eaiLog.getDiffTimeList();
    				for(int i = 0, len = timeList.size(); i < len; i++) {
    					long time = timeList.get(i);
    					String timeString = time >= 1000 ? (time / 1000) + "s " + (time % 1000) + "ms" : time + "ms";
    					if(time == costMaxTime) {
    						showString.append("最大耗時流程 : " + logList.get(i).getRepo() + " -" + timeString + "-> " + logList.get(i + 1).getRepo() + "\n");
    						boolean repoBEFlag = logList.get(i).getRepo().contains("-be-") && logList.get(i + 1).getRepo().contains("-be-");
    						boolean logBEFlag = logList.get(i).getLog().contains("[BE]") && logList.get(i + 1).getLog().contains("[BE]");
    						if(repoBEFlag && logBEFlag) {
        						showString.append(logList.get(i).getLog().substring(0, 24) + "[BE]" + logList.get(i).getLog().split("\\[BE]")[1] + "\n");
        						showString.append(logList.get(i + 1).getLog().substring(0, 24) + "[BE]" + logList.get(i + 1).getLog().split("\\[BE]")[1] + "\n");
        						beCostLonTimeCount += 1;
        					} else {
        						exceptionList.add(logList.get(i).getO360Seq());
        					}
    					}
    				}
    				StringBuilder processString = new StringBuilder();
    				StringBuilder overOneSecond = new StringBuilder();
    				for(int i = 0, len = timeList.size(); i < len; i++) {
    					long longTime = timeList.get(i);
    					String time = longTime >= 1000 ? (longTime / 1000) + "s " + (longTime % 1000) + "ms" : longTime + "ms";
    					
    					double countPer = 0.00;
    					countPer = longTime * 100D / totalTime;
    					
    					if(longTime >= 1000) {
    						overOneSecond.append(logList.get(i).getRepo() + " -" + time + "(" + df.format(countPer) + "%)-> " + logList.get(i + 1).getRepo() + "\n");
    					}
    					
    					if(i != len - 1) {
    						processString.append(time + "(" + df.format(countPer) + "%)-> " + logList.get(i + 1).getRepo() + " -");
    					} else {
    						processString.append(time + "(" + df.format(countPer) + "%)-> " + logList.get(i + 1).getRepo() + "\n");
    					}
    				}
    				if(overOneSecond.toString().length() > 0) {
    					showString.append("超過1秒的流程 : \n");
    					showString.append(overOneSecond.toString());
    				}
    				showString.append(logList.get(0).getRepo() + " -");
    				showString.append(processString.toString());
        			totalo360Logs.append("前端MSGID : " + eaiLog.getFrontMsgid() + "\n");
        			if(eaiLog.getBackMsgId() != null)
        				totalo360Logs.append("後端MSGID : " + eaiLog.getBackMsgId() + "\n");
        			totalo360Logs.append("O360SEQ : " + eaiLog.getO360seq() + "\n"
        					+ "Total Cost Time = " + totalTime + " ms" + "\n" 
        					+ "中間最大耗時 : " + (costMaxTime >= 1000 ? (costMaxTime / 1000) + "s " + (costMaxTime % 1000) + "ms " : costMaxTime + "ms ") + "\n"
        					+ o360Logs.toString() + "\n");
    //    			System.out.println(showString);
        			if(count % 100 == 0) {
        				System.out.println(getDateTime() + " Transit Time = " + (System.currentTimeMillis() - startTime) + "ms " + df.format(count / length * 100.0) + " %");
        			}
        			totalString.append(showString.toString() + "\n");
    			}
    			diffTimeList = new ArrayList<>();
    			costMaxTime = 0L;
    			totalTime = 0L;
    			
    		});
    		System.out.println(getDateTime() + " Total Transit Time = " + (System.currentTimeMillis() - outStartTime) + "ms " + df.format(count / length * 100.0) + " %");
			System.out.println("超過" + df.format((double)overTime / 1000d) + "秒電文數 / 總電文數 : " + overTimeCount + " / " + (int)count);
//			System.out.println("是BE -> 後端 -> BE慢的數量 / 超過" + df.format((double)overTime / 1000d) + "秒電文數：" + beCostLonTimeCount + " / " + overTimeCount);
//			System.out.println("不屬於上數的例外電文：\n");
//			for(String o360Seq : exceptionList) {
//				System.out.println(o360Seq);
//			}
    		if(bw1 != null) {
    			bw1.write(totalString.toString());
    			bw1.flush();
    			bw1.close();
    		}
    		if(bw2 != null) {
    			bw2.write(totalo360Logs.toString());
    			bw2.flush();
    			bw2.close();
    		}
        } catch(IOException e) {
            e.printStackTrace();
        }
	}
	
	public String getDateTime(){
		SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		String strDate = sdFormat.format(date);
		return strDate;
	}
	
	private String timeDiff(String time1, String time2) {
		SimpleDateFormat format = new SimpleDateFormat(timeFormat);
		try {
			Date date1 = format.parse(time1);
			Date date2 = format.parse(time2);
			long diff = date2.getTime() - date1.getTime();
			diff = Math.abs(diff);
			if(costMaxTime < diff) {
				costMaxTime = diff;
			}
			diffTimeList.add(diff);
			totalTime += diff;
			if(diff > 1000L) {
				return ((int) diff / 1000) + "s " + ((int) diff % 1000) + "ms ";
			} else {
				return diff + "ms ";
			}
		} catch (ParseException e) {
			return "";
		}
	}
}
