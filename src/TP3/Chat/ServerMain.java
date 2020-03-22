package Chat;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class ServerMain {
    private static final String EXCHANGE_NAME = "chat_server_exchange";
    
	public static void main(String[] args) {
		try {
			
			ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("localhost");
	        Connection connection = factory.newConnection();
	        
	        // For the clients to join the chat
	        Channel channelJoin = connection.createChannel();
	        channelJoin.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        String queueNameJoin = channelJoin.queueDeclare().getQueue();
	        channelJoin.queueBind(queueNameJoin, EXCHANGE_NAME, "join");
	        
	        // Receive message on the server <=> For the clients to send a message
	        Channel channelSendMessage = connection.createChannel();
	        channelSendMessage.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        String queueNameSendMessage = channelSendMessage.queueDeclare().getQueue();
	        channelSendMessage.queueBind(queueNameSendMessage, EXCHANGE_NAME, "sendMessage");
	        
	        // Receive message on the clients <=> For the clients to receive the messages
	        Channel channelReceiveMessage = connection.createChannel();
	        channelReceiveMessage.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        
			Server server = Server.getInstance(EXCHANGE_NAME, channelReceiveMessage);
	        
			// Callback and subscribe for when a client wants to join the chat
			DeliverCallback deliverCallbackJoin = (consumerTag, delivery) -> {
				Client client = (Client) ByteSerializable.fromBytes(delivery.getBody());
			    server.register(client);
	        };
	        channelJoin.basicConsume(queueNameJoin, true, deliverCallbackJoin, consumerTag -> {});
	        
	        // Callback and subscribe for when the client sends a message in the chat
	        DeliverCallback deliverCallbackSendMessage = (consumerTag, delivery) -> {
	        	Message message = (Message) ByteSerializable.fromBytes(delivery.getBody());
				server.sendMessage(message);
	        };
	        channelJoin.basicConsume(queueNameSendMessage, true, deliverCallbackSendMessage, consumerTag -> {});
			
			System.out.println("Server ready");

		} catch (Exception e) {
			System.err.println("Error on server :" + e);
			e.printStackTrace();
		}
	}
}
