package UsefulFunction;

import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketClient {
	
	private final String IP = "127.0.0.1";
	private final int PORT = 9151;
	private final String content = "0345        BOSBOST0000         EBT-NT-B2C-040000                                                                                                                                    123456789012                    00100012230920213312    0000000000000000202109230830120000012A123456789Test.txt                    000000500000000000020020210923083100N";
//	private final String content = "0267        FNSCIF0000          IVT-AS-BND-01                                                                                                                                        123456789012                                          999900000 U160460217              11";
	
	public static void main(String[] args) {
		new SocketClient();
	}
	
    public SocketClient() {
        Socket client = new Socket();
        InetSocketAddress isa = new InetSocketAddress(this.IP, this.PORT);
        try {
            client.connect(isa, 100000);
            BufferedOutputStream out = new BufferedOutputStream(client
                    .getOutputStream());
            // 送出字串
            out.write(content.getBytes("CP937"));
            out.flush();
            out.close();
            out = null;
            client.close();
            client = null;
        } catch (java.io.IOException e) {
            System.out.println("Socket連線有問題 !");
            System.out.println("IOException :" + e.toString());
        }
    }
}
