import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

//http://java.sun.com/j2se/1.5.0/docs/api/

public class EchoServer {
	public static void main(String[] args) {
		ServerSocket serverSocket;
		Socket socket;

		try {
			int serverPort = 1234;
			serverSocket = new ServerSocket(serverPort);

			serverSocket.getInetAddress();

			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			System.out.println("ordinary Echo server started at " + localhostAddress + ":1234");

			while (true) {
				socket = serverSocket.accept();

				ClientRunnable clientRunnable = new ClientRunnable(socket);
				Thread clientThread = new Thread(clientRunnable);
				clientThread.start();
			}
		}
		catch (IOException exception) {
			exception.printStackTrace();
		}
	}
}
