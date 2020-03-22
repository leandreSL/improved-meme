package Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.rmi.RemoteException;

public class Server implements ServerService {
	private static Server instance;
	private List<ClientService> clients;
	private History history;
	
	private Server() {
		this.clients = Collections.synchronizedList(new ArrayList<ClientService>());
		this.history = new History();
	}
	
	public static Server getInstance () {
		if (instance == null) instance = new Server();
		return instance;
	}

	@Override
	public void register (ClientService client) throws RemoteException {
		String msg = client.getName() + " a rejoint le chat";
		history.addMessage(msg);
		for (ClientService c: this.clients) {
			try {
				c.receiveMessage(msg);
			}
			catch (Exception e) {
				
			}
		}
		System.out.println(msg);
		
		clients.add(client);
	}
	
	@Override	
	public void sendMessage (Message msg) throws RemoteException {
		System.out.println(msg);
		history.addMessage(msg);
		
		for (ClientService c: this.clients) {
			if (c.getId().equals(msg.getId())) continue;
			c.receiveMessage(msg);
		}
	}

	@Override
	public void disconnect(ClientService client, String name) throws RemoteException {
		clients.remove(client);

		String msg = name + " a quitté le chat";
		history.addMessage(msg);
		for (ClientService c: this.clients) {
			c.receiveMessage(msg);
		}
		System.out.println(msg);
	}

	@Override
	public String getHistory() throws RemoteException {
		String string_history = history.toString();
		System.out.println(string_history);
		return string_history;
	}
}
