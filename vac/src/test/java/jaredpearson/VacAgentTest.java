package jaredpearson;

import org.junit.Assert;
import org.junit.Test;

import agent.Action;
import agent.Agent;
import agent.Percept;
import vacworld.GoForward;
import vacworld.SuckDirt;
import vacworld.TurnLeft;
import vacworld.TurnRight;
import vacworld.VacPercept;
import vacworld.VacuumState;

public class VacAgentTest {
	@Test
	public void testSuckWhenDirty() {
		VacAgent agent = new VacAgent();
		Percept percept = new PerceptBuilder()
			.withAgent(agent)
			.withDirtAt(3, 3)
			.build();
		agent.see(percept);
		Action action = agent.selectAction();
		Assert.assertEquals("Expected action SuckDirt when the over dirty node", SuckDirt.class, action.getClass());
	}
	
	@Test
	public void testGoForwardWhenOpenForward() {
		VacAgent agent = new VacAgent();
		Percept percept = new PerceptBuilder()
			.withAgent(agent)
			.build();
		agent.see(percept);
		Action action = agent.selectAction();
		Assert.assertEquals("Expected action GoForward when the forward node is open and unvisited", GoForward.class, action.getClass());
	}
	
	/**
	 * The agent should turn left when there is a visited node in front and right but unvisited node to the left
	 */
	@Test
	public void testSelectTurnActionWhenVisitedForwardAndLeftUnvisited() {
		
		//set up the agent's memory
		NodeMap nodeMap = new NodeMap();
		Node forwardNode = nodeMap.getNode(0, 1);
		forwardNode.setVisited(true);
		forwardNode.setType(NodeType.OPEN);
		Node leftNode = nodeMap.getNode(-1, 0);
		leftNode.setType(NodeType.UNKNOWN);
		leftNode.setVisited(false);
		Node rightNode = nodeMap.getNode(1, 0);
		rightNode.setType(NodeType.OPEN);
		rightNode.setVisited(true);
		
		//set up the world and agent
		VacAgent agent = new VacAgent(nodeMap);
		Percept percept = new PerceptBuilder()
			.withAgent(agent)
			.build();
		agent.see(percept);
		
		//ask agent for action
		Action action = agent.selectAction();
		Assert.assertEquals("Expected left turn action when the forward and left are visited", TurnLeft.class, action.getClass());
	}
	
	/**
	 * The agent should turn right when there is a visited node in front and to the left but the
	 * right is unvisited.
	 */
	@Test
	public void testSelectTurnActionWhenVisitedForwardAndRightUnvisited() {
		
		//set up the agent's memory
		NodeMap nodeMap = new NodeMap();
		Node forwardNode = nodeMap.getNode(0, 1);
		forwardNode.setVisited(true);
		forwardNode.setType(NodeType.OPEN);
		Node leftNode = nodeMap.getNode(-1, 0);
		leftNode.setType(NodeType.OPEN);
		leftNode.setVisited(true);
		Node rightNode = nodeMap.getNode(1, 0);
		rightNode.setType(NodeType.UNKNOWN);
		rightNode.setVisited(false);
		
		//set up the world and agent
		VacAgent agent = new VacAgent(nodeMap);
		Percept percept = new PerceptBuilder()
			.withAgent(agent)
			.build();
		agent.see(percept);
		
		//ask agent for action
		Action action = agent.selectAction();
		Assert.assertEquals("Expected right turn action when forward and left are visited", TurnRight.class, action.getClass());
	}
	
	/**
	 * When forward, left, and right are visited and the forward+1 is unvisited, the agent should select to move forward
	 */
	@Test
	public void testSelectClosestActionWhenVisitedAllAround() {
		//set up the agent's memory
		NodeMap nodeMap = new NodeMap();
		Node forwardNode = nodeMap.getNode(0, 1);
		forwardNode.setVisited(true);
		forwardNode.setType(NodeType.OPEN);
		Node leftNode = nodeMap.getNode(-1, 0);
		leftNode.setType(NodeType.OPEN);
		leftNode.setVisited(true);
		Node rightNode = nodeMap.getNode(1, 0);
		rightNode.setType(NodeType.OPEN);
		rightNode.setVisited(true);
		
		//set up the world and agent
		VacAgent agent = new VacAgent(nodeMap);
		Percept percept = new PerceptBuilder()
			.withAgent(agent)
			.build();
		agent.see(percept);
		
		//ask agent for action
		Action action = agent.selectAction();
		Assert.assertEquals("Expected forward action because it is the closest unvisited", GoForward.class, action.getClass());
	}
	
	@Test
	public void testTurnWhenObstacleInFront() {
		VacAgent agent = new VacAgent();
		Percept percept = new PerceptBuilder()
			.withAgent(agent)
			.withWallAt(3, 2)
			.build();
		agent.see(percept);
		Action action = agent.selectAction();
		Assert.assertTrue("Expected turn action when the forward node is a wall", isTurn(action));
	}
	
	private boolean isTurn(Action action) {
		return action instanceof TurnLeft || action instanceof TurnRight;
	}
	
	private static class PerceptBuilder {
		private int startX = 3, startY = 3;
		private Agent agent = null;
		private MapBuilder mapBuilder = new MapBuilder();
		
		public PerceptBuilder withAgent(Agent agent) {
			this.agent = agent;
			return this;
		}
		
		public PerceptBuilder withDirtAt(int x, int y) {
			mapBuilder.withDirtAt(x, y);
			return this;
		}
		
		public PerceptBuilder withWallAt(int x, int y) {
			mapBuilder.withWallAt(x, y);
			return this;
		}
		
		public Percept build() {
			int[][] map = mapBuilder.build();
			VacuumState state = new VacuumState(map);
			state.setAgentDir(vacworld.Direction.NORTH);
			state.setAgentX(startX);
			state.setAgentY(startY);
			VacPercept percept = new VacPercept(state, agent);
			return percept;
		}
	}
	
	private static class MapBuilder {
		private int[][] map = {
				{VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL},
				{VacuumState.WALL, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.WALL}, 
				{VacuumState.WALL, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.WALL}, 
				{VacuumState.WALL, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.WALL}, 
				{VacuumState.WALL, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.WALL}, 
				{VacuumState.WALL, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.CLEAR, VacuumState.WALL},
				{VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL, VacuumState.WALL}};
		
		public MapBuilder withDirtAt(int x, int y) {
			this.map[x][y] = VacuumState.DIRT;
			return this;
		}
		
		public MapBuilder withWallAt(int x, int y) {
			this.map[x][y] = VacuumState.WALL;
			return this;
		}
		
		public int[][] build() {
			return map;
		}
	}
}
