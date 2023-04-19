/** Milestones
 * Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 * Jan 8, 2023 - All legal moves are now recognized (hopefully), including: En passant, castling, checks, promotion, etc.
 * Jan 26, 2023 - Moved from 8x8 board search to piece HashMaps. All legal moves are recognized as well. ~500K -> ~65K iterations.
 * Feb 20, 2023 - AI makes random moves. Includes promotion, en passant, and castling.
 * Apr 9, 2023 - Took a break for other pursuits, organized BoardEval.java better and got things working again.
 * Apr 10, 2023 - AI is semi-working, but it's so slow it can only see 1 move in advance (even then it takes a few seconds).
 * Apr 11, 2023 - Purely materialistic AI can see 1-2 moves ahead, taking ~15 seconds per move.
 * Apr 17, 2023 - Switched to simpler array-based move generation. Less complicated than HashMap system but still as efficient.
 * Apr 19, 2023 - Switched from minimax to negamax with alpha-beta pruning. Program seems to make good decisions.
 * **/


/**
 * TODO: Different side toggle
 * TODO: give scores to pieces (pawn values increase as it gets closer to promotion)
 *
 * TODO: maintain a hashmap of FENs to cut down on board eval costs.
 * **/

import javax.swing.*;
import java.awt.*;

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 * Negamax algorithm from https://en.wikipedia.org/wiki/Negamax
 *
 * @author Gene Yang
 * @version April 19, 2023
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
