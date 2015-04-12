 /*
  * Clue.java
  */

import java.util.*;
import java.awt.*;        
import java.awt.event.*;
import javax.swing.*;

/** 
 * This program is the driver for the game of Clue. 
 *
 * Run the program as one of the following:
 *     java Clue          (GUI with a default delay time of 1 second)
 *     java Clue delay    (GUI with a delay of (delay) milliseconds)
 *     java Clue 0        (GUI with a button for each player)
 *     java Clue -delay   (No GUI - run program (delay) times)
 *
 * @author     Roxanne Canosa
 * @author	   T.J. Borrelli
 *
 */

public class Clue extends JPanel {
       
    final static int SCARLET = 0;                 // Whose turn it is
    final static int MUSTARD = 1;
    final static int WHITE = 2;
    final static int PEACOCK = 3;
    final static int GREEN = 4;
    final static int PLUM = 5;
    
    static Board board = new Board();             // The game state
    static int die;
    static int delay;
    static int turn;
    static boolean gui = true;

    static ArrayList<CluePlayer> allPlayers = new ArrayList<CluePlayer>();
    static ArrayList<HashMap<String,String>> allCards = 
        new ArrayList<HashMap<String,String>>();

    private Scarlet scarlet = new Scarlet();      // The players
    private Mustard mustard = new Mustard();
    private White white = new White();
    private Peacock peacock = new Peacock();
    private Green green = new Green();
    private Plum plum = new Plum();

    private ArrayList<String> suspects = new ArrayList<String>();
    private ArrayList<String> weapons = new ArrayList<String>();
    private ArrayList<String> rooms = new ArrayList<String>();
    private ArrayList<String> casefile = new ArrayList<String>();
    private ArrayList<String> cards = new ArrayList<String>();
    private HashMap<String,String> sCards = new HashMap<String,String>();
    private HashMap<String,String> mCards = new HashMap<String,String>();
    private HashMap<String,String> wCards = new HashMap<String,String>();
    private HashMap<String,String> pCards = new HashMap<String,String>();
    private HashMap<String,String> gCards = new HashMap<String,String>();
    private HashMap<String,String> uCards = new HashMap<String,String>();
    
    private javax.swing.Timer timer, animationTimer;
    private long startTime, stopTime, runTime = 0;
    private boolean done = false, scarlet_lost = false, mustard_lost = false,
        white_lost = false, peacock_lost = false,
        green_lost = false, plum_lost = false;
    private int scarlet_won, mustard_won, white_won, peacock_won, 
        green_won, plum_won;
    
    /**
     *  This constructor sets up the initial game configuration, 
     *  and starts the timer with a default delay of 1 second.
     */
    public Clue() { this(1000); }

    /**
     *  This constructor sets up the initial game configuration, 
     *  and starts the timer with a user specified delay.
     *
     *  @param    delay    number of milliseconds between player moves
     */
    public Clue(int delay) {
       
        // Run the game with GUI using a timer
        if (delay > 0) {
            initGame();
            setBackground(Color.ORANGE);
            timer = new javax.swing.Timer(delay, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        playerMove();
                        if (done) {
                            repaint();
                            timer.stop();
                        }
                        else
                            repaint();
                    }
                });
            
            // Create the Start and Stop buttons
            JButton start = new JButton("Start"); 
            start.setBounds(10,20,80,25); 
            add(start);
            start.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        timer.start();
                    }
                });

            JButton stop = new JButton("Stop"); 
            stop.setBounds(10,80,80,25); 
            add(stop);
            stop.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        timer.stop();
                    }
                });
        }

        // Run the game with GUI by clicking a button for each player
        if (delay == 0) {
            initGame();
            setBackground(Color.ORANGE);

            JButton button_0 = new JButton("Scarlet"); 
            button_0.setBounds(10,0,80,25); 
            add(button_0);
            button_0.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        turn = SCARLET;
                        playerMove();
                        repaint();
                    }
                });
            
            JButton button_1 = new JButton("Mustard"); 
            button_1.setBounds(10,50,80,25); 
            add(button_1);
            button_1.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        turn = MUSTARD;
                        playerMove();
                        repaint();
                    }
                });

            JButton button_2 = new JButton("White"); 
            button_2.setBounds(10,100,80,25); 
            add(button_2);
            button_2.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        turn = WHITE;
                        playerMove();
                        repaint();
                    }
                });

            JButton button_3 = new JButton("Peacock"); 
            button_3.setBounds(10,150,80,25); 
            add(button_3);
            button_3.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        turn = PEACOCK;
                        playerMove();
                        repaint();
                    }
                });

            JButton button_4 = new JButton("Green"); 
            button_4.setBounds(10,200,80,25); 
            add(button_4);
            button_4.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        turn = GREEN;
                        playerMove();
                        repaint();
                    }
                });

            JButton button_5 = new JButton("Plum"); 
            button_5.setBounds(10,250,80,25); 
            add(button_5);
            button_5.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent evt){
                        turn = PLUM;
                        playerMove();
                        repaint();
                    }
                });

            if (done) {
                System.out.print("\nCase File: ");
                for(String element: casefile) 
                    System.out.print(element + "     ");
                System.out.println();
                if (turn==SCARLET) System.out.println("Scarlet won.");
                else if (turn==MUSTARD) System.out.println("Mustard won.");
                else if (turn==WHITE) System.out.println("White won.");
                else if (turn==PEACOCK) System.out.println("Peacock won.");
                else if (turn==GREEN) System.out.println("Green won.");
                else if (turn==PLUM) System.out.println("Plum won.");
            }
        }

        // Run the game without the GUI - as many times as specified in delay.
        if (delay < 0) {
            gui = false;

            // Keep track of how many wins each player has
            scarlet_won=0; mustard_won=0; white_won=0; peacock_won=0; 
            green_won=0; plum_won=0;

            // Start timing how long it takes to play "delay" games
            startTime = new Date().getTime();

            // Play a bunch of games!
            for (int times=0; times < -delay; times++) {
                initGame();
                done = false;
                while (!done)
                    playerMove();
            }

            stopTime = new Date().getTime();
            runTime = (stopTime - startTime);

            int deduction = -delay-scarlet_won;
            System.out.println("===========================");
            System.out.println("Total number of games = " + -delay);
            System.out.println("Miss Scarlet won " + scarlet_won + " times");
            System.out.println("Colonel Mustard won " + mustard_won + " times");
            System.out.println("Mrs. White won " + white_won + " times");
            System.out.println("Mrs. Peacock won " + peacock_won + " times");
            System.out.println("Mr. Green won " + green_won + " times");
            System.out.println("Professor Plum won " + plum_won + " times");
            
            System.out.print("\nRuntime for " + -delay + " games = ");
            System.out.println(runTime + " milliseconds");
            System.out.println("===========================");
            
            System.out.println("Points deducted = " + deduction);
        }
    }
   
    /** 
     *  Initialize the game state
     *
     */
    public void initGame() {
        shuffleCards();  
        dealCards();
        turn = SCARLET;
    }  

    /**
     *  Fill up the card lists and shuffle them.
     *
     */
    public void shuffleCards() {

        suspects.clear();
        weapons.clear();
        rooms.clear();
        
        suspects.add("Scarlet");
        suspects.add("Mustard");
        suspects.add("White");
        suspects.add("Peacock");
        suspects.add("Green");
        suspects.add("Plum");

        weapons.add("Wrench");
        weapons.add("Candlestick");
        weapons.add("Pipe");
        weapons.add("Rope");
        weapons.add("Revolver");
        weapons.add("Knife");

        rooms.add("Kitchen");
        rooms.add("Ballroom");
        rooms.add("Conservatory");
        rooms.add("Billiard Room");
        rooms.add("Library");
        rooms.add("Study");
        rooms.add("Hallway");
        rooms.add("Lounge");
        rooms.add("Dining Room");

        Collections.shuffle(suspects);
        Collections.shuffle(weapons);
        Collections.shuffle(rooms);
    }
 
    /**
     *  Create the case file and deal the cards to the players.
     *
     */
    public void dealCards() {

        allPlayers.clear();
        allCards.clear();
        casefile.clear();
        cards.clear();
        scarlet.clear();
        mustard.clear();
        white.clear();
        peacock.clear();
        green.clear();
        plum.clear();

        casefile.add(rooms.remove(0));
        casefile.add(suspects.remove(0));
        casefile.add(weapons.remove(0));

        Iterator iter1 = suspects.iterator();
        while (iter1.hasNext()){
            cards.add((String)iter1.next());
        }
        
        Iterator iter2 = weapons.iterator();
        while (iter2.hasNext()){
            cards.add((String)iter2.next());
        }

        Iterator iter3 = rooms.iterator();
        while (iter3.hasNext()){
            cards.add((String)iter3.next());
        }

        Collections.shuffle(cards);

        for (int i=0; i<3; i++) { 
            sCards.put(cards.get(0),cardType(cards.get(0)));
            scarlet.setNotes(cards.get(0),cardType(cards.remove(0)));
            mCards.put(cards.get(0),cardType(cards.get(0)));
            mustard.setNotes(cards.get(0),cardType(cards.remove(0)));
            wCards.put(cards.get(0),cardType(cards.get(0)));
            white.setNotes(cards.get(0),cardType(cards.remove(0)));
            pCards.put(cards.get(0),cardType(cards.get(0)));
            peacock.setNotes(cards.get(0),cardType(cards.remove(0)));
            gCards.put(cards.get(0),cardType(cards.get(0)));
            green.setNotes(cards.get(0),cardType(cards.remove(0)));
            uCards.put(cards.get(0),cardType(cards.get(0)));
            plum.setNotes(cards.get(0),cardType(cards.remove(0)));
        }

        scarlet.setCards(sCards);
        mustard.setCards(mCards);
        white.setCards(wCards);
        peacock.setCards(pCards);
        green.setCards(gCards);
        plum.setCards(uCards);

        allCards.add(sCards);
        allCards.add(mCards);
        allCards.add(wCards);
        allCards.add(pCards);
        allCards.add(gCards);
        allCards.add(uCards);

        allPlayers.add(scarlet);
        allPlayers.add(mustard);
        allPlayers.add(white);
        allPlayers.add(peacock);
        allPlayers.add(green);
        allPlayers.add(plum);

        if (gui) {
            scarlet.printCards();
            scarlet.printNotes();
            mustard.printCards();
            mustard.printNotes();
            white.printCards();
            white.printNotes();
            peacock.printCards();
            peacock.printNotes();
            green.printCards();
            green.printNotes();
            plum.printCards();
            plum.printNotes();
        }

    }

    /**
     *  Find the type of card - suspect, weapon, or room.
     *  The card is the "key" in the HashMap, and the type is the "value".
     *
     *  @param     card     the card
     *
     *  @return             the kind of card it is -> suspect, weapon, or room. 
     *
     */
    public static String cardType(String card) {
    
        String type = "None";

        if ( card.equals("Scarlet") || card.equals("Mustard") 
             || card.equals("White") || card.equals("Peacock") 
             || card.equals("Green") || card.equals("Plum") )
                type = "suspect";
        else if ( card.equals("Rope") || card.equals("Candlestick") 
                  || card.equals("Knife") || card.equals("Revolver") 
                  || card.equals("Wrench") || card.equals("Pipe") )
            type = "weapon";
        else if ( card.equals("Kitchen") || card.equals("Ballroom") 
                  || card.equals("Conservatory") || card.equals("Dining Room") 
                  || card.equals("Billiard Room") || card.equals("Library")
                  || card.equals("Lounge") || card.equals("Hallway") 
                  || card.equals("Study") )
            type = "room";
        
        return type;
    }
    
    /**
     *  A player makes a move when the Timer goes off. Miss Scarlet goes
     *  first, and then the other players in turn.
     */
    public void playerMove() { 
        
        die = (int)(Math.random()*6) + 1;
        
        if (turn == SCARLET) {
            if (!scarlet_lost) done = make_accusation(scarlet.strategy());
            if (!done) turn = MUSTARD;
        }
        else if (turn == MUSTARD) {
            if (!mustard_lost) done = make_accusation(mustard.strategy());
            if (!done) turn = WHITE;
        }
        else if (turn == WHITE) {
            if (!white_lost) done = make_accusation(white.strategy());
            if (!done) turn = PEACOCK;
        }
        else if (turn == PEACOCK) {
            if (!peacock_lost )done = make_accusation(peacock.strategy());
            if (!done) turn = GREEN;
        }
        else if (turn == GREEN) {
            if (!green_lost)done = make_accusation(green.strategy());
            if (!done) turn = PLUM;
        }
        else if (turn == PLUM) {
            if (!plum_lost) done = make_accusation(plum.strategy());
            if (!done) turn = SCARLET;
        }

        if (done) {
            if (gui) {
                System.out.print("\nCase File: ");
                for(String element: casefile) 
                    System.out.print(element + "     ");
                System.out.println();
                if (turn==SCARLET) System.out.println("Scarlet won.");
                else if (turn==MUSTARD) System.out.println("Mustard won.");
                else if (turn==WHITE) System.out.println("White won.");
                else if (turn==PEACOCK) System.out.println("Peacock won.");
                else if (turn==GREEN) System.out.println("Green won.");
                else if (turn==PLUM) System.out.println("Plum won.");
            }
            
            if (turn==SCARLET) scarlet_won++;
            else if (turn==MUSTARD) mustard_won++;
            else if (turn==WHITE) white_won++;
            else if (turn==PEACOCK) peacock_won++;
            else if (turn==GREEN) green_won++;
            else if (turn==PLUM) plum_won++;
        }
    }
       
    /**
     *  A player makes an accusation. Check to see if it is correct.
     *
     *  @param accusation     The accusation
     *
     *  @return               True if the accusation is correct.
     */
    public boolean make_accusation(ArrayList<String> accusation) {
        
        boolean winner = true;

        if ( (accusation != null) && (accusation.size() != 0) ) {
            for (int i=0; i<3; i++) 
                if (!(accusation.get(i)).equals(casefile.get(i))) {
                    winner = false;
                    if (!(accusation.get(1)).equals("None")) {
                        if (turn == SCARLET) scarlet_lost = true;
                        if (turn == MUSTARD) mustard_lost = true;
                        if (turn == WHITE) white_lost = true;
                        if (turn == PEACOCK) peacock_lost = true;
                        if (turn == GREEN) green_lost = true;
                        if (turn == PLUM) plum_lost = true;
                    }
                }
        }
        else 
            winner = false;
        
        return winner;
    }
    
   /**
    *  Draw the board and the current state of the game. 
    *
    *  @param    g    the graphics context of the game
    */
   public void paintComponent(Graphics g) {
      
       super.paintComponent(g);  // Fill panel with background color
       Font big = new Font("SansSerif", Font.BOLD, 48);
       Font small = new Font("SansSerif", Font.BOLD, 18);
       
       int width = getWidth();
       int height = getHeight();
       int xoff = width / board.WIDTH;
       int yoff = height / board.HEIGHT;

       g.setColor(Color.BLACK);       
       // Draw the horizontal lines on the board
       for (int i=2; i <= board.HEIGHT; i++)
           g.drawLine(0, i*yoff, width, i*yoff);
       
       // Draw the vertical lines on the board
       for (int i=0; i <= board.WIDTH; i++) 
           g.drawLine(i*xoff, 2*yoff, i*xoff, height);
       
       // Draw the contents of each square
       for (int i=0; i < board.HEIGHT; i++)         
           for (int j=0; j < board.WIDTH; j++) {
               String color = board.getColor(i,j);
               String room = board.getRoom(i,j);
               String roomChar = " ";

               if (room.equals("Kitchen")) roomChar = "k";
               else if (room.equals("Ballroom")) roomChar = "b";
               else if (room.equals("Conservatory")) roomChar = "c";
               else if (room.equals("Dining Room")) roomChar = "d";
               else if (room.equals("Billiard Room")) roomChar = "i";
               else if (room.equals("Library")) roomChar = "l";
               else if (room.equals("Lounge")) roomChar = "o";
               else if (room.equals("Hallway")) roomChar = "h";
               else if (room.equals("Study")) roomChar = "s";
               else if (room.equals("Center")) roomChar = "X";

               if ( color.equals("Red") ) {
                   g.setColor(Color.RED);
                   g.fillOval(j*xoff+4,i*yoff+4,25,25);
               }
               else if ( color.equals("Yellow") ) {
                   g.setColor(Color.YELLOW);
                   g.fillOval(j*xoff+4,i*yoff+4,25,25);
               }
               else if ( color.equals("White") ) {
                   g.setColor(Color.WHITE);
                   g.fillOval(j*xoff+4,i*yoff+4,25,25);
               }
               else if ( color.equals("Blue") ) {
                   g.setColor(Color.BLUE);
                   g.fillOval(j*xoff+4,i*yoff+4,25,25);
               }
               else if ( color.equals("Green") ) {
                   g.setColor(Color.GREEN);
                   g.fillOval(j*xoff+4,i*yoff+4,25,25);
               }
               else if ( color.equals("Magenta") ) {
                   g.setColor(Color.MAGENTA);
                   g.fillOval(j*xoff+4,i*yoff+4,25,25);
               }
               else if ( color.equals("Gray") ) {
                   g.setColor(Color.LIGHT_GRAY);
                   g.fillRect(j*xoff+1,i*yoff+1,29,29);
               }
               g.setColor(Color.BLACK);
               g.setFont(small);
               g.drawString(roomChar,(j*xoff+9),(i*yoff+21));
           }

      
       // Check if any player has won the game
       if ( done ) {  
           g.setColor(Color.RED);
           if (turn == SCARLET) g.drawString("SCARLET WON!", 200, 50);
           else if (turn == MUSTARD) g.drawString("MUSTARD WON!", 180, 50);
           else if (turn == WHITE) g.drawString("WHITE WON!", 180, 50);
           else if (turn == PEACOCK) g.drawString("PEACOCK WON!", 200, 50);
           else if (turn == GREEN) g.drawString("GREEN WON!", 180, 50);
           else if (turn == PLUM) g.drawString("PLUM WON!", 200, 50);
       }
   }

    /**
     * The main program.
     *
     * @param    args    command line arguments (ignored)
     */
    public static void main(String [] args) {

        Clue content;

        if (args.length > 1) {
            System.out.println("Usage: java Clue delayTime");
            System.exit(0);
        }

        if (args.length == 1) {   
            try {
                delay = Integer.parseInt(args[0]);
            }
            catch (NumberFormatException e) {
                System.out.println("Command line arg must be an integer");
                System.exit(0);
            }
            content = new Clue(delay);
            if (delay >= 0) {
                JFrame window = new JFrame("Game of Clue");
                window.setContentPane(content);
                window.setSize(542,632);
                window.setLocation(100,100);
                window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
                window.setVisible(true);
            }
        }
        else {
            content = new Clue();
            JFrame window = new JFrame("Game of Clue");
            window.setContentPane(content);
            window.setSize(542,632);
            window.setLocation(100,100);
            window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
            window.setVisible(true);
        }
    }
}  // Clue
