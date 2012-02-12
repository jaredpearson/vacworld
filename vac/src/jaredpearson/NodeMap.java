package jaredpearson;

import java.util.HashMap;
import java.util.Map;

public class NodeMap {
	private static final Log log = LoggerFactory.getLogger(NodeMap.class);
	private HashMap<Integer, Map<Integer, Node>> nodeMap = new HashMap<Integer, Map<Integer, Node>>();
	
	public Node getNode(int x, int y) {
		return getNode(x, y, true);
	}
	
	private Node getNode(int x, int y, boolean create) {
		Map<Integer, Node> yMap = null;
		if(nodeMap.containsKey(x)) {
			yMap = nodeMap.get(x);
		} else {
			yMap = new HashMap<Integer, Node>();
			nodeMap.put(x, yMap);
		}
		
		if(yMap.containsKey(y)) {
			return yMap.get(y);
		} else if(create) {
			log.debug("Creating new node(" + x + ", " + y + ")");
			Node newNode = new Node(this, x, y);
			yMap.put(y, newNode);
			return newNode;
		} else {
			return null;
		}
	}
	
	public boolean isComplete() {
		//TODO: write code to determine when the map is completely visited
		return false;
	}
}
