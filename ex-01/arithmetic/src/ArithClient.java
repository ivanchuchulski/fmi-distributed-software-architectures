
import java.rmi.*;

//remote byte code access;
//
//-Djava.rmi.server.codebase=http://rmi.yaht.net/repo/arith.bin/

//
//local byte code access;
//-Djava.rmi.server.codebase=file:/C:\Users\<your-user>\Desktop\workspace\arithmetic\bin/
//or
//-Djava.rmi.server.codebase=file:///some-local-unix-path/


//thus Run configuration VM arguments become: 
//-Djava.rmi.server.codebase=http://rmi.yaht.net/repo/arith.bin/ -Djava.rmi.server.useCodebaseOnly=false

public class ArithClient {

    public static void main(String argv[]) {
    	
        int a[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        int b[] = {0, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        
        int result[] = new int[10];
        
		// RMI will not start without this... Security is always major milestone, especially in Java :)  
        System.setProperty("java.security.policy", "src/security.policy");
        System.setSecurityManager(new SecurityManager());
        
        // The following one is deprecated in Java 8;
        // System.setSecurityManager(new RMISecurityManager());

        String roServer = "localhost";
        String roReg = roServer + ":1099";
        
        try {
        	
        	String bindings[] = Naming.list("//" + roReg +"/");
        	
        	System.out.println("[info] remote objects found @ " + roReg);
        	for(int i=0; i < bindings.length; i++)
        		System.out.println(bindings[i]);
        	
        	System.out.println("[info] instantiating and calling the remote method...");
        	ArithIface obj = (ArithIface) Naming.lookup("//" + roServer + "/ArithServer");
        	result = obj.add(a, b);
           
        } catch (Exception e) {
        	
        	System.out.println("[halt] ArithClient err: " + e.getMessage());
        	// e.printStackTrace();
        	
        }
        
        System.out.print("[info] result array:");
        for (int j=0; j<10; j++) {
        	System.out.print("    " + result[j]);
        }
        System.out.println();
        
    }
    
}
