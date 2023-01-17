/** Milestones
 * Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 * Jan 8, 2023 - All legal moves are now recognized (hopefully), including: En passant, castling, checks, promotion, etc.
 * **/

/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI will be implemented later.
 *
 * TODO: Javadoc comments
 * TODO: Different side toggle
 * TODO: save games to PGN file format
 * TODO: if two knights can move to the same square, then the notation should reflect that.
 * TODO: all (or at least most) illegal moves should be prohibited in the this.canMoveTo method.
 * TODO: illegal moves shouldn't be highlighted squares.
 *
 * TODO: Note that my HashMap of pieces should never delete a piece unless it's captured. Instead, it should set that piece.
 *
 * I'll need to have a separate method or something, that goes through every move in the HashMap and tries it out.
 * Then if it causes the king to get in check, I'll make it not possible.
 * Remember: in this method I'm tampering with blackKingInCheck and whiteKingInCheck directly.
 * So I'll need to be careful about what my final value is for those.
 * If it was false initially, it should be false after that too.
 * It should also be reset for every iteration.
 * **/

import javax.swing.*;
import java.awt.*;

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 *
 * @author Gene Yang
 * @version Jan. 7, 2023
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
