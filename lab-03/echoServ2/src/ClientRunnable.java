import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class ClientRunnable implements Runnable {
	private Socket clientSocket;

	public ClientRunnable(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public void run() {
		System.out.println("system: " + Thread.currentThread().getName() + " is going up!");

		try {
			SocketAddress socketAddress = clientSocket.getRemoteSocketAddress();
			InetSocketAddress clientAddress = (InetSocketAddress) socketAddress;

			System.out.println("Just connected:");
			System.out.println(clientAddress.getAddress() + " with port " + clientAddress.getPort());

			InputStream c_in = clientSocket.getInputStream();
			OutputStream c_out = clientSocket.getOutputStream();

			Scanner in = new Scanner(c_in);
			PrintWriter out = new PrintWriter(c_out);

			out.println("Ordinary Echo2 Server Says Hello!");
			out.flush();

			while (in.hasNextLine()) {
				String line = in.nextLine();
				System.out.println("client said: " + line);

				if (line.startsWith("bye")) {
					out.println("bye bye...");
					out.flush();
					clientSocket.close();

					break;
				}
				else if (line.startsWith("quit")) {
					out.println("aborting server...");
					out.flush();
					clientSocket.close();
					System.exit(0);
				}

				out.println(line);
				out.flush();

			}

		}
		catch (IOException exception) {
			exception.printStackTrace();
		}

		System.out.println("system: " + Thread.currentThread().getName() + " is going down!");
	}
}
