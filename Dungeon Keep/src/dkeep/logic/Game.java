package dkeep.logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Game {
	private Hero hero = new Hero();
	private Vector<Guard> guards;
	private ArrayList<Ogre> ogres=new ArrayList<Ogre>();
	private Vector<Club> clubs;
	private Map map;
	private Lever lever;
	private Vector<Exit> exits;
	private Key key;

	/////////////////////////////////////////GETS AND SETS//////////////////////////////////////
	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}
	
	public ArrayList<Ogre> getOgres() {
		return ogres;
	}

	public void setOgres(ArrayList<Ogre> ogres) {
		this.ogres = ogres;
	}

	/////////////////////////////////////////LEVELS//////////////////////////////////////
	public boolean logicLevel1(char direction) {
		moveHero(direction);
		
		for (int j=0; j<guards.size(); j++)
		{
			eraseTrailC(guards.get(j)); // erases guard's trail as it changes position
			guards.get(j).behaviour();
			updateCharacterPosition(guards.get(j)); // updates guard's position on map
		}
		
		openLever(); // checks to see if lever can be opened and updates

		if (catchHero(guards.get(0))) { // checks if guard Rookie has caught hero
			return true;
		}
		
		if (guards.get(1).getAwake())
		{
			if (catchHero(guards.get(1))) { // checks if guard Drunken has caught hero
			return true;
			}
		}
			
		if (catchHero(guards.get(2))) { // checks if guard Suspicious has caught hero
			return true;
		}

		return false;
	}

	public boolean logicLevel2(char direction, int[] i) {
		
		moveHero(direction);
		pickClub(); // checks if hero has picked up club and updates
		moveOgre();

		pickKey(); // checks if hero has picked up key and updates
		nearKey(); // checks if ogre is near key and updates
		
		stunOgre(i); //checks if hero stuns ogre

		if (exits.get(0).getX() == hero.getX() && exits.get(0).getY() + 1 == hero.getY() && key.getPickedUp()) {
			// if hero is close to exit and has the key
			// the exit turns to stairs
			exits.get(0).open();
			updateObjectPosition(exits.get(0));
		}
		
		for (int j=0; j<ogres.size();j++)
		{
			if (!ogres.get(j).getStunned())
			{
				if (catchHero(ogres.get(j))) { // checks if guard has caught hero
					return true;
				}
			}
		}
		return false;
	}

	///////////////////////////////////////////////////////////////////////////////
	public boolean checkVictoryLevel1() {
		if ((exits.get(0).getX() == hero.getX() && exits.get(0).getY() == hero.getY())
				|| (exits.get(1).getX() == hero.getX() && exits.get(1).getY() == hero.getY()))
		// if hero is on the exit, you win
		{
			return true;
		}
		return false;
	}

	public boolean checkVictoryLevel2() {
		if (exits.get(0).getX() == hero.getX() && exits.get(0).getY() == hero.getY())
		// if hero is on top of stairs, you win
		{
			return true;
		}
		return false;
	}

	/////////////////////////////////////////HERO//////////////////////////////////////
	public void moveHero(char direction) {
		eraseTrailC(hero); // deletes trail when hero moves
		hero.movement(map, direction);
		updateCharacterPosition(hero); // updates hero's position on the map
	}

	public void openLever() {
		if ((lever.getX() - 1 == hero.getX() && lever.getY() == hero.getY())
				|| (lever.getX() + 1 == hero.getX() && lever.getY() == hero.getY())
				|| (lever.getX() == hero.getX() && lever.getY() - 1 == hero.getY())
				|| (lever.getX() == hero.getX() && lever.getY() + 1 == hero.getY()))
		// if hero is near lever (up,down,right,left)
		{
			// lever opens, exits open and both are updated
			lever.open();
			exits.get(0).open();
			updateObjectPosition(exits.get(0));
			exits.get(1).open();
			updateObjectPosition(exits.get(1));
		}
	}
	
	public void pickKey() {
		if ((key.getX() - 1 == hero.getX() && key.getY() == hero.getY())
				|| (key.getX() + 1 == hero.getX() && key.getY() == hero.getY())
				|| (key.getX() == hero.getX() && key.getY() - 1 == hero.getY())
				|| (key.getX() == hero.getX() && key.getY() + 1 == hero.getY()))
		// if hero is near key (up,down,right,left)
		{
			// key gets picked up, disappears, hero turns to K and all is
			// updated
			key.pickedUp();
			updateObjectPosition(key);
			hero.hasKey();
			updateCharacterPosition(hero);
		}
	}

	public void pickClub() {
		if ((clubs.get(0).getX() - 1 == hero.getX() && clubs.get(0).getY() == hero.getY())
				|| (clubs.get(0).getX() + 1 == hero.getX() && clubs.get(0).getY() == hero.getY())
				|| (clubs.get(0).getX() == hero.getX() && clubs.get(0).getY() - 1 == hero.getY())
				|| (clubs.get(0).getX() == hero.getX() && clubs.get(0).getY() + 1 == hero.getY()))
		// if hero is near clubs.get(1) (up,down,right,left)
		{
			// clubs.get(0) gets picked up, disappears, hero turns to A and all is updated
			hero.armed();
			updateCharacterPosition(hero);
			eraseTrailC(clubs.get(0));
		}
	}
	
	public void stunOgre(int[] i)
	{
		for (int j=0; j<ogres.size();j++)
		{

			if (((ogres.get(j).getX() - 1 == hero.getX() && ogres.get(j).getY() == hero.getY())
					|| (ogres.get(j).getX() + 1 == hero.getX() && ogres.get(j).getY() == hero.getY())
					|| (ogres.get(j).getX() == hero.getX() && ogres.get(j).getY() - 1 == hero.getY())
					|| (ogres.get(j).getX() == hero.getX() && ogres.get(j).getY() + 1 == hero.getY())) && hero.getArmed())
			// if ogre is near hero's club (up,down,left,right) and hero is
			// armed with it
			{
				// ogre turns to 8 and updates
				ogres.get(j).stun();
				updateCharacterPosition(ogres.get(j));
			} else if (ogres.get(j).getStunned() && i[j] == 2) {
				// otherwise, ogre goes back to O and updates
				ogres.get(j).notStunned();
				System.out.println(ogres.get(j).getStunned());
				updateCharacterPosition(ogres.get(j));
			}
		}
	}
	
	/////////////////////////////////////////GUARD//////////////////////////////////////

	
	/////////////////////////////////////////OGRE//////////////////////////////////////
	public void moveOgre() {
		
		for (int i = 0; i < ogres.size(); i++) {
			// generates ogre's trajectory randomly
			char direction = ogres.get(i).randomTrajectory();

			eraseTrailC(ogres.get(i)); // erases ogre's trail as it changes position
			ogres.get(i).movement(map, direction);
			updateCharacterPosition(ogres.get(i)); // updates ogre's position on map

			// generates club's position randomly
			eraseTrailC(clubs.get(i+1));
			clubs.get(i+1).movement(map, ogres.get(i).getX(), ogres.get(i).getY());
			updateCharacterPosition(clubs.get(i+1));
		}
	}
	
	/////////////////////////////////////////USEFUL FUNCTIONS//////////////////////////////////////
	public void nearKey() {
		
		for (int i = 0; i < ogres.size(); i++) {
			if (((key.getX() - 1 == ogres.get(i).getX() && key.getY() == ogres.get(i).getY())
					|| (key.getX() + 1 == ogres.get(i).getX() && key.getY() == ogres.get(i).getY())
					|| (key.getX() == ogres.get(i).getX() && key.getY() - 1 == ogres.get(i).getY())
					|| (key.getX() == ogres.get(i).getX() && key.getY() + 1 == ogres.get(i).getY())) && !key.getPickedUp())
			// if ogre is near key (up,down,left,right)
			{
				// ogre turns to $ and updates
				ogres.get(i).setSymbol('$');
				updateCharacterPosition(ogres.get(i));
			} else {
				if (ogres.get(i).getStunned() == true) {
					ogres.get(i).setSymbol('8');
					updateCharacterPosition(ogres.get(i));
				} else {
					ogres.get(i).setSymbol('O');
					updateCharacterPosition(ogres.get(i));
				}
			}

			for (int j = 1; j < clubs.size(); j++) {
				if (((key.getX() - 1 == clubs.get(j).getX() && key.getY() == clubs.get(j).getY())
						|| (key.getX() + 1 == clubs.get(j).getX() && key.getY() == clubs.get(j).getY())
						|| (key.getX() == clubs.get(j).getX() && key.getY() - 1 == clubs.get(j).getY())
						|| (key.getX() == clubs.get(j).getX() && key.getY() + 1 == clubs.get(j).getY()))
						&& !key.getPickedUp())
				// if club is near key (up,down,left,right)
				{
					// club turns to $ and updates
					clubs.get(j).setSymbol('$');
					updateCharacterPosition(clubs.get(j));
				} else {
					clubs.get(j).setSymbol('*');
					updateCharacterPosition(clubs.get(j));
				}
			}
		}
	}

	public boolean catchHero(Character c) {
			if ((c.getX() - 1 == hero.getX() && c.getY() == hero.getY())
					|| (c.getX() + 1 == hero.getX() && c.getY() == hero.getY())
					|| (c.getX() == hero.getX() && c.getY() - 1 == hero.getY())
					|| (c.getX() == hero.getX() && c.getY() + 1 == hero.getY())) {
				System.out.println("***********");
				System.out.println("*GAME OVER*");
				System.out.println("***********");
				System.out.println("You just got caught!");
				return true;
		}
		for (int i = 0; i < ogres.size(); i++) {
			if (c == ogres.get(i)) {
				if ((clubs.get(i+1).getX() - 1 == hero.getX() && clubs.get(i+1).getY() == hero.getY())
						|| (clubs.get(i+1).getX() + 1 == hero.getX() && clubs.get(i+1).getY() == hero.getY())
						|| (clubs.get(i+1).getX() == hero.getX() && clubs.get(i+1).getY() - 1 == hero.getY())
						|| (clubs.get(i+1).getX() == hero.getX() && clubs.get(i+1).getY() + 1 == hero.getY())) {
					System.out.println("***********");
					System.out.println("*GAME OVER*");
					System.out.println("***********");
					System.out.println("You just got caught!");
					return true;
				}
			}
		}
		return false;
	}
	
	/////////////////////////////////////////UPDATE MAP//////////////////////////////////////
	public void loadElementsLevel1() {
		// map
		map = new Map(1);

		// lever
		lever = new Lever(8, 7);
		map.insertObject(lever);

		// exits
		exits = new Vector<Exit>();
		exits.add(new Exit(5, 0));
		exits.add(new Exit(6, 0));
		exits.add(new Exit(1, 4));
		exits.add(new Exit(3, 4));
		exits.add(new Exit(3, 2));
		exits.add(new Exit(8, 2));
		exits.add(new Exit(8, 4));

		for (int i = 0; i < exits.size(); i++) {
			map.insertObject(exits.get(i));
		}

		// guard
		guards = new Vector<Guard>();
		guards.add(new GuardRookie(1, 8)); //Rookie
		guards.add(new GuardDrunken(1, 8)); //Drunken
		guards.add(new GuardSuspicious(1, 8)); //Suspicious
		
		for (int i = 0; i < guards.size(); i++) {
			map.insertCharacter(guards.get(i));
		}

		// hero
		hero.setX(1);
		hero.setY(1);
		map.insertCharacter(hero);
	}

	public void loadElementsLevel2() {
		// map
		map = new Map(2);

		// key
		key = new Key(1, 7);
		map.insertObject(key);

		// exits
		exits = new Vector<Exit>();
		exits.add(new Exit(1, 0));

		for (int i = 0; i < exits.size(); i++) {
			map.insertObject(exits.get(i));
		}

		// ogre
		Random n = new Random();
		int value = n.nextInt(5);
		for (int i=0; i<value;i++)
		{
			ogres.add(new Ogre(1,4));
			map.insertCharacter(ogres.get(i));
		}
		
		clubs = new Vector<Club>();
		clubs.add(new Club(7,5)); //hero's club
		
		for (int i=1; i<ogres.size()+1;i++)
		{
			clubs.add(new Club(7,3)); //ogre's club
			clubs.get(i).movement(map, ogres.get(i-1).getX(), ogres.get(i-1).getY());
		}
		
		
		for (int i = 0; i < clubs.size(); i++) {
			map.insertCharacter(clubs.get(i));
		}

		// hero
		hero.setX(7);
		hero.setY(1);
		map.insertCharacter(hero);
	}

	public void eraseTrailC(Character c) {
		map.getMap()[c.getX()][c.getY()] = ' ';
	}

	public void eraseTrailO(Object o) {
		map.getMap()[o.getX()][o.getY()] = ' ';
	}

	public void updateCharacterPosition(Character c) {
		map.insertCharacter(c);
	}

	public void updateObjectPosition(Object o) {
		map.insertObject(o);
	}

	}