package Chat;

import java.rmi.server.UID;

public class Client {
	private UID id;
	private String name;
	
	public Client () {
		this.id = new UID();
	}
	
	public UID getId () {
		return this.id;
	}
	
	public String getName () {
		return this.name;
	}

	public void setName (String name) {
		this.name = name;
	}

}