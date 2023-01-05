/** Milestones
 * Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 *
 * **/


/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI will be implemented later.
 *
 * TODO: En passant
 * TODO: Check for attacked king when castling.
 * TODO: Promotion options
 * TODO: Javadoc comments
 * TODO: clicking king after promoting pawn results in a bug.
 *
 * Last step: some optimizations (searching the entire board gets costly pretty quick)
 * **/

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 *
 **/


public class Hackberry {
    private InitChessboard chessboard;

    public void run(){
        chessboard = new InitChessboard();
    }

    public static void main(String[] args) {
        new Hackberry().run();
    }
}
