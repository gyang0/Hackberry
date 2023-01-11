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
 *
 * **/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 *
 * @author Gene Yang
 * @version Jan. 11, 2023
 **/


public class Hackberry {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
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
