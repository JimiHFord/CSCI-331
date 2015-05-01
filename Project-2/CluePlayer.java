/*
 * CluePlayer.java
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/** 
 * This class contains player strategies for the game of Clue. 
 *
 * @author     James Ford (jhf3617)
 *
 */

public class CluePlayer {  

	private boolean inRoom = false;
	private Square door = null;
	private TargetDoor target = null;
	private boolean onTheHunt = false;
	private int current_row, current_col;
	
	public static final String
		KITCHEN = "Kitchen",
		BALL_ROOM = "Ball Room",
		CONSERVATORY = "Conservatory",
		DINING_ROOM = "Dining Room",
		BILLIARD_ROOM = "Billiard Room",
		LOUNGE = "Lounge",
		HALL = "Hall",
		LIBRARY = "Library",
		STUDY = "Study";
	
	public static final String
		MUSTARD = "Mustard",
		WHITE = "White",
		GREEN = "Green",
		PEACOCK = "Peacock",
		SCARLET = "Scarlet",
		PLUM = "Plum";
		
	public static final String
		WRENCH = "Wrench",
		CANDLESTICK = "Candlestick",
		PIPE = "Pipe",
		ROPE = "Rope",
		REVOLVER = "Revolver",
		KNIFE = "Knife";
	
	private static final String[] weapons = {
		WRENCH,
		CANDLESTICK,
		PIPE,
		ROPE,
		REVOLVER,
		KNIFE
	};
	
	private static final String[] rooms = {
		KITCHEN,
		BALL_ROOM,
		CONSERVATORY,
		DINING_ROOM,
		BILLIARD_ROOM,
		LOUNGE,
		HALL,
		LIBRARY,
		STUDY
	};
	
	private static final String[] suspects = {
		MUSTARD,
		PLUM,
		PEACOCK,
		SCARLET,
		WHITE,
		GREEN
	};
	
	private PriorityQueue<TargetDoor> queue = null;
	
	private static final int 
	UP = 0,
	DOWN = 1,
	RIGHT = 2,
	LEFT = 3;

	/**
	 *  Find a random square on the board for a player to move to.
	 *
	 *  @return                  The square that the player ends up on  
	 */
	public Square findSquareRand() {

		int row = 0, col = 0;
		boolean valid = false;

		while (!valid) {
			col = (int)(Math.random()*(Clue.board.WIDTH)) + 1;
			row = (int)(Math.random()*(Clue.board.HEIGHT)) + 1;
			if (col >= 0 && col < Clue.board.WIDTH && 
					row >= 2 && row < Clue.board.HEIGHT)
				valid = true;
		}  
		return new Square(row, col);
	}

	private void shuffleArray(int[] array)
	{
		Random r = new Random();
		for (int i = array.length - 1; i > 0; i--)
		{
			int index = r.nextInt(i + 1);
			int a = array[index];
			array[index] = array[i];
			array[i] = a;
		}
	}
	
	private void initQueue() {
		queue = new PriorityQueue<TargetDoor>();
		for(int row = 2; row < Clue.board.HEIGHT; row++) {
			for(int col = 0; col < Clue.board.WIDTH; col++) {
				if(Clue.board.isDoor(row, col)) {
					queue.add(new TargetDoor(
						Clue.board.getRoom(row, col),row,col));
				}
			}
		}
	}
	
	private boolean isRoom(int row, int col) {
		try {
			return !Clue.board.getRoom(row, col).trim().equals("");
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	/**
	 *  Find a square on the board for a player to move to by rolling the 
	 *  die and choosing a random direction. The square that is chosen must
	 *  be legally accessible to the player (i.e., the player must adhere to 
	 *  the rules of the game to be there).
	 *
	 *  @param    c_row          The current row of this player
	 *  @param    c_col          The current column of this player
	 *
	 *  @return                  The square that this player ends up on
	 * @throws Exception 
	 */
	public Square findSquareRandDir(final int c_row, final int c_col) {
		int[] dirArr = new int[] { UP, RIGHT, DOWN, LEFT };
		shuffleArray(dirArr);
		boolean validRoll = false;
		boolean leavingRoom;
		boolean validDirection;
		int col = 0, row = 0;
		inRoom = isRoom(c_row, c_col);
		while(!validRoll) {
			// if they're in a room
			if(inRoom) {

				// they are currently either on a door or not
				col = c_col;
				row = c_row;
				final boolean onDoor = Clue.board.isDoor(c_row, c_col);
				
				// loop over direction options
				for(int i = 0; i < dirArr.length && !validRoll; i++) {
					// pick a direction
					int direction = dirArr[i];
					col = c_col;
					row = c_row;
					if(direction == UP) { // up
						validDirection = inBounds(row - Clue.die, col);
						leavingRoom = !isRoom(row - Clue.die, col);
					} else if(direction == RIGHT) { // right
						validDirection = inBounds(row, col + Clue.die);
						leavingRoom = !isRoom(row, col + Clue.die);
					} else if(direction == DOWN) { // down
						validDirection = inBounds(row + Clue.die, col);
						leavingRoom = !isRoom(row + Clue.die, col);
					} else { // left
						validDirection = inBounds(row, col - Clue.die);
						leavingRoom = !isRoom(row, col - Clue.die);
					}
					// if they're on a door and they're leaving,
					// the next square better not be in a room
					if(validDirection && onDoor && leavingRoom) {
						if(direction == UP) {
							validDirection = !isRoom(row - 1, col);
						} else if(direction == RIGHT) {
							validDirection = !isRoom(row, col + 1);
						} else if(direction == DOWN) {
							validDirection = !isRoom(row + 1, col);
						} else {
							validDirection = !isRoom(row, col - 1);
						}
					} 
					// check every position along the way to make sure this is a valid
					// direction to travel in given the number of steps 
					for(int dir = 0; dir < Clue.die && validDirection; dir++) {
						if(direction == UP) { // up
							row--;
						} else if(direction == RIGHT) { // right
							col++;
						} else if(direction == DOWN) { // down
							row++;
						} else { // left
							col--;
						}
						if(inBounds(row, col)) {
							if(Clue.board.isDoor(row, col)) {
								// ensure they are leaving the same door
								if(leavingRoom) {
									if(door.getRow() != row || door.getColumn() != col) {
										validDirection = false;
									}
								}
							} else if(isRoom(row, col)) {
								validDirection = false;
							}
						} else {
							validDirection = false;
						}
					}
					if (inBounds(row, col) && validDirection) {
						validRoll = true;
					}
				}
			} else { // they are not in a room
				// for each direction that they could move
				// test which one works
				for(int i = 0; i < dirArr.length && !validRoll; i++) {
					int direction = dirArr[i];
					col = c_col;
					row = c_row;

					validDirection = true;
					for(int die = 0; die < Clue.die && 
							validDirection && !validRoll; die++) {
						if(direction == UP) { // up
							row--;
						} else if(direction == RIGHT) { // right
							col++;
						} else if(direction == DOWN) { // down
							row++;
						} else { // left
							col--;
						}
						if(inBounds(row, col)) {
							if(Clue.board.isDoor(row, col)) {
								door = new Square(row,col);
								validRoll = true;
								//								break;
							} else if(isRoom(row, col)) {
								validDirection = false;
							}
						} else {
							validDirection = false;
						}
					} // check direction
					if (inBounds(row, col) && validDirection) {
						validRoll = true;
						//						break;
					}
				}
			}
			if(!validRoll) {
				roll();
			}
		}
		return new Square(row, col);
	}

	private void roll() {
		Clue.die = (int)(Math.random()*6) + 1;
	}

	private boolean inBounds(int row, int col) {
		return (col >= 0 && col < Clue.board.WIDTH && 
				row >= 2 && row < Clue.board.HEIGHT);
	}

	private boolean contains(String[] src, String target) {
		for(String s : src) {
			if(s.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	private List<String> filter(List<String> source, String[] by) {
		List<String> retVal = new ArrayList<String>();
		for(String s : source) {
			if(!contains(by,s)) {
				retVal.add(s);
			}
		}
		return retVal;
	}
	
	private List<String> filter(String[] source, List<String> by) {
		List<String> newSource = Arrays.asList(source);
		String[] newBy = new String[by.size()];
		int index = 0;
		for(String s : by) {
			newBy[index++] = s;
		}
		return filter(newSource, newBy);
	}
	
	private boolean onTheHunt(DetectiveNotes notes) {
		if(target == null) {
			return false;
		}
		List<String> rooms = notes.getMyRooms();
		if(rooms.contains(target.name)) {
			queue.remove(target.name);
			return false;
		}
		return true;
	}
	
	/**
	 *  Find a square on the board for a player to move to by rolling the 
	 *  die and choosing a good direction. The square that is chosen must
	 *  be legally accessible to the player (i.e., the player must adhere to 
	 *  the rules of the game to be there).
	 *
	 *  @param    c_row          The current row of this player
	 *  @param    c_col          The current column of this player
	 *  @param    notes          The Detective Notes of this player 
	 *
	 *  @return                  The square that this player ends up on
	 */
	public Square findSquareSmart(int c_row, int c_col, DetectiveNotes notes) {
		current_row = c_row;
		current_col = c_col;
		if(queue == null) initQueue();
		
		if(!queue.isEmpty()) {
			if(!onTheHunt(notes)) { 
				// if not already looking for room, find closest
				target = queue.remove();
			}
			return pathToDoor();
		} else {
			return findSquareRandDir(c_row, c_col);
		}
		
//		if(inRoom) {
//			// find direction to make first step
//		} else {
//			
//		}
//		System.out.println("Scarlet is targetting: " + target.name);
//		System.out.println("Scarlet is targetting: " + queue.remove().name);
//		return findSquareRandDir(c_row, c_col);
	}

	/**
	 * a* path
	 * @param start
	 * @param goal
	 * @return a* path
	 */
	private List<PathSquare> astarPath(PathSquare start) {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(20, new NodeComparator());
		LinkedList<Node> closed = new LinkedList<Node>();
		Node X = new Node(start);
		TargetDoor goal = start.goal;
		boolean found = false;
		if(start.equals(goal)) {
//			System.out.println("STARTING ON GOAL");
			found = true;
		}
		Node next;
		UndirectedEdge edge;
		PathSquare current, t1;
		pq.add(X);
		int count = 0;
		while(!pq.isEmpty() && !found) {
//			if((++count)%10 == 0) {
//				System.out.println(count);
//				if(count > 9) {
//					System.out.println("WARNING " + pq.size());
//					System.out.println("Start: " + "("+start.row+", "+start.col+")\t"
//							+ "Goal: " +"("+goal.row+", "+goal.col+")");
//				}
//			}
//			System.out.println("WARNING " + pq.size());
//			System.out.println("Start: " + "("+start.row+", "+start.col+")\t"
//					+ "Goal: " +"("+goal.row+", "+goal.col+")");
			X = pq.remove();
			closed.add(X);
			current = X.payload;
//			System.out.println("Current: (" + current.row+", "+current.col+")");
			if(X.payload.equals(goal)) {
				found = true;
				break;
			} else {
				for(int i = 0; i < current.edgeCount(); i++) {
					edge = current.get(i);
					t1 = edge.other(current);
					next = new Node(X, t1);
					if(!closed.contains(next) && !pq.contains(next) && inBounds(next.payload.row, next.payload.col)
							&& (!isRoom(next.payload.row, next.payload.col) ||
									Clue.board.isDoor(next.payload.row, next.payload.col))) {
						next.g = edge.weight + X.g;
						next.h = t1.distance();
						pq.add(next);
					}
				}
			}
		}
		ArrayList<PathSquare> result = new ArrayList<PathSquare>();
		while(!X.isRoot()) {
			result.add(0,X.payload);
			X = X.parent;
		}
		result.add(0, X.payload);
		return result;
	}
	
	private Square pathToDoor() {
		if(target == null) System.out.println("ERROR NULL");
		PathSquare start = new PathSquare(current_row, current_col, target);
		List<PathSquare> path = astarPath(start);
		PathSquare p;
		if(path.size() <= 1) {
			return findSquareRandDir(current_row, current_col);
		}
		if(Clue.die >= path.size()) {
			p = path.get(path.size()-1);
		} else {
			p = path.get(Clue.die);
		}
		return new Square(p.row, p.col);
	}
	
	/**
	 *  Move to a legal square on the board. If the move lands on a door,
	 *  make a suggestion by guessing a random suspect and random weapon.
	 *
	 *  @param    curr_row        The row of the player before move
	 *  @param    curr_column     The column of the player before move
	 *  @param    row             Selected row 
	 *  @param    column          Selected column 
	 *  @param    color           Player color
	 *  @param    name            Player name
	 *  @param    notes           Player Detective Notes 
	 *
	 *  @return                   A suggestion -> [name,room,suspect,weapon]
	 */
	public String[] moveNaive(int curr_row, int curr_column, 
			int row, int column, String color, String name, 
			DetectiveNotes notes) {

		String [] retVal = new String[4];
		String suspect = notes.getRandomSuspect();
		String weapon = notes.getRandomWeapon();
		String room = null;
		try {
			room = Clue.board.getRoom(row,column);
		} catch(ArrayIndexOutOfBoundsException e) {
			room = "";
		}

		if (Clue.board.isDoor(curr_row,curr_column))
			Clue.board.setColor(curr_row,curr_column,"Gray");
		else 
			Clue.board.setColor(curr_row, curr_column, "None");

		if (Clue.board.isDoor(row,column)) { 
			retVal[0] = name;
			retVal[1] = room;
			retVal[2] = suspect;
			retVal[3] = weapon;

			if (Clue.gui) {
				System.out.print(name+" suggests that the crime was committed");
				System.out.println(" in the " + room + " by " + suspect +
						" with the " + weapon);
			}
		}
		else retVal = null;

		Clue.board.setColor(row,column,color);

		return retVal;
	}

	/**
	 *  Move to a legal square on the board. If the move lands on a door,
	 *  make a good suggestion for the suspect and the weapon. A good
	 *  suggestion here is one which does not include any suspects or
	 *  weapons that are already in the Detective Notes of this player.
	 *
	 *  @param    curr_row        The row of the player before move
	 *  @param    curr_column     The column of the player before move
	 *  @param    row             Selected row 
	 *  @param    column          Selected column 
	 *  @param    color           Player color
	 *  @param    name            Player name
	 *  @param    notes           Player Detective Notes 
	 *
	 *  @return                   A suggestion -> [name,room,suspect,weapon]
	 */
	public String[] moveSmart(int curr_row, int curr_column, 
			int row, int column, String color, String name, 
			DetectiveNotes notes) {
		String [] retVal = new String[4];
		String suspect = filter(suspects, notes.getMySuspects()).get(0);
		String weapon = filter(weapons, notes.getMyWeapons()).get(0);
		String room = null;
		try {
			room = Clue.board.getRoom(row,column);
		} catch(ArrayIndexOutOfBoundsException e) {
			room = "";
		}

		if (Clue.board.isDoor(curr_row,curr_column))
			Clue.board.setColor(curr_row,curr_column,"Gray");
		else 
			Clue.board.setColor(curr_row, curr_column, "None");

		if (Clue.board.isDoor(row,column)) { 
			retVal[0] = name;
			retVal[1] = room;
			retVal[2] = suspect;
			retVal[3] = weapon;

			if (Clue.gui) {
				System.out.print(name+" suggests that the crime was committed");
				System.out.println(" in the " + room + " by " + suspect +
						" with the " + weapon);
			}
		}
		else retVal = null;

		Clue.board.setColor(row,column,color);

		return retVal;
	}

	private int contains(String target, String[] source) {
		return contains(target, source, 0);
	}
	
	private int contains(String target, String[] source, int start) {
		for(int i = start; i < source.length; i++) {
			if(source[i].equals(target)) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 *  Try to prove a suggestion is false by asking the players, in a
	 *  round-robin fashion, to show the suggester one of the suggestions if
	 *  the player has that suggested card in their hand. The other players 
	 *  know that ONE of the suggestions cannot be in the case file, but they 
	 *  do not know which one.
	 *
	 *  @param  suggestion      A suggestion -> [name,room,suspect,weapon]
	 *  @param  notes           The Detective Notes of the current player
	 *  @param  player          The current player
	 *  @param  next            The next player clockwise around the board
	 *  
	 *  @return                 An accusation, to check if it is a winner
	 *
	 */
	public ArrayList<String> prove(String[] suggestion, DetectiveNotes notes,
			int player, int next) {

		String card;
		boolean found = false;
		ArrayList<String> accusation = new ArrayList<String>();

		// Ask the other 5 players to show one of the suggested cards
		// YOUR CODE GOES HERE
		List<Object> cards;
		for(int other = 0; other < 6; other++) {
			if(other != player) {
				cards = Arrays.asList((Clue.allCards.get(other)).keySet().toArray());
				for(int i = 0; i < cards.size(); i++) {
					card = (String)cards.get(i);
					int contains = contains(card, suggestion, 1);
					if(contains > 0) {
						found = true;
						if(contains == 1) { // room
							notes.addRoom(card);
							while(queue != null && queue.remove(card));
						} else if(contains == 2) { // suspect
							notes.addSuspect(card);
						} else if(contains == 3) { // weapon
							notes.addWeapon(card);
						}
					}
				}
			}
		}
		// Make an accusation
		if (!found) {
			// Check this player's cards to see if this player has them
			for (int i=0; i<3; i++) { 
				card = (String)Arrays.asList(
						(Clue.allCards.get(Clue.turn)).keySet().toArray()).get(i);

				for (int k=1; k<=3; k++) 
					if (!found && card.equals(suggestion[k])) {
						found = true;
					}
			}
			// If still not found, I do believe I have won the game!
			for (int i=1; i<4; i++)
				if (!found)
					accusation.add(suggestion[i]);
				else 
					accusation.add("None");
		}

		return accusation;
	}
	
	

	/**
	 *  Update this player's detective notes upon learning some information.
	 *
	 *  @param    notes    The detective notes of this player
	 *  @param    card     The card that caused the change
	 *  @param    type     The type of the card - suspect, weapon, or room
	 *
	 */
	public void setNotes(DetectiveNotes notes, String card, String type) {

		if (type.equals("suspect"))
			notes.addSuspect(card);
		else if (type.equals("weapon"))
			notes.addWeapon(card);
		else if (type.equals("room"))
			notes.addRoom(card);
	}

	private class PathSquare {
		public final int row, col;
		public final TargetDoor goal;
		public PathSquare(int row, int col, TargetDoor goal) {
			this.row = row;
			this.col = col;
			this.goal = goal;
		}
		
		public boolean equals(Object o) {
			if(o == this) {
				return true;
			}
			if(o instanceof TargetDoor) {
				TargetDoor d = (TargetDoor)o;
				return this.row == d.row &&
						this.col == d.col;
			}
			if(!(o instanceof PathSquare)) {
				return false;
			}
			PathSquare p = (PathSquare)o;
			return this.row == p.row &&
					this.col == p.col;
		}
		
		public int edgeCount() {
			return 4;
		}
		
		public UndirectedEdge get(int i) {
			PathSquare temp;
			switch(i) {
			case UP: // up
				temp = new PathSquare(row - 1,col, this.goal);
				return new UndirectedEdge(this, temp);
			case RIGHT:
				temp = new PathSquare(row, col + 1, this.goal);
				return new UndirectedEdge(this, temp);
			case DOWN:
				temp = new PathSquare(row + 1, col, this.goal);
				return new UndirectedEdge(this, temp);
			default: // LEFT
				temp = new PathSquare(row, col - 1, this.goal);
				return new UndirectedEdge(this, temp);
			}
		}
		
		public int distance() {
			int a, b;
			a = row - goal.row;
			b = col - goal.col;
			return a*a + b*b; 
		}
	}
	
	/**
	 * private internal class used for traversals
	 * @author jimiford
	 *
	 */
	private class Node {
		public final Node parent;
		public final PathSquare payload;
		private double g;
		private double h;
		
		private Node(PathSquare payload) {
			parent = null;
			this.payload = payload;
		}
		
		private Node(Node parent, PathSquare payload) {
			this.parent = parent;
			this.payload = payload;
		}
		
		public boolean equals(Object o) {
			if(o == this) return true;
			if(!(o instanceof Node)) return false;
			Node casted = (Node)o;
			return this.payload.equals(casted.payload);
		}
		
		private boolean isRoot() {
			return parent == null;
		}
		
		public double f() {
			return g + h;
		}
	}
	
	private class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node o1, Node o2) {
			return o1.payload.distance() - o2.payload.distance();
		}
		
	}
	
	private class PathComparator implements Comparator<PathSquare> {
		@Override
		public int compare(PathSquare o1, PathSquare o2) {
			return o1.distance() - o2.distance();
		}
		
	}
	
	public class UndirectedEdge {
		public final double weight = 1;
		
		// private data members
		private PathSquare a, b;
		
		/**
		 * Construct an undirected edge
		 * @param id a unique identifier to distinguish between other edges
		 * @param a one vertex in the graph
		 * @param b another vertex in the graph not equal to <I>a</I>
		 */
		public UndirectedEdge(PathSquare a, PathSquare b) {
			this.a = b;
			this.b = a;
		}
		
		/**
		 * Get the <I>other</I> vertex given a certain vertex connected to
		 * this edge
		 * 
		 * @param current the current vertex
		 * @return the other vertex connected to this edge
		 */
		public PathSquare other(PathSquare current) {
			if(current == null) return null;
			return current == a ? b : a;
		}
	}
	
	private class TargetDoor implements Comparable<TargetDoor> {
		public final String name;
		public final int row, col;
		public TargetDoor(String name, int row, int col) {
			this.name = name;
			this.row = row;
			this.col = col;
		}
		
		@Override
		public boolean equals(Object o) {
			if(this == o) {
				return true;
			}
			if(o instanceof String) {
				String cast = (String)o;
				return name.equals(cast);
			}
			TargetDoor cast = (TargetDoor)o;
			return name.equals(cast.name);
		}

		@Override
		public int compareTo(TargetDoor o) {
			int myDistance = distance(current_row, current_col);
			int hisDistance = o.distance(current_row, current_col);
			return myDistance - hisDistance;
		}
		
		public int distance(int row, int col) {
			int a = this.row - row;
			int b = this.col - col;
			return (a*a) + (b*b);
		}
	}
}
