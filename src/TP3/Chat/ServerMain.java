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
	        Channel channel = connection.createChannel();
	        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        
	        
	        String queueNameJoin = channel.queueDeclare().getQueue(); // For the clients to join the chat
	        String queueNameSendMessage = channel.queueDeclare().getQueue(); // Receive message on the server <=> For the clients to send a message
	        String queueNameDisconnect = channel.queueDeclare().getQueue(); // Disconnect clients
	        String queueNameAskHistory = channel.queueDeclare().getQueue(); // For when the client asks for the history
	        channel.queueBind(queueNameJoin, EXCHANGE_NAME, "join"); 
	        channel.queueBind(queueNameSendMessage, EXCHANGE_NAME, "sendMessage");
	        channel.queueBind(queueNameDisconnect, EXCHANGE_NAME, "disconnect");
	        channel.queueBind(queueNameAskHistory, EXCHANGE_NAME, "askHistory");
	        
	        
	        // Receive message on the clients <=> For the clients to receive the messages
	        Channel channelReceiveMessage = connection.createChannel();
	        channelReceiveMessage.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        
			Server server = Server.getInstance(EXCHANGE_NAME, channel);
	        
			// Callback and subscribe for when a client wants to join the chat
			DeliverCallback deliverCallbackJoin = (consumerTag, delivery) -> {
				Client client = (Client) ByteSerializable.fromBytes(delivery.getBody());
			    server.register(client);
	        };
	        channel.basicConsume(queueNameJoin, true, deliverCallbackJoin, consumerTag -> {});
	        
	        // Callback and subscribe for when the client sends a message in the chat
	        DeliverCallback deliverCallbackSendMessage = (consumerTag, delivery) -> {
	        	Message message = (Message) ByteSerializable.fromBytes(delivery.getBody());
				server.sendMessage(message);
	        };
	        channel.basicConsume(queueNameSendMessage, true, deliverCallbackSendMessage, consumerTag -> {});

	        // Callback and subscribe for when the client disconnects
	        DeliverCallback deliverCallbackDisconnect = (consumerTag, delivery) -> {
				Client client = (Client) ByteSerializable.fromBytes(delivery.getBody());
	        	server.disconnect(client);
	        };
	        channel.basicConsume(queueNameDisconnect, true, deliverCallbackDisconnect, consumerTag -> {});
	        

	        // Callback and subscribe for when the client asks for the history
	        DeliverCallback deliverCallbackAskHistory = (consumerTag, delivery) -> {
	        	String client_id = (String) ByteSerializable.fromBytes(delivery.getBody());
				server.sendHistoryTo(client_id);
	        };
	        channel.basicConsume(queueNameAskHistory, true, deliverCallbackAskHistory, consumerTag -> {});
			
			System.out.println("Server ready");

		} catch (Exception e) {
			System.err.println("Error on server :" + e);
			e.printStackTrace();
		}
	}
}
