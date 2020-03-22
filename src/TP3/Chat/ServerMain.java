package Chat;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ServerMain {
    private static final String EXCHANGE_NAME = "chat_server_exchange";
    
	public static void main(String[] args) {
		try {
			ConnectionFactory factory = new ConnectionFactory();
	        factory.setHost("localhost");
	        Connection connection = factory.newConnection();
	        
	        Channel channelJoin = connection.createChannel();
	        channelJoin.exchangeDeclare(EXCHANGE_NAME, "fanout");
	        String queueNameJoin = channelJoin.queueDeclare().getQueue();
	        channelJoin.queueBind(queueNameJoin, EXCHANGE_NAME, "");
	        
	        Channel channelSendMessage = connection.createChannel();
	        channelSendMessage.exchangeDeclare(EXCHANGE_NAME, "fanout");
	        String queueNameSendMessage = channelSendMessage.queueDeclare().getQueue();
	        channelSendMessage.queueBind(queueNameSendMessage, EXCHANGE_NAME, "");
	        			
			System.out.println("Server ready");

		} catch (Exception e) {
			System.err.println("Error on server :" + e);
			e.printStackTrace();
		}
	}
}
