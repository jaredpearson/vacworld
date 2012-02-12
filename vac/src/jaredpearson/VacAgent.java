package jaredpearson;

import java.util.ArrayList;
import java.util.List;

import vacworld.GoForward;
import vacworld.ShutOff;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;
import vacworld.VacPercept;
import agent.Action;
import agent.Agent;
import agent.Percept;

public class VacAgent 
	extends Agent{
	private static final Log log = LoggerFactory.getLogger(VacAgent.class);
	private NodeMap nodeMap = new NodeMap();
	private VacPercept currentPercept;
	private Node currentNode;
	private Direction facing;
	
	@Override
	public void see(Percept p) {
		this.currentPercept = (VacPercept)p;
	}

	@Override
	public Action selectAction() {
		
		//if this is the first node, then we create an open node at 0,0
		if(currentNode == null) {
			currentNode = nodeMap.getNode(0, 0);
			currentNode.setType(NodeType.OPEN);
			currentNode.setVisited(true);
			facing = Direction.NORTH;
		}
		
		//check to see if node in front is open
		Node facingNode = currentNode.getNode(facing);
		if(currentPercept.seeObstacle()) {
			log.debug("detected solid in front");
			facingNode.setType(NodeType.SOLID);
		} else {
			log.debug("detected open in front");
			facingNode.setType(NodeType.OPEN);
		}
		
		//check for dirt
		if(currentPercept.seeDirt()) {
			currentNode.setDirty(true);
		}
		
		Action action = determineAction(currentNode);
		
		//update the robot and node state after the action
		if(action instanceof SuckDirt) {
			currentNode.setDirty(false);
		} else if(action instanceof GoForward) {
			currentNode.setVisited(true);
			currentNode = currentNode.getNode(facing);
			
			if(!currentNode.isOpen()) {
				throw new IllegalStateException("Trying to move into a non-open spot!");
			}
			
		} else if(action instanceof TurnLeft) {
			facing = facing.rotateLeft();
		} else if(action instanceof TurnRight) {
			facing = facing.rotateRight();
		}
		
		return action;
	}
	
	private Action determineAction(Node currentNode) {
		if(nodeMap.isComplete()) {
			return new ShutOff();
		}
		
		//give priority to suck dirt
		if(currentNode.isDirty()) {
			return new SuckDirt();
		}
		
		//try to move forward
		Node forwardNode = currentNode.getNode(facing);
		if(forwardNode.isOpen()  && !forwardNode.isVisited()) {
			log.debug("Moving forward because it can be moved into and is not visited");
			return new GoForward();
		} 
		
		//determine the next square to move to
		RelativeDirection turnDirection = nextDirection(currentNode);
		if(turnDirection != null){
			if(turnDirection == RelativeDirection.LEFT) {
				return new TurnLeft();
			} 
			if(turnDirection == RelativeDirection.RIGHT) {
				return new TurnRight();
			}
		}
		
		//find the closest unvisited node
		Node node = findClosestUnvisitedNode(currentNode);
		if(node != null) {
			log.debug("(" + currentNode.getX() + "," + currentNode.getY() + ") -> (" + node.getX() + "," + node.getY() + ")");
			
			//determine the direction to turn based upon the coordinates of the closest
			Direction turnToDirection = null;
			if(node.getY() > currentNode.getY()) { //node is to the north
				turnToDirection = Direction.NORTH;
			} else if(node.getX() > currentNode.getX()) { //node is to east
				turnToDirection = Direction.EAST;
			} else if(node.getX() < currentNode.getX()) {
				turnToDirection = Direction.WEST;
			}
			
			//based upon the turn direction, determine the way to turn
			if(facing == turnToDirection) {
				log.debug("Closest is to the " + turnToDirection + " and facing " + facing + " so go forward");
				return new GoForward();
			} else if(turnToDirection == Direction.NORTH) {
				if (facing == Direction.WEST) {
					return new TurnRight();
				} else if (facing == Direction.EAST) {
					return new TurnLeft();
				}
			} else if(turnToDirection == Direction.EAST) {
				if(facing == Direction.NORTH) {
					return new TurnRight();
				} else if(facing == Direction.SOUTH) {
					return new TurnLeft();
				}
			} else if(turnToDirection == Direction.WEST) {
				if(facing == Direction.NORTH) {
					return new TurnLeft();
				} else if(facing == Direction.SOUTH) {
					return new TurnRight();
				}
			}
		}
		
		log.debug("Can't determine so just turning left");
		return new TurnLeft();
	}
	
	private RelativeDirection nextDirection(Node currentNode) {
		//check to see if we know what is to the left
		Direction leftDirection = facing.rotateLeft();
		Node leftNode = currentNode.getNode(leftDirection);
		if(leftNode.isUnknown()) {
			log.debug("Turning to left because it is unknown");
			return RelativeDirection.LEFT;
		}
		
		//check to see if we know what is to the right
		Direction rightDirection = facing.rotateRight();
		Node rightNode = currentNode.getNode(rightDirection);
		if(rightNode.isUnknown()) {
			log.debug("Turning to right because it is unknown");
			return RelativeDirection.RIGHT;
		}
		
		return null;
	}
	
	private Node findClosestUnvisitedNode(Node currentNode) {
		List<WeightedNode> possibleNodes = new ArrayList<WeightedNode>();
		
		WeightedNode forwardNode = findClosestUnvisitedNode(currentNode, facing);
		if(forwardNode != null) {
			possibleNodes.add(forwardNode);
		}
		
		WeightedNode leftNode = findClosestUnvisitedNode(currentNode, facing.rotateLeft());
		if(leftNode != null) {
			possibleNodes.add(leftNode);
		}
		
		WeightedNode rightNode = findClosestUnvisitedNode(currentNode, facing.rotateRight());
		if(rightNode != null) {
			possibleNodes.add(rightNode);
		}
		
		WeightedNode closest = null;
		for(WeightedNode weightedNode : possibleNodes) {
			if(closest == null || weightedNode.weight < closest.weight) {
				closest = weightedNode;
			}
		}
		
		if(closest == null) {
			return null;
		} else {
			log.debug("Determined " + closest.node + " is the closest");
			return closest.node;
		}
	}
	
	private WeightedNode findClosestUnvisitedNode(Node currentNode, Direction facing) {
		int weight = 1;
		Node lookAtNode = currentNode.getNode(facing);
		for(int distance = 0; ; distance++) {
			
			if(lookAtNode.isSolid()) {
				return null;
			} else if(lookAtNode.isUnknown() || !lookAtNode.isVisited()) {
				WeightedNode wNode = new WeightedNode();
				wNode.weight = weight + distance;
				wNode.node = lookAtNode;
				return wNode;
			} else {
				lookAtNode = lookAtNode.getNode(facing);
			}
		}
	}
	
	private static class WeightedNode {
		public int weight;
		public Node node;
	}
	
	@Override
	public String getId() {
		return "jpearson" + Math.random();
	}
}
