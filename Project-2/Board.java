/*
 * Board.java
 */

import java.util.*;

/**
 *
 * This class represents the state of the game board in the game of Clue.
 *
 * @author Roxanne Canosa
 * @author T.J. Borrelli
 */

class Board {

    final static int HEIGHT = 20;
    final static int WIDTH = 18; 

    private int column = 0;        // The preferred column for a player
    private int row = 0;           // The preferred row for a player

    private Square board[][] = new Square[HEIGHT][WIDTH];

    /**
     *  Constructor for an initial board 
     */
    public Board () {

        for (int i=0; i < HEIGHT; i++)
            for (int j=0; j < WIDTH; j++) {
                board[i][j] = new Square(i,j);
                setColor(i,j,"None");
                if ( (i>1) && (i<5) && (j>-1) && (j<5) ) {
                    if (isKitchenDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Kitchen");
                }
                else if ( (i>1) && (i<6) && (j>5) && (j<12) ) {
                    if (isBallroomDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Ballroom");
                }
                else if ( (i>1) && (i<6) && (j>13) && (j<18) ) {
                    if (isConservatoryDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Conservatory");
                }
                else if ( (i>7) && (i<13) && (j>-1) && (j<5) ) {
                    if (isDiningRoomDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Dining Room");
                }
                else if ( (i>6) && (i<10) && (j>13) && (j<18) ) {
                    if (isBilliardRoomDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Billiard Room");
                }
                else if ( (i>11) && (i<15) && (j>13) && (j<18) ) {
                    if (isLibraryDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Library");
                }
                else if ( (i>15) && (i<20) && (j>-1) && (j<5) ) {
                    if (isLoungeDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Lounge");
                }
                else if ( (i>14) && (i<20) && (j>6) && (j<12) ) {
                    if (isHallwayDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Hallway");
                }
                else if ( (i>16) && (i<20) && (j>13) && (j<18) ) {
                    if (isStudyDoor(i,j)) setColor(i,j,"Gray");
                    setRoom(i,j,"Study");
                }
                else if ( (i>7) && (i<13) && (j>6) && (j<12) ) {
                    setRoom(i,j,"Center");
                }
                else 
                    setRoom(i,j," ");
            }
       
        // Set up the initial locations of the players
        setColor(19,5,"Red");
        setColor(14,0,"Yellow");
        setColor(2,5,"White");
        setColor(2,12,"Blue");
        setColor(11,17,"Green");
        setColor(15,17,"Magenta");     
    }

    /**
     *  Get a copy of the Board - useful for a search algorithm where
     *  you want to try different strategies before choosing a specific one.
     *
     *  @param     other   The original Board of which you want a copy.
     */
    public Board(Board other) {

        for (int i=0; i < HEIGHT; i++) 
            for (int j=0; j < WIDTH; j++) {
                board[i][j] = new Square(i,j);
                setColor(i,j,other.getColor(i,j));
                setRoom(i,j,other.getRoom(i,j));
            }
    }

    /**
     *  Mutator method to set a square to a specific room
     *
     *  @param    row     The row number, 2 to 19
     *  @param    column  The column number, 0 to 17
     *  @param    room    The room
     */
    public void setRoom(int row, int column, String room) {
        board[row][column].setRoom(room);
    }

    /**
     *  Accessor method to get the room that a player is in 
     *
     *  @param    row     The row number, 2 to 19 
     *  @param    column  The column number, 0 to 17
     *
     *  @return           The room at that row, column
     */
    public String getRoom(int row, int column) {
        return board[row][column].getRoom();
    }

    /**
     *  Mutator method to change the color of a square
     *
     *  @param    row     The row number, 2 to 19
     *  @param    column  The column number, 0 to 17
     *  @param    color   The color of the square
     */
    public void setColor(int row, int column, String color) {
        board[row][column].setColor(color);
    }
    
    /**
     *  Accessor method to get the color of a square
     *
     *  @param    row     The row number, 2 to 19
     *  @param    column  The column number, 0 to 17
     *
     *  @return           The color of the square at that row, column
     */
    public String getColor(int row, int column) {
        return board[row][column].getColor();
    }

    /**
     *  Check if the selected square is any door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a door 
     */
    public boolean isDoor(int row, int column) {
         
        return isKitchenDoor(row,column) || isBallroomDoor(row,column) 
            || isConservatoryDoor(row,column) || isDiningRoomDoor(row,column) 
            || isBilliardRoomDoor(row,column) || isLibraryDoor(row,column) 
            || isLoungeDoor(row,column) || isHallwayDoor(row,column) 
            || isStudyDoor(row,column);
    }

    /**
     *  Check if the selected square is a Kitchen door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Kitchen door 
     */
    public boolean isKitchenDoor(int row, int column) {
         
        return (row==4 && column==3);
    }

    /**
     *  Check if the selected square is a Ballroom door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Ballroom door 
     */
    public boolean isBallroomDoor(int row, int column) {
         
        return (row==4 && column==6)  || (row==5 && column==7)
            || (row==5 && column==10) || (row==4 && column==11);
    }
    
    /**
     *  Check if the selected square is a Conservatory door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Conservatory door 
     */
    public boolean isConservatoryDoor(int row, int column) {
         
        return (row==5 && column==14);
    }
    
    /**
     *  Check if the selected square is a Dining Room door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Dining Room door 
     */
    public boolean isDiningRoomDoor(int row, int column) {
         
        return (row==12 && column==3) || (row==9 && column==4);
    }
    
    /**
     *  Check if the selected square is a Billiard Room door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Billiard Room door 
     */
    public boolean isBilliardRoomDoor(int row, int column) {
         
        return (row==7 && column==14) || (row==9 && column==16);
    }
    
    /**
     *  Check if the selected square is a Library door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Library door 
     */
    public boolean isLibraryDoor(int row, int column) {
         
        return (row==12 && column==15) || (row==13 && column==14);
    }

    /**
     *  Check if the selected square is a Lounge door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Lounge door 
     */
    public boolean isLoungeDoor(int row, int column) {
         
        return (row==16 && column==4);
    }
    
    /**
     *  Check if the selected square is a Hallway door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Hallway door 
     */
    public boolean isHallwayDoor(int row, int column) {
         
        return (row==16 && column==11) || (row==15 && column==9);
    }
    
    /**
     *  Check if the selected square is a Study door
     *  @param     row      The row number, 2 to 19
     *  @param     column   The column number, 0 to 17
     *
     *  @return             True if square is a Study door 
     */
    public boolean isStudyDoor(int row, int column) {
         
        return (row==17 && column==14);
    }
}
