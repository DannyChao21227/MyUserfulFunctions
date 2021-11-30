package UsefulFunction;

import java.io.IOException;
import java.net.SocketException;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ConnectToFTP {
    
    public static void main(String[] args) {
        ConnectToFTP connect = new ConnectToFTP();
        connect.testConnect();
    }
    
    public FTPClient getFTPClient() {
        FTPClient ftpClient = null;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect("88.8.138.155", 22);
            ftpClient.login("dmws3adm", "dmws3adm");
            if(!FTPReply.isPositiveCompletion(ftpClient.getReply())) {
                System.out.println("未連線到FTP，使用者名稱或者密碼錯誤");
            } else {
                System.out.println("成功連線");
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ftpClient;
    }
    
    public void testConnect() {
        ChannelSftp sftp = null;
        Channel channel = null;
        Session sshSession = null;
        
        try {
            JSch jSch = new JSch();
            sshSession = jSch.getSession("dmws3adm", "88.8.138.155", 22);
            sshSession.setPassword("dmws3adm");
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            channel = sshSession.openChannel("sftp");
//            channel.setInputStream(new ByteArrayInputStream("find . -type f -name \"*OFTGEBTRAN*\"".getBytes(StandardCharsets.UTF_8)));
//            channel.setOutputStream(System.out);
//            InputStream in = channel.getInputStream();
//            StringBuilder outBuff = new StringBuilder();
//            int exitStatus = 1;
            
            channel.connect();
            
//            while(in.read() >= 0) {
//                for(int c; ((c = in.read()) >= 0);) {
//                    outBuff.append((char) c);
//                }
//                if(channel.isClosed()) {
//                    if(in.available() > 0) continue;
//                    exitStatus = channel.getExitStatus();
//                    break;
//                }
//            }
//            System.out.println(outBuff.toString());
//            System.out.print ("Exit status of the execution: " + exitStatus);
//            if ( exitStatus == 0 ) {
//                System.out.print (" (OK)\n");
//            } else {
//                System.out.print (" (NOK)\n");
//            }
            sftp = (ChannelSftp) channel;
            Vector<?> vector = sftp.ls("/MT/dmws3adm/etcfiles/flow");
            for(Object item : vector) {
                LsEntry entry = (LsEntry) item;
                if(entry.getFilename().contains("070247") || entry.getFilename().contains("070248") || entry.getFilename().contains("070249") || entry.getFilename().contains("070250")) {
                    System.out.println(entry.getFilename());
                }
            }
//            vector = sftp.ls("/MT/dmws3adm/etcfiles/flow");
//            for(Object item : vector) {
//                LsEntry entry = (LsEntry) item;
//                
//                System.out.println(entry.getFilename() + " is director : " + entry.getAttrs().isDir());
//            }
        } catch(Exception e) {
            
        } finally {
            if(sftp != null)
                sftp.disconnect();
            if(channel != null)
                channel.disconnect();
            if(sshSession != null)
                sshSession.disconnect();
        }
    }
}
