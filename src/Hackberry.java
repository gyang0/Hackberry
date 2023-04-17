/** Schedule
 * Feb 5 - AI follows opening library before making random moves
 * Feb 25 - AI sees one move ahead
 * Mar 5 - AI sees five moves ahead
 * Mar 10 - Option to switch sides
 * Mar 15 - Store games in a file
 * Mar 30 - Optimizations
 *
 * Apr 12 - Working AI for depth 1
 * Apr 15 - Switch to new system of move search (3 candidate moves -> explore those)
 * Apr 17 - Working AI for depth 4
 * **/

/** Milestones
 * Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 * Jan 8, 2023 - All legal moves are now recognized (hopefully), including: En passant, castling, checks, promotion, etc.
 * Jan 26, 2023 - Moved from 8x8 board search to piece HashMaps. All legal moves are recognized as well. ~500K -> ~65K iterations.
 * Feb 20, 2023 - AI makes random moves. Includes promotion, en passant, and castling.
 * Apr 9, 2023 - Took a break, organized BoardEval.java better and got things working again.
 * Apr 10, 2023 - AI is semi-working, but it's so slow it can only see 1 move in advance (even then it takes a few seconds).
 * Apr 11, 2023 - Purely materialistic AI can see 1-2 moves ahead, taking ~15 seconds per move.
 * Apr 17, 2023 - Switched to simpler array-based move generation. Less complicated than HashMap system but still as efficient.
 * **/


/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI is currently being implemented.
 *
 * TODO: Javadoc comments
 * TODO: Different side toggle
 * TODO: pawn values should increase with every step taken.
 * TODO: piece values change depending on position
 * **/

import javax.swing.*;
import java.awt.*;

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 *
 * @author Gene Yang
 * @version April 8, 2023
 **/

public class Hackberry {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;
    private Board board;

    public void run(){
        JFrame window = new JFrame();
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        board = new Board();
        window.add(board, BorderLayout.CENTER);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        new Hackberry().run();
    }
}
