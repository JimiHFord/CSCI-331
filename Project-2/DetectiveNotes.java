/**
 * DetectiveNotes.java
 *
 */

import java.util.*;

/**
 *
 * This class represents a player's detective notes in the game of Clue.
 *
 * @author Roxanne Canosa
 * @author T.J. Borrelli
 */

public class DetectiveNotes {

    private ArrayList<String> suspects = new ArrayList<String>();
    private ArrayList<String> weapons = new ArrayList<String>();
    private ArrayList<String> rooms = new ArrayList<String>();
    private String[] allSuspects = {"Scarlet","Mustard","White","Peacock",
                                    "Green","Plum"};
    private String[] allWeapons = {"Rope","Revolver","Wrench","Pipe",
                                   "Candlestick","Knife"};
    private String[] allRooms = {"Kitchen","Ballroom","Conservatory",
                                 "Dining Room","Billiard Room","Library",
                                 "Lounge", "Hallway","Study"};
    
    public String getRandomSuspect() {
        return allSuspects[(int)(Math.random()*6)];
    }

    public ArrayList<String> getMySuspects() {
        return suspects;
    }

    public void addSuspect(String suspect) {
        if (!suspects.contains(suspect))
            suspects.add(suspect);
    }

    public String getRandomWeapon() {
        return allWeapons[(int)(Math.random()*6)];
    }

    public ArrayList<String> getMyWeapons() {
        return weapons;
    }

    public void addWeapon(String weapon) {
        if (!weapons.contains(weapon))
            weapons.add(weapon);
    }
    
    public String getRandomRoom() { 
        return allRooms[(int)(Math.random()*9)];
    }

    public ArrayList<String> getMyRooms() {
        return rooms;
    }

    public void addRoom(String room) {
        if (!rooms.contains(room))
            rooms.add(room);
    }

    public void clear() {
        suspects.clear();
        weapons.clear();
        rooms.clear();
    }

    public String toString() {
        StringBuffer s = new StringBuffer();

        s.append(" Suspects:\n");
        Iterator iter = suspects.iterator();

        while (iter.hasNext()) {
            String suspect = (String)iter.next();
            s.append("  " + suspect + "\n" );
        }

        s.append(" Weapons:\n");
        Iterator iter2 = weapons.iterator();

        while (iter2.hasNext()) {
            String weapon = (String)iter2.next();
            s.append("  " + weapon + "\n" );
        }

        s.append(" Rooms:\n");
        Iterator iter3 = rooms.iterator();

        while (iter3.hasNext()) {
            String room = (String)iter3.next();
            s.append("  " + room + "\n" );
        }

        return s.toString();
    }
}
        
