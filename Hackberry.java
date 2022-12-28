/**
 * This is where most of the stuff will take place.
 * All methods will be combined to set turns, make moves, etc.
 *
 * AI will be implemented later.
 * **/


public class Hackberry {
    private InitChessboard chessboard;

    public void run(){
        chessboard = new InitChessboard();
    }

    public static void main(String[] args) {
        new Hackberry().run();
    }
}
