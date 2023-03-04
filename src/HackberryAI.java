import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


/**
 * AI for user to play against.
 *
 * Current makes random moves from the first possible piece.
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
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wr" : "br", this.side);
                break;
            case 1:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wn" : "bn", this.side);
                break;
            case 2:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wb" : "bb", this.side);
                break;
            case 3:
                pieces[x][y].setPiece(x, y, this.side == 'w' ? "wq" : "bq", this.side);
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

    /**
     * Populates an array of Pieces to the same values as another array specified. (copy)
     *
     * @param from - The array of Piece objects to copy
     * @param to - The array of piece objects to copy to.
     * **/
    public void setPieces(Piece[][] from, Piece[][] to){
        for(int r = 0; r < NUM_SQUARES; r++) {
            for (int c = 0; c < NUM_SQUARES; c++) {
                to[r][c].setPiece(r, c, from[r][c].getType(), from[r][c].getSide());
            }
        }
    }

    // Actual AI goes here
    // Find the 5 moves that offer the best position
    public void makeMove(boolean[][] squaresControlledW, boolean[][] squaresControlledB, Piece[][] pieces, int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB, int[] prevCoords){
        /*if(this.stillInOpening()){
            //playOpening();
            return;
        }*/

        // Sort potential moves, each with its x and y coordinates.
        ArrayList<CheckMoves> bestMoves = new ArrayList<CheckMoves>();

        if(this.side == 'w'){
            // Choose a random move
            for(Piece p : piecesW.keySet()){
                if(piecesW.get(p).size() > 0){

                    int randIndex = (int)(Math.random() * piecesW.get(p).size());
                    int[] arr = piecesW.get(p).get(randIndex);

                    prevCoords = arr;


                    Notation.updateMoves(arr[0], arr[1], pieces[arr[0]][arr[1]]);
                    p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, prevCoords, true);

                    // Promotion
                    if(arr[1] == 0 && pieces[arr[0]][arr[1]].getType().equals("wp")){
                        Notation.updateMoves(arr[0], arr[1], pieces[arr[0]][arr[1]]);
                        promotePawn(pieces, arr[0], arr[1]);
                        return;
                    }

                    mostRecentPieceMov[0] = arr[0];
                    mostRecentPieceMov[1] = arr[1];

                    return;
                }
            }

        }

        else if(this.side == 'b'){
            // Choose a random move
            for(Piece p : piecesB.keySet()){
                if(piecesB.get(p).size() > 0){

                    //setPieces(pieces, piecesCopy);

                    for(int arr[] : piecesB.get(p)){
                        // Try moving there
                        // Use the copy of the array to avoid ConcurrentModificationException.
                        p.playMove(arr[0], arr[1], piecesCopy, piecesW, piecesB, prevCoords, false);

                        BoardEval.setBoard(squaresControlledW, squaresControlledB, piecesW, piecesB,
                                           pieces, piecesCopy, mostRecentPieceMov, prevCoords, true);
                        BoardEval.updateControlledSquares();
                        double score = BoardEval.boardScore();
                        System.out.println("Board score for " + p + ": " + score);
                        bestMoves.add(new CheckMoves(score, arr, p));

                        //setPieces(piecesCopy, pieces);
                    }
                }
            }

            // Sort to get the best moves
            Collections.sort(bestMoves);
            CheckMoves best = bestMoves.get(0);
            //System.out.println(best.p);
            //System.out.println(best.position[0] + " " + best.position[1]);
            for(CheckMoves c : bestMoves)
                System.out.println(c.p + "  ->  " + c.boardScore);

            // Promotion
            if(best.position[1] == NUM_SQUARES - 1 && best.p.getType().equals("bp")){
                Notation.updateMoves(best.position[0], best.position[1], best.p);
                promotePawn(pieces, best.position[0], best.position[1]);
                return;
            } else {
                Notation.updateMoves(best.position[0], best.position[1], best.p);
                prevCoords[0] = best.p.getGridX();
                prevCoords[1] = best.p.getGridY();
                best.p.playMove(best.position[0], best.position[1], pieces, piecesW, piecesB, prevCoords, true);
            }

            mostRecentPieceMov[0] = best.position[0];
            mostRecentPieceMov[1] = best.position[1];
        }

    }

    class CheckMoves implements Comparable<CheckMoves> {
        public double boardScore;
        public int[] position;
        public Piece p;

        public CheckMoves(double boardScore, int[] position, Piece p){
            this.boardScore = boardScore;
            this.position = new int[]{position[0], position[1]};
            this.p = p;
        }

        /**
         * Decreasing order, so greater -> least.
         * @param other the object to be compared.
         * @return
         */
        @Override
        public int compareTo(CheckMoves other) {
            return Double.compare(other.boardScore, this.boardScore);
        }
    };

}
