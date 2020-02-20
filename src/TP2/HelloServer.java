package TP2;

import java.rmi.server.*;
import java.util.HashMap;
import java.rmi.RemoteException;
import java.rmi.registry.*;

public class HelloServer implements Registry_itf {
	private static HelloServer instance;
	HashMap<String, Integer> map;
	
	private HelloServer() {
		this.map = new HashMap<String, Integer>();
	}
	
	public static HelloServer getInstance () {
		if (instance == null) instance = new HelloServer();
		return instance;
	}

	public static void main(String[] args) {
		try {
			HelloServer serv = new HelloServer();
			
			// Create a Hello remote object
			HelloImpl h = new HelloImpl("Hello world !");
			Hello h_stub = (Hello) UnicastRemoteObject.exportObject(h, 0);
			Hello2 h2_stub = (Hello2) h_stub;
			

			Registry_itf hs = HelloServer.getInstance();
			Registry_itf hs_stub = (Registry_itf) UnicastRemoteObject.exportObject(hs, 0);

			// Register the remote object in RMI registry with a given identifier
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("HelloService", h_stub);
			registry.bind("HelloService2", h2_stub);
			registry.bind("RegistryService", hs_stub);

			System.out.println("Server ready");

		} catch (Exception e) {
			System.err.println("Error on server :" + e);
			e.printStackTrace();
		}
	}

	@Override
	public void register(Accounting_itf client) throws RemoteException {
		this.setNumberOfCalls(((HelloClient) client).getName(), 0);
	}
	
	public int getNumberOfCalls (String clientId) {
		return this.map.get(clientId);
	}

	public void setNumberOfCalls(String clientId, int i) {
		this.map.put(clientId, i);
	}
}
