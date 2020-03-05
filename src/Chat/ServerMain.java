package Chat;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain {
	public static void main(String[] args) {
		try {
			// Create a Server remote object			
			ServerService serverInstance = Server.getInstance();
			ServerService server_stub = (ServerService) UnicastRemoteObject.exportObject(serverInstance, 0);

			// Register the remote object in RMI registry with a given identifier
			Registry registry = LocateRegistry.getRegistry();
			registry.bind("ServerService", server_stub);
			
			System.out.println("Server ready");

		} catch (Exception e) {
			System.err.println("Error on server :" + e);
			e.printStackTrace();
		}
	}
}
