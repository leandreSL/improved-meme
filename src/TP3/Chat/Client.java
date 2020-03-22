package Chat;

import java.io.Serializable;

public class Client implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1333817325751769421L;
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