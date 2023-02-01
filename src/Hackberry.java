
/** Milestones
 * Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 * Jan 8, 2023 - All legal moves are now recognized (hopefully), including: En passant, castling, checks, promotion, etc.
 * Jan 26, 2023 - Optimizing to program is done. All legal moves are recognized as well. ~500K -> ~65K iterations.
 * **/


/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI is currently being implemented.
 *
 * TODO: Javadoc comments
 * TODO: Different side toggle
 * TODO: if two knights can move to the same square, then the notation should reflect that.
 * TODO: 50-move rule (no pawn moves or captures), stalemate, checkmate, draw by insufficient material
 * TODO: threefold repetition (this should check the HashMaps, not the board)
 * TODO: Notation should only be updated after move is confirmed to be legal.
 * TODO: pawn values should increase with every step taken.
 * TODO: bugfix, kings can capture each other???
 * TODO: test castling on AI
 *
 * Note to self - how about assigning a value to each square depending on how valuable it is? Then compare the score of
 * the squares controlled for both, and use minimax on that.
 * Program should also keep track of how many pieces on one side control a specific square, then determine a winner.
 * **/

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 *
 * @author Gene Yang
 * @version Jan. 29, 2023
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
