package Chat;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class ClientMain {
	public static void main(String[] args) {

		try (Scanner scanner = new Scanner(System.in)) {
			if (args.length < 1) {
				System.out.println("Usage: java HelloClient <rmiregistry host>");
				return;
			}

			String host = args[0];
			Message m;
			// Get remote object reference
			Registry registry = LocateRegistry.getRegistry(host);
			ServerService serverService = (ServerService) registry.lookup("ServerService");

			// Create a Client remote object			
			Client client = new Client();
			ClientService clientService = (ClientService) client;
			ClientService client_stub = (ClientService) UnicastRemoteObject.exportObject(clientService, 0);
			registry.bind(client.getId().toString(), client_stub);

			System.out.print("Veuillez entrer votre pseudo :\n");
            client.setName(scanner.nextLine());
			serverService.register(client_stub);
            
            System.out.println("Bienvenue. \nEntrez \"!exit\" pour quitter");
            System.out.println("Entrez \"!history\" pour avoir l'historique du chat");
			String userInput;
			while ((userInput = scanner.nextLine()) != null) {
				if (userInput.equals("!exit")) {
					serverService.disconnect(client_stub, client.getName());
					System.exit(0);
					return;
				}
				else if (userInput.equals("!history")) {
					System.out.println(serverService.getHistory());
				}
				
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				m = new Message(client.getId().toString(), client.getName(), userInput, LocalTime.now().format(timeFormatter));
				// Remote method invocation
				serverService.sendMessage(m);
			}

		} catch (Exception e) {
			System.err.println("Error on client: " + e);
		}
	}

}
