package jaredpearson;

import org.junit.Assert;
import org.junit.Test;

public class NodeMapTest {
	
	@Test
	public void testGetNodeReturnANewNode() {
		NodeMap map = new NodeMap();
		Node node = map.getNode(0, 0);
		Assert.assertNotNull("map.getNode should never return null", node);
	}
	
	@Test
	public void testGetNodeReturnExistingNode() {
		NodeMap map = new NodeMap();
		Node node1 = map.getNode(0, 0);
		Node node2 = map.getNode(0, 0);
		Assert.assertEquals("Calling map.getNode twice should return same node", node1, node2);
	}
}
