package Chat;

public class BasicMessage implements Message {
	private String id;
	private String name;
	private String message;
	private String time;
	
	public BasicMessage(String id, String name, String message, String time) {
		this.id = id;
		this.name = name;
		this.message = message;
		this.time = time;
	}

	public String toString() {
		return time + " - " + name + ": " + message;
	}
	
	public String getId () {
		return this.id;
	}
}
