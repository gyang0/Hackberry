
/** Milestones
 * Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 * Jan 8, 2023 - All legal moves are now recognized (hopefully), including: En passant, castling, checks, promotion, etc.
 * Jan 26, 2023 - Moved from 8x8 board search to piece HashMaps. All legal moves are recognized as well. ~500K -> ~65K iterations.
 * Feb 20, 2023 - AI makes random moves. Includes promotion, en passant, and castling.
 * Apr 9, 2023 - Took a small break, organized BoardEval.java better and got things working again.
 * **/


/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI is currently being implemented.
 *
 * TODO: Javadoc comments
 * TODO: Different side toggle
 * TODO: if two knights can move to the same square, then the notation should reflect that. (same for pawns, rooks, queens, bishops)
 * TODO: pawn values should increase with every step taken.
 * TODO: make the notation work with captures, castling, en passant, and promotions.
 * TODO: AI doesn't recognize captures as being valuable.
 *
 * TODO: weird board evaluation in-between mouse clicks.
 * TODO: checkmate & stalemate aren't recognized.
 *
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
