import java.rmi.Remote;

public interface ArithIface extends Remote {

	public int[] add(int a[], int b[]) throws java.rmi.RemoteException;
	
}
