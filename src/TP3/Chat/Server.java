package Chat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UID;

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


	public void disconnect(Client client, String name) throws RemoteException {
		clients_id.remove(client);

		Message message = new SystemMessage(name + " a quitté le chat");
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


	public String getHistory() {
		String string_history = history.toString();
		System.out.println(string_history);
		return string_history;
	}
}
