/**
 * Square.java
 *
 */

/**
 *
 * This class represents a square on the board in the game of Clue.
 *
 * @author Roxanne Canosa
 * @author T.J. Borrelli
 */

class Square {

    private String color;     // The color of the square for display
    private String room;      // The room that the square belongs to
    private int row;          // The row of this square
    private int column;       // The column of this square
   
    public Square () {
        row = -1;
        column = -1;
    }
    
    public Square (int r, int c) {
        row = r;
        column = c;
    }

    public void setColor(String c) {
        color = c;
    }

    public String getColor() {
        return color;
    }

    public void setRoom(String r) {
        room = r;
    }

    public String getRoom() {
        return room;
    }

    public void setRow(int r) {
        row = r;
    }

    public int getRow() {
        return row;
    }

    public void setColumn(int c) {
        column = c;
    }

    public int getColumn() {
        return column;
    }
}
