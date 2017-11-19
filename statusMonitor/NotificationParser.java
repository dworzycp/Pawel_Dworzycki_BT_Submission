package statusMonitor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Models the behaviour for a notification parser which reads a text file
 * and based on the lines in the file it updates the statuses of nodes
 * 
 * @author Pawel Dworzycki
 * @version 19/11/2017
 */
public class NotificationParser {
	private Map<String, Node> nodes;
	
	/**
	 * Constructor
	 */
	public NotificationParser() {
		nodes = new HashMap<String, Node>();
	}
	
	/**
	 * Main class
	 * 
	 * @param args the text file with the notifications
	 */
	public static void main(String[] args) {
		NotificationParser np = new NotificationParser();
		
		try {
			np.ReadNotifications(args[0]);
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("The file which you are trying to read does not exist.");
		}
		
		np.GenerateReport();
	}

	/**
	 * Reads the passed text file line by line and invokes a method to process the notifications
	 * 
	 * @param file the text file with the notifications
	 * @throws Exception
	 */
	public void ReadNotifications(String file) throws Exception {
		FileReader input = new FileReader(file);
		BufferedReader reader = new BufferedReader(input);
		String notification = null;
		
		// Process each line until the reader reaches end of the file
		while ( (notification = reader.readLine()) != null)
		{    
			ProcessNotification(notification);
		}
		
		reader.close();
	}
	
	/**
	 * Processes the notification of format [received time] [generated time] [node] [message] [node?]
	 * 
	 * @param notification status notification
	 * @throws Exception
	 */
	public void ProcessNotification(String notification) throws Exception {	
		// Split notification string
		String[] notificationArray = notification.split("\\s+");
		
		// Check there are at least 4 values
		if (notificationArray.length < 4) {
			throw new Exception("The received message contains too few values");
		}
		// Check there are no more than 5 values
		if (notificationArray.length > 5) {
			throw new Exception("The received message contains too many values");
		}
		// Check that both of the times are actually numbers
		// RegEx suggested by Karthink T at https://stackoverflow.com/questions/14206768/how-to-check-if-a-string-is-numeric
		String numRegEx = "[-+]?\\d*\\.?\\d+";
		if (!notificationArray[0].matches(numRegEx) || !notificationArray[1].matches(numRegEx)) {
			throw new Exception("At least one of the times is not a valid number");
		}
		
		// Read the notification type (index 3)
		if (notificationArray[3].equals("HELLO")) {
			// Node at index 2 says hello, i.e. ALIVE
			UpdateNode(notificationArray, 2, Status.ALIVE);
		}
		else if (notificationArray[3].equals("LOST")) {
			// Node at index 4 was lost i.e. DEAD
			UpdateNode(notificationArray, 4, Status.DEAD);
			// Node at index 2 must be alive to have LOST a node
			UpdateNode(notificationArray, 2, Status.ALIVE);
		}
		else if (notificationArray[3].equals("FOUND")) {
			// Node at index 4 was found, i.e. ALIVE
			UpdateNode(notificationArray, 4, Status.ALIVE);
			// Node at index 2 must be alive to have FOUND a node
			UpdateNode(notificationArray, 2, Status.ALIVE);
		}
		else {
			System.out.println("ERR: Notification type " + notificationArray[3] + " is not valid");
		}
		
	}
	
	/**
	 * Updated the node's status to ALIVE and also sets the latest message
	 * 
	 * @param nodeName name of the node
	 * @param updateMessage update message as a string
	 * @param timeOfMessageCreation time at which the message was generated at the node
	 * @param timeMessageReceived time at which the message was received by the system
	 */
	public void UpdateNodeToAlive(String nodeName, String updateMessage, String timeOfMessageCreation, String timeMessageReceived) {
		Node node = GetNodeObject(nodeName);
		node.SetToAlive();
		node.UpdateMessage(updateMessage, Long.parseLong(timeOfMessageCreation), Long.parseLong(timeMessageReceived));
	}
	
	/**
	 * Updated the node's status to DEAD and also sets the latest message
	 * 
	 * @param nodeName name of the node
	 * @param updateMessage update message as a string
	 * @param timeOfMessageCreation time at which the message was generated at the node
	 * @param timeMessageReceived time at which the message was received by the system
	 */
	public void UpdateNodeToDead(String nodeName, String updateMessage, String timeOfMessageCreation, String timeMessageReceived) {
		Node node = GetNodeObject(nodeName);
		node.SetToDead();
		node.UpdateMessage(updateMessage, Long.parseLong(timeOfMessageCreation), Long.parseLong(timeMessageReceived));
	}
	
	/**
	 * Updated the node's status to UNKNOWN and also sets the latest message
	 * 
	 * @param nodeName name of the node
	 * @param updateMessage update message as a string
	 * @param timeOfMessageCreation time at which the message was generated at the node
	 * @param timeMessageReceived time at which the message was received by the system
	 */
	public void UpdateNodeToUnknown(String nodeName, String updateMessage, String timeOfMessageCreation, String timeMessageReceived) {
		Node node = GetNodeObject(nodeName);
		node.SetToUnknown();
		node.UpdateMessage(updateMessage, Long.parseLong(timeOfMessageCreation), Long.parseLong(timeMessageReceived));
	}
	
	/**
	 * Creates the update message and sets the status for a node
	 * 
	 * @param notification the split status notification
	 * @param indexOfAffectedNode index of the node to update
	 * @param expectedStatus expected status of the node based on the message type
	 */
	public void UpdateNode(String[] notification, int indexOfAffectedNode, Status expectedStatus) {
		StringBuilder sb = new StringBuilder();
		Status status = null;
		
		// Determine the status of the node
		if (CheckMessageResultIsAmbiguous(notification[indexOfAffectedNode], notification)) {
			status = Status.UNKNOWN;
		}
		else {
			// Check if the message is the latest
			if (CheckMessageIsLatest(notification[indexOfAffectedNode], notification[1])) {				
				if (expectedStatus == Status.ALIVE) {
					status = Status.ALIVE;
				}
				else {
					status = Status.DEAD;
				}
				
			}
		}
		
		// Check that the status is not null
		// It can be null if the message is out of sync and systems wants to ignore it
		if (status != null) {
			// Create a message from the notification
			if (notification.length == 4) {
				// Example: vader HELLO
				sb.append(notification[2] + " " + notification[3]);
			}
			else {
				// Example: luke LOST leia
				sb.append(notification[2] + " " + notification[3] + " " + notification[4]);	
			}
			
			// Update node to the correct status
			if (status.equals(Status.ALIVE)) {
				UpdateNodeToAlive(notification[indexOfAffectedNode], sb.toString(), notification[1], notification[0]);
			}
			else if (status.equals(Status.DEAD)) {
				UpdateNodeToDead(notification[indexOfAffectedNode], sb.toString(), notification[1], notification[0]);
			}
			else {
				UpdateNodeToUnknown(notification[indexOfAffectedNode], sb.toString(), notification[1], notification[0]);
			}
		}
	}
	
	/**
	 * Tries to return a Node object for a node of a given name
	 * If that object doesn't already exist, it creates then returns it
	 * 
	 * @param nodeName name of the node
	 * @return the Node object
	 */
	public Node GetNodeObject(String nodeName) {
		// Check if the node already exists
		if (nodes.containsKey(nodeName)) {
			// Node exists, return it
			return nodes.get(nodeName);
		}
		else {
			// Node doesn't exist, create it then return it
			Node node = new Node(nodeName);
			nodes.put(nodeName, node);
			return node;
		}
	}
	
	/**
	 * Prints out a report about the status of all of the nodes
	 */
	public void GenerateReport() {
		for (Map.Entry<String, Node> nodeEntry : nodes.entrySet()) {
			Node node = nodeEntry.getValue();
			System.out.println(node.Report());
		}
	}
	
	/**
	 * Check if the message is the latest message for that node
	 * Use to see if the message was received out of sync
	 * 
	 * @param node Node
	 * @param timeOfMessageCreation time at which the message was created at the node
	 * @return True if the message is the latest, False if not
	 */
	private boolean CheckMessageIsLatest(Node node, String timeOfMessageCreation) {	
		// Check if this is the first message
		if (node.GetTimeMessageGenerated() == null) {
			return true;
		}
		// Check if the previous message was generated before this one
		else if (Long.parseLong(timeOfMessageCreation) > node.GetTimeMessageGenerated()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private boolean CheckMessageIsLatest(String nodeName, String timeOfUpdate) {	
		Node node = GetNodeObject(nodeName);
		return CheckMessageIsLatest(node, timeOfUpdate);
	}
	
	/**
	 * Check if the result of processing of this message be ambiguous
	 * 
	 * @param nodeName name of the node
	 * @param timeOfMessageCreation time at which the message was created at the node
	 * @return True if the result of processing this notification will be ambiguous, False if not
	 */
	private boolean CheckMessageResultIsAmbiguous(String nodeName, String[] notification) {
		// Check if the result of processing of this message be ambiguous
		// We know that the nodes are synced to <50ms of each other 
		// but are NOT synced with the monitoring system
		
		// Example (messages are in received order)
		// 1508405807400 1508405807480 luke LOST vader
		// 1508405807510 1508405807470 vader HELLO
		// With the above example the system can't be sure if vader is DEAD or ALIVE
		// because the time difference between luke and vader is <50ms
		// AND the messages were received out of sync
		// This means that vader's status is not certain i.e. UNKNOWN
		
		Node node = GetNodeObject(nodeName);
		Long creationTime = Long.parseLong(notification[1]);
		Long syncTime = (long) 50;
		
		// Check if the message is latest
		if (CheckMessageIsLatest(node, notification[1]) == true) {
			return false;
		}
		else {
			// The message is out of sync
			// Check if the time difference between two messages' generation time is < syncTime
			if (Math.abs(node.GetTimeMessageGenerated() - creationTime) < syncTime) {
				// Print out a new line with the details of this
				System.out.println("UNKNOWN - notification received from node " + notification[2] + " at " + notification[0]);
				// return
				return true;
			}
			else {
				// Message is fine, no problems with the sequence
				return false;
			}
		}
		
	}
	
	/**
	 * @return returns the Map of the nodes
	 */
	public Map<String, Node> GetNodes() {
		return nodes;
	}
	
}
