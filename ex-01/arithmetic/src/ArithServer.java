
import java.rmi.*;

import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;

// remote byte code access;
//
// -Djava.rmi.server.codebase=http://rmi.yaht.net/repo/arith.bin/

//
// local byte code access;
// -Djava.rmi.server.codebase=file:/C:\Users\<your-user>\Desktop\workspace\arithmetic\bin/
// or
// -Djava.rmi.server.codebase=file:///some-local-unix-path/


// thus Run configuration VM arguments become: 
// -Djava.rmi.server.codebase=http://rmi.yaht.net/repo/arith.bin/ -Djava.rmi.server.useCodebaseOnly=false

public class ArithServer extends UnicastRemoteObject implements ArithIface {

	private static final long serialVersionUID = -3297373988297707790L;
	private String name;
	
	public ArithServer(String name) throws RemoteException {
		super();
		this.setName(name);
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public int[] add(int[] a, int[] b) throws RemoteException {
		
		int c[] = new int[10];
		for (int i=0; i<10; i++) { c[i] = a[i] + b[i]; }
        return c;
        
	}

	public static void main(String[] args) {
		
		// RMI will not start without this... Security is always major milestone, especially in Java :)  
        System.setProperty("java.security.policy", "src/security.policy");
        System.setSecurityManager(new SecurityManager());
        
        // The following one is deprecated in Java 8;
        // System.setSecurityManager(new RMISecurityManager());
        
        try {
        	
        	// I'm trying to find my "local" IP address...
        	String lha = InetAddress.getLocalHost().getHostAddress().toString();
        	System.out.println("[info] local ip address found: " + lha);
        	
        	// Instantiating our remote object. It even has a name :) 
        	ArithServer obj = new ArithServer("ArithServer");
        	
        	// Trying to show our remote object to the rest of the world ...
        	Naming.rebind("//" + lha + "/ArithServer", obj);
        	
        	System.out.println("[info] " + obj.getName() + " bound in registry @ " + lha + ":1099");
        	System.out.println("[info] public address for our server is: //" + lha + ":1099/" );
        	
        } catch (Exception e) {
        	
        	System.out.println("[halt] ArithServer err: " + e.getMessage());
        	// e.printStackTrace();
        	
        }
		
	}

}
