package Chat;

import java.io.Serializable;
import java.rmi.server.UID;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6970524661461216310L;
	private UID id;
	private String name;
	private String message;
	private String time;
	
	public Message(UID id, String name, String message, String time) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.time = time;
	}

	public String toString() {
		return name + " - " + time  + ": " + message;
	}
	
	public UID getId () {
		return this.id;
	}
}
