/*
 * CluePlayer.java
 */

import java.util.ArrayList;
import java.util.Arrays;
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
		int[] dirArr = new int[] { 0, 1, 2, 3 };
		shuffleArray(dirArr);
		double ran_dir;
		boolean validRoll = false;
		boolean leavingRoom = false;
		boolean stopForDoor = false;
		boolean validDirection;
		//		boolean keepRolling = true;
		//		int col = 0, row = 0;
		int col = 0, row = 0;
		while(!validRoll) {
			// if they're in a room
			if(inRoom) {
				col = c_col;
				row = c_row;
				validRoll = true;
				for(int i = 0; i < dirArr.length; i++) {
					int direction = dirArr[i];
					col = c_col;
					row = c_row;
					validDirection = true;
					for(int dir = 0; dir < Clue.die && validDirection; dir++) {
						if(direction == 0) { // up
							row--;
						} else if(direction == 1) { // right
							col++;
						} else if(direction == 2) { // down
							row++;
						} else { // left
							col--;
						}
						if(inBounds(row, col)) {
							if(Clue.board.isDoor(row, col)) {
								if(door.getRow() == row && door.getColumn() == col) {
									leavingRoom = true;
								}
								break;
							} else if(Clue.board.getRoom(row, col).trim().equals("")) {
								validDirection = false;
							}
						} else {
							validDirection = false;
						}
					}
					if (inBounds(row, col) && validDirection) {
						validRoll = true;
						break;
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
						if(direction == 0) { // up
							row--;
						} else if(direction == 1) { // right
							col++;
						} else if(direction == 2) { // down
							row++;
						} else { // left
							col--;
						}
						if(inBounds(row, col)) {
							if(Clue.board.isDoor(row, col)) {
								door = new Square(row,col);
								inRoom = true;
								validRoll = true;
//								break;
							} else if(!Clue.board.getRoom(row, col).trim().equals("")) {
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
				leavingRoom = false;
			}
		}
		if(leavingRoom) {
			inRoom = false;
		}
		if(!inBounds(row,col)) {
			System.err.println(row + ", " + col);
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

	/**
	 *  Find a square on the board for a player to move to by rolling the 
	 *  die and chosing a good direction. The square that is chosen must
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

		return findSquareRand();
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


		return moveNaive(curr_row,curr_column,row,column,color,name,notes);
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
}
