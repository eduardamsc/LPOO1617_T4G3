package dkeep.logic;

// TODO: Auto-generated Javadoc
/**
 * The Class Club.
 */
public class Club extends Character{
	/**
	 * Constructor for class Club.
	 */	
	public Club() {
		super();
		this.symbol = '*';
	}
	
	/**
	 * Constructor for class Club with coordinates.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */	
	public Club(int x, int y) {
		this.x = x;
		this.y = y;
		this.symbol = '*';
	}

	/////////////////////////////////////////MOVEMENTS//////////////////////////////////////
	/**
	 * Moves Club around Ogre.
	 * @param map Map in which Club is moving.
	 * @param x Line of Ogre's current position.
	 * @param y Column of Ogre's current position.
	 */	
	public void movement(Map map, int x, int y) {

		char direction = randomTrajectory();

		while (!wall(map, direction, x, y)) {
			direction = randomTrajectory();
		}
		switch (direction) {
		case 'w':
			this.x = x - 1;
			this.y = y;
			break;
		case 's':
			this.x = x + 1;
			this.y = y;
			break;
		case 'a':
			this.x = x;
			this.y = y - 1;
			break;
		case 'd':
			this.x = x;
			this.y = y + 1;
			break;

		}
	}
	
	/**
	 * Checks if Club can move to a certain position around the Ogre.
	 *
	 * @param map
	 *            Map in which Club is moving.
	 * @param direction
	 *            Direction in which Club is trying to move.
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return True for permission to move.
	 */	
	public boolean wall(Map map, char direction, int x, int y) {
		switch (direction) {
		case 'w':
			return wallAux(map, x, y, -1, 0);
		case 's':
			return wallAux(map, x, y, 1, 0);
		case 'a':
			return wallAux(map, x, y, 0, -1);
		case 'd':
			return wallAux(map, x, y, 0, 1);
		}
		return true;
	}
	
	/**
	 * Wall aux.
	 *
	 * @param map
	 *            the map
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @param i
	 *            the i
	 * @param j
	 *            the j
	 * @return true, if successful
	 */
	public boolean wallAux(Map map, int x, int y, int i, int j)
	{
		int a=x+i, b=y+j;
		if (map.getMap()[a][b] == 'X' || map.getMap()[a][b] == 'k' || map.getMap()[a][b] == 'I'
				|| map.getMap()[a][b] == 'G' || map.getMap()[a][b] == 'H' || map.getMap()[a][b] == 'A' || map.getMap()[a][b] == 'K'
				|| a < 0 || (b) < 0 || a > map.getMap().length
				|| b > map.getMap()[a].length)
			return false;
		else return true;
	}
}
