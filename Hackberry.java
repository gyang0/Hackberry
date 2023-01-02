/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI will be implemented later.
 *
 * TODO: Blank square click + piece click shouldn't remove pieces
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
