package Chat;

import java.io.Serializable;

public class Client implements Serializable {
	private String id;
	private String name;
	
	public Client () {
		this.id = null;
	}
	
	public String getId () {
		return this.id;
	}

	public void setId(String queueNameReceiveMessage) {
		this.id = queueNameReceiveMessage;
	}
	
	public String getName () {
		return this.name;
	}

	public void setName (String name) {
		this.name = name;
	}

}