package Chat;

public class SystemMessage implements Message {
	private String message;
	
	public SystemMessage (String message) {
		this.message = message;
	}
	
	public String toString () {
		return this.message;
	}

}
