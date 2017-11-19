package statusMonitor;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

class NotificationParserTest {

	@Test
	void testProcessingHelloMessage() throws Exception {
		NotificationParser np = new NotificationParser();
		
		// Test notification
		String notification = "1508405807242 1508405807141 vader HELLO";
		np.ProcessNotification(notification);
		
		// Set up node
		Node node = null;
		// Get the node
		for (Map.Entry<String, Node> nodeEntry : np.GetNodes().entrySet()) {
			node = nodeEntry.getValue();
		}
		String report = node.Report();
		
		assertEquals("vader ALIVE 1508405807242 vader HELLO", report);
	}

	@Test
	void testProcessingLostMessage() throws Exception {
		NotificationParser np = new NotificationParser();
		
		// Test notification
		String notification = "1508405807378 1508405807387 luke LOST vader";
		np.ProcessNotification(notification);
		
		// Set up node
		Node node = null;
		// Get the node
		for (Map.Entry<String, Node> nodeEntry : np.GetNodes().entrySet()) {
			node = nodeEntry.getValue();
		}
		String report = node.Report();
		
		assertEquals("vader DEAD 1508405807378 luke LOST vader", report);
	}
	
	@Test
	void testProcessingFoundMessage() throws Exception {
		NotificationParser np = new NotificationParser();
		
		// Test notification
		String notification = "1508405807467 1508405807479 luke FOUND r2d2";
		np.ProcessNotification(notification);
		
		// Set up node
		Node node = null;
		// Get the node
		for (Map.Entry<String, Node> nodeEntry : np.GetNodes().entrySet()) {
			node = nodeEntry.getValue();
		}
		String report = node.Report();
		
		assertEquals("r2d2 ALIVE 1508405807467 luke FOUND r2d2", report);
	}
	
	@Test
	void testProcessingMultipleMessagesForOneNode() throws Exception {
		NotificationParser np = new NotificationParser();
		
		// Test notification
		String notification = "1508405807242 1508405807141 vader HELLO";
		np.ProcessNotification(notification);
		notification = "1508405807378 1508405807387 luke LOST vader";
		np.ProcessNotification(notification);
		notification = "1508405807560 1508405807504 vader HELLO";
		np.ProcessNotification(notification);
		
		// Set up node
		Node node = null;
		// Get the node
		for (Map.Entry<String, Node> nodeEntry : np.GetNodes().entrySet()) {
			node = nodeEntry.getValue();
		}
		String report = node.Report();
		
		assertEquals("vader ALIVE 1508405807560 vader HELLO", report);
	}
	
	@Test
	void testProcessingMultipleMessagesForOneNodeOutOfSync() throws Exception {
		NotificationParser np = new NotificationParser();
		
		// Test notification
		String notification = "1508405807242 1508405807141 vader HELLO";
		np.ProcessNotification(notification);
		notification = "1508405807378 1508405807387 luke LOST vader";
		np.ProcessNotification(notification);
		notification = "1508405807560 1508405807000 vader HELLO";
		np.ProcessNotification(notification);
		
		// Set up node
		Node node = null;
		// Get the node
		for (Map.Entry<String, Node> nodeEntry : np.GetNodes().entrySet()) {
			node = nodeEntry.getValue();
		}
		String report = node.Report();
		
		assertEquals("vader DEAD 1508405807378 luke LOST vader", report);
	}
	
	@Test
	void testProcessingAmbiguousMultipleMessagesForOneNode() throws Exception {
		NotificationParser np = new NotificationParser();
		
		// Test notification
		String notification = "1508405807400 1508405807480 luke LOST vader";
		np.ProcessNotification(notification);
		notification = "1508405807510 1508405807470 vader HELLO";
		np.ProcessNotification(notification);
		
		// Set up node
		Node node = null;
		// Get the node
		for (Map.Entry<String, Node> nodeEntry : np.GetNodes().entrySet()) {
			node = nodeEntry.getValue();
		}
		String report = node.Report();
		
		assertEquals("vader UNKNOWN 1508405807510 vader HELLO", report);
	}

}
