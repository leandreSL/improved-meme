package Chat;

import java.rmi.server.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.rmi.RemoteException;
import java.rmi.registry.*;

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
		clients.add(client);
	}
	
	@Override	
	public void sendMessage (Message msg) throws RemoteException {
		System.out.println(msg);
		//history.addMessage(msg);
		
		this.broadcast(msg);
	}

	@Override
	public void disconnect(ClientService client, String name) throws RemoteException {
		clients.remove(client);
		
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		Message msg = new Message("Message système", name, name + " a quitté le chat", LocalTime.now().format(timeFormatter));
		this.broadcast(msg);
		System.out.println(msg);
	}

	@Override
	public String getHistory() throws RemoteException {
		return history.toString();
	}
	
	public void broadcast (Message msg) throws RemoteException {
		for (ClientService c: this.clients) {
			if (c.getId().equals(msg.getId())) continue;
			c.receiveMessage(msg);
		}
	}
}
