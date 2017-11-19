package statusMonitor;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

class MessageTest {
	
	@Test
	void testGetMessage() {
		String msgText = "vader HELLO";
		Long genTime = Long.parseLong("1508405807504");
		Long recTime = Long.parseLong("1508405807560");
		
		Message msg = new Message(msgText, (long) genTime, recTime);
		
		assertEquals(msgText, msg.GetMessage());
	}

	@Test
	void testGetTimeMessageGenerated() {
		String msgText = "vader HELLO";
		Long genTime = Long.parseLong("1508405807504");
		Long recTime = Long.parseLong("1508405807560");
		
		Message msg = new Message(msgText, (long) genTime, recTime);
		
		assertEquals(genTime, msg.GetTimeMessageGenerated());
	}
	
	@Test
	void testGetTimeMessageReceived() {
		String msgText = "vader HELLO";
		Long genTime = Long.parseLong("1508405807504");
		Long recTime = Long.parseLong("1508405807560");
		
		Message msg = new Message(msgText, (long) genTime, recTime);
		
		assertEquals(recTime, msg.GetTimeMessageReceived());
	}
	
}
