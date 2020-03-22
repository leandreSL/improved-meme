package Chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerService extends Remote {
	public void register (ClientService client) throws RemoteException;
	public void sendMessage(Message message) throws RemoteException;
	public String getHistory() throws RemoteException;
	public void disconnect(ClientService client, String name) throws RemoteException ;
}
