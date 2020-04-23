import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
	public static void main(String[] args) {
		EchoServer echoServer = new EchoServer("my server");
		echoServer.go();
	}

	public EchoServer(String sname) {

	}

	public void go() {
		try {
			String localhostAddress = InetAddress.getLocalHost().getHostAddress();
			ServerSocket serverSocket = new ServerSocket(1234);
			// or
			// serverSocket = new ServerSocket(1234, 1024, InetAddress.getLocalHost());

			System.out.println("[server] ordinary EchoServer started at " + localhostAddress + ":1234");

			while (true) {
				Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public static void handleClient(Socket clientSocket) throws IOException {
		InputStream clientInputStream = clientSocket.getInputStream();
		OutputStream clientOutputStream = clientSocket.getOutputStream();

		Scanner in = new Scanner(clientInputStream);
		PrintWriter out = new PrintWriter(clientOutputStream);

		String clientIPAddress = clientSocket.getInetAddress().getHostAddress();

		System.out.println("[server] talking to " + clientIPAddress + ", sending: greetings...");
		out.println("ordinary Echo server welcomes you...");
		out.flush();

		while (in.hasNextLine()) {
			String line = in.nextLine();

			System.out.println("[server] client " + clientIPAddress + " said: " + line);

			if (line.startsWith("bye")) {
				System.out.println("[server] talking to " + clientIPAddress + ", sending: bye...");
				System.out.println("--------------------------------------------------------------");
				out.println("bye bye...");
				out.flush();

				in.close();
				out.close();
				clientSocket.close();

				break;
			}
			else if (line.startsWith("quit")) {
				System.out.println("[server] talking to " + clientIPAddress + ", sending: I'm out...");
				out.println("aborting server...");
				out.flush();

				in.close();
				out.close();
				clientSocket.close();

				System.exit(0);
			}
			else {
				out.println(line);
				System.out.println("[server] talking to " + clientIPAddress + ", sending: " + line);
				out.flush();
			}
		}
	}
}
