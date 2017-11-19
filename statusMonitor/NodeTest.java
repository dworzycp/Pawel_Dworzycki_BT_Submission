package statusMonitor;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class NodeTest {

	@Test
	void testSetToAliveGetStatus() {
		Node n = new Node("luke");
		n.SetToAlive();
		assertEquals(Status.ALIVE, n.GetStatus());
	}

	@Test
	void testSetToDeadGetStatus() {
		Node n = new Node("luke");
		n.SetToDead();
		assertEquals(Status.DEAD, n.GetStatus());
	}
	
	@Test
	void testSetToUnknownGetStatus() {
		Node n = new Node("luke");
		n.SetToUnknown();
		assertEquals(Status.UNKNOWN, n.GetStatus());
	}
	
	@Test
	void testReport() {
		Node n = new Node("luke");
		n.SetToAlive();
		
		String msgText = "luke HELLO";
		Long genTime = Long.parseLong("1508405807350");
		Long recTime = Long.parseLong("1508405807340");
		n.UpdateMessage(msgText, genTime, recTime);
		
		assertEquals("luke ALIVE 1508405807340 luke HELLO", n.Report());
	}
	
	@Test
	void testGetTimeMessageReceived() {
		Node n = new Node("luke");
		
		String msgText = "luke HELLO";
		Long genTime = Long.parseLong("1508405807350");
		Long recTime = Long.parseLong("1508405807340");
		n.UpdateMessage(msgText, genTime, recTime);
		
		assertEquals(recTime, n.GetTimeMessageReceived());
	}
	
	@Test
	void testGetTimeMessageGenerated() {
		Node n = new Node("luke");
		
		String msgText = "luke HELLO";
		Long genTime = Long.parseLong("1508405807350");
		Long recTime = Long.parseLong("1508405807340");
		n.UpdateMessage(msgText, genTime, recTime);
		
		assertEquals(genTime, n.GetTimeMessageGenerated());
	}
	
}
