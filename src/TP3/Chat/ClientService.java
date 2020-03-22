package Chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UID;

public interface ClientService extends Remote {
	public String getName() throws RemoteException;
	public UID getId() throws RemoteException;
	
	public void receiveMessage(Message msg) throws RemoteException;
	public void receiveMessage(String msg) throws RemoteException;
}