package Chat;

public class SystemMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5292951775412080216L;
	private String message;
	
	public SystemMessage (String message) {
		this.message = message;
	}
	
	public String toString () {
		return this.message;
	}

}
