package jaredpearson;

public class Node
{
	private NodeMap nodeMap;
	private int x = 0;
	private int y = 0;
	private boolean dirty = false;
	private boolean visited = false;
	private NodeType type = NodeType.UNKNOWN;
	private Node northNode;
	private Node southNode;
	private Node westNode;
	private Node eastNode;
	
	public Node(NodeMap nodeMap, int x, int y) {
		this.nodeMap = nodeMap;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	
	public boolean isOpen() {
		return this.getType() == NodeType.OPEN;
	}
	
	public boolean isUnknown() {
		return this.getType() == NodeType.UNKNOWN;
	}

	public boolean isSolid() {
		return this.getType() == NodeType.SOLID;
	}
	
	public Node getNorthNode() {
		if(northNode == null) {
			northNode = this.nodeMap.getNode(this.x, this.y + 1);
			northNode.southNode = this;
		}
		return northNode;
	}
	
	public Node getSouthNode() {
		if(southNode == null) {
			southNode = this.nodeMap.getNode(this.x, this.y - 1);
			southNode.northNode = this;
		}
		return southNode;
	}
	
	public Node getWestNode() {
		if(westNode == null) {
			westNode = this.nodeMap.getNode(this.x - 1, this.y);
			westNode.eastNode = this;
		}
		return westNode;
	}
	
	public Node getEastNode() {
		if(eastNode == null) {
			eastNode = this.nodeMap.getNode(this.x + 1, this.y);
			eastNode.westNode = this;
		}
		return eastNode;
	}
	
	public Node getNode(Direction direction) {
		switch (direction) {
		case NORTH:
			return this.getNorthNode();
		case SOUTH:
			return this.getSouthNode();
		case EAST:
			return this.getEastNode();
		case WEST:
			return this.getWestNode();
		}
		throw new IllegalStateException("Unknown direction " + direction);
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Node [x=" + x + ", y=" + y + "]";
	}
}