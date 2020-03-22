package Chat;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ClientMain {
	private static final String EXCHANGE_NAME = "chat_server_exchange";
    
	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {			
			ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("localhost");
	        Connection connection = factory.newConnection();
	        
	        Channel channel = connection.createChannel();
	        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        String queueName = channel.queueDeclare().getQueue();

	        // For the client to join the chat
	        channel.queueBind(queueName, EXCHANGE_NAME, "join");
	        // For the client to send a message
	        channel.queueBind(queueName, EXCHANGE_NAME, "sendMessage");

	        Client client = new Client();
			System.out.print("Veuillez entrer votre pseudo :\n");
            client.setName(scanner.nextLine());
			register(client, channel);
            
            System.out.println("Bienvenue. \nEntrez \"!exit\" pour quitter");
            System.out.println("Entrez \"!history\" pour avoir l'historique du chat");
			String userInput;
			Message message;
			while ((userInput = scanner.nextLine()) != null) {
				if (userInput.equals("!exit")) {
					break;
				}
				else if (userInput.equals("!history")) {
					channel.basicPublish(EXCHANGE_NAME, "askHistory", null, ByteSerializable.getBytes(client.getId()));
					continue;
				}
				
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				message = new BasicMessage(client.getId().toString(), client.getName(), userInput, LocalTime.now().format(timeFormatter));
				// Remote method invocation
				channel.basicPublish(EXCHANGE_NAME, "sendMessage", null, ByteSerializable.getBytes(message));
				System.out.print(String.format("\033[%dA",1)); // Move up
			}
			
			channel.basicPublish(EXCHANGE_NAME, "disconnect", null, ByteSerializable.getBytes(client));
			System.exit(1);
			return;

		} catch (Exception e) {
			System.err.println("Error on client: " + e);
		}
	}
	
	private static void register(Client client, Channel channel) {
		try {
			String queueNameReceiveMessage = channel.queueDeclare().getQueue();
			channel.queueBind(queueNameReceiveMessage, EXCHANGE_NAME, queueNameReceiveMessage);
			client.setId(queueNameReceiveMessage);
			
			// Callback and subscribe for when the client receives a message from the server
	        DeliverCallback deliverCallbackReceiveMessage = (consumerTag, delivery) -> {
	        	Message message = (Message) ByteSerializable.fromBytes(delivery.getBody());
				System.out.println(message);
	        };
	        channel.basicConsume(queueNameReceiveMessage, true, deliverCallbackReceiveMessage, consumerTag -> {});
	        
	        // Callback and subscribe for when the client receives the history
	        DeliverCallback deliverCallbackReceiveHistory = (consumerTag, delivery) -> {
	        	String history  = (String) ByteSerializable.fromBytes(delivery.getBody());
				System.out.println(history);
	        };
	        channel.basicConsume(queueNameReceiveMessage, true, deliverCallbackReceiveMessage, consumerTag -> {});
			
			channel.basicPublish(EXCHANGE_NAME, "join", null, ByteSerializable.getBytes(client));
		}
		catch (IOException e) {
			e.printStackTrace();
		}	
	}

}
