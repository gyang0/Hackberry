import java.util.ArrayList;
import java.util.HashMap;

/**
 * AI for user to play against.
 */

public class HackberryAI {
    private char side;
    private int depth;

    // Constructors
    public HackberryAI(){
        this.side = 'b';
        this.depth = 0;
    }

    public HackberryAI(char side, int depth){
        this.side = side;
        this.depth = depth;
    }

    // Something to evaluate the board state
    public double boardEval(HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Sum of the relative values for each piece
        double whiteScore = 0.0,
               blackScore = 0.0;

        for(Piece p : piecesW.keySet())
            whiteScore += p.getValue();

        for(Piece p : piecesB.keySet())
            blackScore += p.getValue();

        return whiteScore - blackScore;
    }

    // Minimax search goes here

    // Something to play the moves
    public void makeMove(Piece[][] pieces, HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB, int[] prevCoords){
        if(this.side == 'w'){

            // Choose a random move
            for(Piece p : piecesW.keySet()){
                if(piecesW.get(p).size() > 0){

                    int randIndex = (int)(Math.random() * piecesW.get(p).size());
                    int[] arr = piecesW.get(p).get(randIndex);

                    prevCoords = arr;

                    p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, prevCoords, true);
                    return;
                }
            }

        }

        else if(this.side == 'b'){

            // Choose a random move
            for(Piece p : piecesB.keySet()){
                if(piecesB.get(p).size() > 0){

                    int randIndex = (int)(Math.random() * piecesB.get(p).size());
                    int[] arr = piecesB.get(p).get(randIndex);

                    prevCoords = arr;

                    p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, prevCoords, true);
                    return;
                }
            }
        }

    }

}
