package statusMonitor;

/**
 * Model class for a node
 * 
 * @author Pawel Dworzycki
 * @version 19/11/2017
 */
public class Node {
	private String name;
	private Status status;
	private Message lastMessage;
	
	/**
	 * Create a new node
	 * 
	 * @param name name of the node
	 */
	public Node(String name) {
		this.name = name;
	}
	
	/**
	 * Set the node status to ALIVE
	 */
	public void SetToAlive() {
		status = Status.ALIVE;
	}
	
	/**
	 * Set the node status to DEAD
	 */
	public void SetToDead() {
		status = Status.DEAD;
	}
	
	/**
	 * Set the node status to UNKNOWN
	 */
	public void SetToUnknown() {
		status = Status.UNKNOWN;
	}
	
	/**
	 * @return the status of the node
	 */
	public Status GetStatus() {
		return status;
	}
	
	/**
	 * Updates the message of the node
	 * 
	 * @param updateMessage the message
	 * @param timeMessageGenerated time at which it was generated at the node
	 * @param timeMessageReceived time at which it was received by the system
	 */
	public void UpdateMessage(String updateMessage, Long timeMessageGenerated, Long timeMessageReceived) {
		lastMessage = new Message(updateMessage, timeMessageGenerated, timeMessageReceived);		
	}
	
	/**
	 * @return a report about the node including its name, status and last message details
	 */
	public String Report() {
		StringBuilder sb = new StringBuilder();
		sb.append(name + " " + status + " " + lastMessage.GetTimeMessageReceived() + " " + lastMessage.GetMessage());
		return sb.toString();
	}
	
	/**
	 * @return the time which the message was received by the system
	 */
	public Long GetTimeMessageReceived() {
		if (lastMessage == null) {
			return null;
		}
		else {
			return lastMessage.GetTimeMessageReceived();
		}
	}
	
	/**
	 * @return the time which the message was generated at the node
	 */
	public Long GetTimeMessageGenerated() {
		if (lastMessage == null) {
			return null;
		}
		else {
			return lastMessage.GetTimeMessageGenerated();
		}
	}
	
}
