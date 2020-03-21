package Chat;

import java.rmi.*;
import java.rmi.server.UID;

public class Client implements ClientService {
	private UID id;
	private String name;
	
	public Client () {
		this.id = new UID();
	}
	
	@Override	
	public UID getId() throws RemoteException {
		return this.id;
	}
	
	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void receiveMessage(Message msg) throws RemoteException {
		System.out.println(msg);
	}

	@Override
	public void receiveMessage(String msg) throws RemoteException {
		System.out.println(msg);
	}

}