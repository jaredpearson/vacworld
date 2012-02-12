package jaredpearson;

import org.junit.Assert;
import org.junit.Test;

public class NodeTest {
	@Test
	public void testGetterSetter() {
		NodeMap nodeMap = new NodeMap();
		Node node = new Node(nodeMap, 0, 0);
		
		Assert.assertEquals(0, node.getX());
		Assert.assertEquals(0, node.getY());
		
		Assert.assertEquals(false, node.isDirty());
		node.setDirty(true);
		Assert.assertEquals(true, node.isDirty());
		
		Assert.assertEquals(false, node.isVisited());
		node.setVisited(true);
		Assert.assertEquals(true, node.isVisited());
	}
	
	@Test
	public void testNodeTypeProperties() {
		NodeMap nodeMap = new NodeMap();
		Node node = new Node(nodeMap, 0, 0);
		
		//the default type is UNKNOWN
		Assert.assertEquals(NodeType.UNKNOWN, node.getType());
		Assert.assertEquals(true, node.isUnknown());
		Assert.assertEquals(false, node.isOpen());
		Assert.assertEquals(false, node.isSolid());
		
		//set to OPEN
		node.setType(NodeType.OPEN);
		Assert.assertEquals(NodeType.OPEN, node.getType());
		Assert.assertEquals(false, node.isUnknown());
		Assert.assertEquals(true, node.isOpen());
		Assert.assertEquals(false, node.isSolid());
		
		//set to SOLID
		node.setType(NodeType.SOLID);
		Assert.assertEquals(NodeType.SOLID, node.getType());
		Assert.assertEquals(false, node.isUnknown());
		Assert.assertEquals(false, node. isUnknown());
		Assert.assertEquals(true, node.isSolid());
	}
	
	@Test
	public void testDirectionalPropertiesAreSameAsDirection() {
		NodeMap nodeMap = new NodeMap();
		Node node = new Node(nodeMap, 0, 0);

		Node northNode = node.getNorthNode();
		Assert.assertNotNull(northNode);
		Assert.assertEquals(northNode, node.getNode(Direction.NORTH));

		Node southNode = node.getSouthNode();
		Assert.assertNotNull(southNode);
		Assert.assertEquals(southNode, node.getNode(Direction.SOUTH));
		
		Node eastNode = node.getEastNode();
		Assert.assertNotNull(eastNode);
		Assert.assertEquals(eastNode, node.getNode(Direction.EAST));
		
		Node westNode = node.getWestNode();
		Assert.assertNotNull(westNode);
		Assert.assertEquals(westNode, node.getNode(Direction.WEST));
	}
	
	@Test
	public void testToStringIsNotNull() {
		NodeMap nodeMap = new NodeMap();
		Node node = new Node(nodeMap, 0, 0);
		Assert.assertNotNull(node.toString());
	}
}
