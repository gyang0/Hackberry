import java.util.ArrayList;
import java.util.HashMap;

/**
 * AI for user to play against.
 * Makes random moves from the first possible piece.
 */

public class HackberryAI {
    private char side;
    private int depth;
    private Piece[][] piecesCopy;

    private final int NUM_SQUARES = 8;

    // Constructors
    public HackberryAI(){
        this.side = 'b';
        this.depth = 0;
        piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
    }

    public HackberryAI(char side, int depth){
        this.side = side;
        this.depth = depth;

        piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                piecesCopy[i][j] = new Piece();
        }
    }

    public void promotePawn(Piece[][] pieces, int x, int y){
        int choice = (int)(Math.random() * 4);

        switch(choice){
            case 0:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wr" : "br", this.side, 0);
                break;
            case 1:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wn" : "bn", this.side, 0);
                break;
            case 2:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wb" : "bb", this.side, 0);
                break;
            case 3:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wq" : "bq", this.side, 0);
                break;
        }
    }

    /*
    public boolean stillInOpening(){
        for(int i = 0; i < OpeningBook.continuations.length; i++){
            boolean matches = false;
            for(int j = 0; j < PGN.length; j++)
        }
    }*/

    public double minimax(boolean[][] squaresControlledW, boolean[][] squaresControlledB, Piece[][] pieces,
                         int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesW,
                         HashMap<Piece, ArrayList<int[]>> piecesB, int[] prevCoords, int depth){
        /*if(depth == 0)
            return BoardEval.boardScore(...args...);
        else {
            if(whiteTurn){
                // Seeking highest board score
                for(Piece p : piecesW.keySet()){
                    for(int[] arr : piecesW.get(p)){
                        // Play that move, undo, etc.
                    }
                }
            } else {
                // Seeking lowest board score
            }
        }*/

        return 0.0;
    }

    /**
     * Populates an array of Pieces to the same values as another array specified. (copy)
     *
     * @param from - The array of Piece objects to copy
     * @param to - The array of piece objects to copy to.
     * **/
    public void setPieces(Piece[][] from, Piece[][] to){
        for(int r = 0; r < NUM_SQUARES; r++) {
            for (int c = 0; c < NUM_SQUARES; c++) {
                to[r][c].setPiece(r, c, from[r][c].getType(), from[r][c].getSide(), from[r][c].numMoves);
            }
        }
    }

    // Actual AI goes here
    // Find the 5 moves that offer the best position
    public void makeMove(boolean[][] squaresControlledW, boolean[][] squaresControlledB, Piece[][] pieces,
                         int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesW,
                         HashMap<Piece, ArrayList<int[]>> piecesB, int[] prevCoords){
        /*if(this.stillInOpening()){
            //playOpening();
            return;
        } else {
            // [piece X, piece Y, goal square X, goal square Y]
            int[] best = new int[4];

            if(side == 'w'){
                double bestScore = Integer.MIN_VALUE;

                for(Piece p : piecesW.keySet()){
                    for(int arr[] : piecesW.get(p)){
                        double score = minimax(...args...);
                        if(score > bestScore)
                            best = new int[]{p.getGridX(), p.getGridY(), arr[0], arr[1]};
                    }
                }
            } else {
                double bestScore = Integer.MAX_VALUE;

                for(Piece p : piecesB.keySet()){
                    for(int arr[] : piecesB.get(p)){
                        double score = minimax(...args...);
                        if(score < bestScore)
                            best = new int[]{p.getGridX(), p.getGridY(), arr[0], arr[1]};
                    }
                }
            }

            pieces[best[0]][best[1]].playMove(...args...);
        }*/

        /*for(Piece p : piecesB.keySet()){
            System.out.print(p + ": ");
            for(int arr[] : piecesB.get(p))
                System.out.print("(" + arr[0] + ", " + arr[1] + ") ");
            System.out.println();
        }*/

        for(Piece p : piecesB.keySet()){
            for(int[] arr : piecesB.get(p)){
                if(arr.length != 0) {
                    p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, prevCoords, true);
                    return;
                }
            }
        }

    }
}
