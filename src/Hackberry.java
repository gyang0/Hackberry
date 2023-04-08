
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
 * TODO: computer tries to castle out of check when it's still a check.
 * TODO: computer doesn't realize that a king can capture out of check.
 * TODO: weird board evaluation in-between mouse clicks
 * **/

/** Test cases to verify correctness
 * Puzzle #1: not using castling when in check
 * Puzzle #2: using pawn promotion to block a check
 */

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
