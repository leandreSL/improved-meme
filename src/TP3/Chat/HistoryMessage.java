package Chat;

public class HistoryMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5336461044485938528L;
	private String history;
	
	public HistoryMessage(String history) {
		this.history = history;
	}

	public String toString() {
		return this.history;
	}
}
