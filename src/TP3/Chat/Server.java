package Chat;

import java.util.ArrayList;
import java.util.List;
import com.rabbitmq.client.Channel;
import java.io.IOException;

public class Server {    
	private static Server instance;
    private List<String> clients_id;
	private History history;
	private final String EXCHANGE_NAME;
	private Channel channel;
	
	private Server(String EXCHANGE_NAME, Channel channel) {
		this.EXCHANGE_NAME = EXCHANGE_NAME;
		this.clients_id = new ArrayList<String>();
		this.history = new History();
		this.channel = channel;
	}
	
	public static Server getInstance (String EXCHANGE_NAME, Channel channel) {
		if (instance == null) instance = new Server(EXCHANGE_NAME, channel);
		return instance;
	}

	public void register (Client client) {
        try {
            String queueNameReceiveMessage = this.channel.queueDeclare().getQueue();
			this.channel.queueBind(queueNameReceiveMessage, EXCHANGE_NAME, "receiveMessage");
		}
        catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
        Message message = new SystemMessage(client.getName() + " a rejoint le chat");
		history.addMessage(message);
		System.out.println(message);
		this.broadcast(message);
		
		clients_id.add(client.getId());
	}
	
	public void sendMessage (Message message) {
		history.addMessage(message);
		System.out.println(message);
		this.broadcast(message);
	}


	public void disconnect(Client client) {
		clients_id.remove(client.getId());

		Message message = new SystemMessage(client.getName() + " a quitté le chat");
		history.addMessage(message);
		System.out.println(message);
		this.broadcast(message);
	}

	private void broadcast(Message message) {
		byte[] msg = ByteSerializable.getBytes(message);
		
		for (String clientReceiveQueueName: this.clients_id) {
			try {
				channel.basicPublish(this.EXCHANGE_NAME, clientReceiveQueueName, null, msg);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public void sendHistoryTo (String client_id) {
		String string_history = history.toString();
		byte[] message = ByteSerializable.getBytes(new HistoryMessage(string_history));
		
		try {
			channel.basicPublish(this.EXCHANGE_NAME, client_id, null, message);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
