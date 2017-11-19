package statusMonitor;

/**
 * Model class for a message
 * 
 * @author Pawel Dworzycki
 * @version 19/11/2017
 */
public class Message {
	private String message;
	private Long timeMessageGenerated;
	private Long timeMessageReceived;
	
	/**
	 * Create a new message
	 * 
	 * @param message the message
	 * @param timeMessageGenerated time at which it was generated at the node
	 * @param timeMessageReceived time at which it was received by the system
	 */
	public Message(String message, Long timeMessageGenerated, Long timeMessageReceived) {
		this.message = message;
		this.timeMessageGenerated = timeMessageGenerated;
		this.timeMessageReceived = timeMessageReceived;
	}
	
	/**
	 * @return the message string
	 */
	public String GetMessage() {
		return message;
	}
	
	/**
	 * @return the time at which the message was generated at the node
	 */
	public Long GetTimeMessageGenerated() {
		return timeMessageGenerated;
	}
	
	/**
	 * @return the time at which the message was received by the system
	 */
	public Long GetTimeMessageReceived() {
		return timeMessageReceived;
	}
}
