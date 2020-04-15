
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

		new EchoServer("my server").go();

	}

	public EchoServer(String sname) {

	}

	public void go() {

		ServerSocket s;

		try {

			String lha = InetAddress.getLocalHost().getHostAddress();

			s = new ServerSocket(1234);
			// or
			// s = new ServerSocket(1234, 1024, InetAddress.getLocalHost());
			System.out.println("[server] ordinary EchoServer started at " + lha + ":1234");

			while (true) {

				Socket c = s.accept();
                _handle_cc(c);

			}

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	public static void _handle_cc(Socket c) throws IOException {
		
		InputStream c_in = c.getInputStream();
		OutputStream c_out = c.getOutputStream();
		String c_ip = c.getInetAddress().getHostAddress();
		
		Scanner in = new Scanner(c_in);
		PrintWriter out = new PrintWriter(c_out);
		
		System.out.println("[server] talking to " + c_ip + ", sending: greetings...");
		out.println("ordinary Echo server welcomes you...");
		out.flush();
		
		while (in.hasNextLine()) {
			
			String line = in.nextLine();
			
			System.out.println("[server] client " + c_ip + " said: " + line);
			if (line.startsWith("bye")) {
				
				System.out.println("[server] talking to " + c_ip + ", sending: bye...");
				out.println("bye bye...");
				out.flush();
				
				in.close();
				out.close();
				c.close();
				
				break;
				
			} else if (line.startsWith("quit")) {
				
				System.out.println("[server] talking to " + c_ip + ", sending: I'm out...");
				out.println("aborting server...");
				out.flush();
				
				in.close();
				out.close();
				c.close();
				
				System.exit(0);
				
			}
			
			out.println(line);
			System.out.println("[server] talking to " + c_ip + ", sending: " + line);
			out.flush();
			
		}
		
	}
	
}
