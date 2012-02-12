package jaredpearson;

public enum Direction {
	NORTH,
	EAST,
	SOUTH,
	WEST;
	
	public Direction rotateLeft() {
		switch (this) {
		case NORTH:
			return WEST;
		case EAST:
			return NORTH;
		case SOUTH:
			return EAST;
		case WEST:
			return SOUTH;
		}
		throw new IllegalStateException();
	}
	
	public Direction rotateRight() {
		switch (this) {
		case NORTH:
			return EAST;
		case EAST:
			return SOUTH;
		case SOUTH:
			return WEST;
		case WEST:
			return NORTH;
		}
		throw new IllegalStateException();
	}
}