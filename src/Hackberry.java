import java.util.ArrayList;
import java.util.HashMap;

/**
 * AI for user to play against.
 * Makes random moves from the first possible piece.
 *
 * TODO: handle promotion (either a queen or a knight, no need to check others)
 */

public class HackberryAI {
    private final int NUM_SQUARES = 8;
    private char side;
    private int DEPTH;
    private final int NUM_CANDIDATES = 5;


    public HackberryAI(char side, int depth){
        this.side = side;
        this.DEPTH = depth;
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
                to[r][c] = new Piece(r, c, from[r][c].getType(), from[r][c].getSide(), from[r][c].numMoves);
            }
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

    public void print(Piece[][] pieces){
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print("[" + pieces[j][i].getType() + (pieces[j][i].getType().length() == 2 ? " " : "   ") + "] ");
            System.out.println();
        }

        System.out.println();
    }

    private int numNodes = 0;
    public double minimax(int fromX, int fromY, int toX, int toY, Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                          boolean whiteTurn, int depth, double alpha, double beta){
        numNodes++;

        System.out.print("    Depth " + depth + ": Checking (" + fromX + ", " + fromY + ") to (" + toX + ", " + toY + ")");
        System.out.println(" with " + (whiteTurn ? "white" : "black") + " to move.");

        // Avoids changing piecesW and piecesB directly (they're needed for future iterations)
        HashMap<Piece, ArrayList<int[]>> piecesWCopy = null;
        HashMap<Piece, ArrayList<int[]>> piecesBCopy = null;

        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
        boolean[][] squaresControlledWCopy = new boolean[NUM_SQUARES][NUM_SQUARES];
        boolean[][] squaresControlledBCopy = new boolean[NUM_SQUARES][NUM_SQUARES];

        // Play the move specified
        setPieces(pieces, piecesCopy);
        piecesCopy[fromX][fromY].playMove(toX, toY, piecesCopy, new int[]{fromX, fromY});

        print(piecesCopy);


        //{
        piecesWCopy = BoardEval.reset(pieces, 'w');
        piecesBCopy = BoardEval.reset(pieces, 'b');

        piecesWCopy = BoardEval.getPossibleMovesW(squaresControlledWCopy, squaresControlledBCopy, piecesCopy, mostRecentPieceMov, piecesWCopy);
        piecesBCopy = BoardEval.getPossibleMovesB(squaresControlledWCopy, squaresControlledBCopy, piecesCopy, mostRecentPieceMov, piecesBCopy);

        piecesWCopy = BoardEval.removeIllegalMovesW(squaresControlledWCopy, squaresControlledBCopy, piecesCopy, mostRecentPieceMov, prevCoords, piecesWCopy, piecesBCopy);
        piecesBCopy = BoardEval.removeIllegalMovesB(squaresControlledWCopy, squaresControlledBCopy, piecesCopy, mostRecentPieceMov, prevCoords, piecesWCopy, piecesBCopy);

        BoardEval.checkControlledSquaresW(squaresControlledWCopy, squaresControlledBCopy, piecesCopy);
        BoardEval.checkControlledSquaresB(squaresControlledWCopy, squaresControlledBCopy, piecesCopy);

        piecesWCopy = BoardEval.cleanUpHashMap(piecesWCopy, 'w');
        piecesBCopy = BoardEval.cleanUpHashMap(piecesBCopy, 'b');

        BoardEval.givePieceScores(piecesWCopy, piecesBCopy);
        //}


        double score = BoardEval.boardScore(piecesWCopy, piecesBCopy);
        //double score = BoardEval.boardScore(piecesCopy);
        System.out.println("     This move was given score " + score);

        // Check if game is over and assign values
        if(BoardEval.whiteKingInCheck() && whiteTurn && BoardEval.possibleMovesW == 0) return Integer.MIN_VALUE + 10;
        else if(BoardEval.blackKingInCheck() && !whiteTurn && BoardEval.possibleMovesB == 0) return Integer.MAX_VALUE - 10;
        else if(!BoardEval.whiteKingInCheck() && BoardEval.possibleMovesW == 0 && whiteTurn) return 0;
        else if(!BoardEval.blackKingInCheck() && BoardEval.possibleMovesB == 0 && !whiteTurn) return 0;

        boolean breakout = false;
        int iter = 0;

        if(depth == 0)
            return score;
        else {
            if(whiteTurn){
                double val = Integer.MIN_VALUE;

                // Seeking the highest board score
                for(Piece p : piecesWCopy.keySet()){
                    for(int[] arr : piecesWCopy.get(p)){
                        val = Math.max(val, minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], piecesCopy, mostRecentPieceMov, prevCoords, !whiteTurn, depth - 1, alpha, beta));

                        iter++;

                        if(val > beta || iter > 4) {
                            breakout = true;
                            break;
                        }
                        alpha = Math.max(alpha, val);
                    }

                    if(breakout)
                        break;
                }

                return val;
            } else {
                double val = Integer.MAX_VALUE;

                // Seeking the lowest board score
                for(Piece p : piecesBCopy.keySet()){
                    for(int[] arr : piecesBCopy.get(p)){
                        val = Math.min(val, minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], piecesCopy, mostRecentPieceMov, prevCoords, !whiteTurn, depth - 1, alpha, beta));

                        iter++;

                        if(val < alpha || iter > 4) {
                            breakout = true;
                            break;
                        }
                        beta = Math.min(beta, val);
                    }

                    if(breakout)
                        break;
                }

                return val;
            }
        }
    }

    // Actual AI goes here
    // Find the 5 moves that offer the best position
    public void makeMove(HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB, Piece[][] pieces,
                         boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                         int[] mostRecentPieceMov, int[] prevCoords, boolean whiteTurn){
        //update(piecesW, piecesB, pieces, squaresControlledW, squaresControlledB, mostRecentPieceMov, prevCoords);

        numNodes = 0;

        /*if(this.stillInOpening()){
            //playOpening();
            return;
        } else {*/
            // [piece X, piece Y, goal square X, goal square Y]
            int[] best = new int[4];

            if(side == 'w'){
                double bestScore = Integer.MIN_VALUE;

                for(Piece p : piecesW.keySet()){
                    for(int arr[] : piecesW.get(p)){
                        double score = minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], pieces, mostRecentPieceMov, prevCoords, whiteTurn, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);

                        if(score > bestScore) {
                            bestScore = score;
                            best = new int[]{p.getGridX(), p.getGridY(), arr[0], arr[1]};
                        }
                    }
                }
            } else {
                double bestScore = Integer.MAX_VALUE;

                for(Piece p : piecesB.keySet()){
                    for(int arr[] : piecesB.get(p)){
                        System.out.println("Checking " + p + " to (" + arr[0] + ", " + arr[1] + ")");
                        double score = minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], pieces, mostRecentPieceMov, prevCoords, !whiteTurn, DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        System.out.println("Score: " + score);

                        if(score < bestScore) {
                            bestScore = score;
                            best = new int[]{p.getGridX(), p.getGridY(), arr[0], arr[1]};
                        }
                    }
                }
            }


            System.out.println("\n\nChosen move: " + pieces[best[0]][best[1]] + " to (" + best[2] + ", " + best[3] + ")");
            System.out.println("Searched " + numNodes + " nodes.");
            mostRecentPieceMov[0] = best[2];
            mostRecentPieceMov[1] = best[3];
            pieces[best[0]][best[1]].playMove(best[2], best[3], pieces, new int[]{best[0], best[1]});
        //}
    }


    public void update(HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB, Piece[][] pieces,
                       boolean[][] squaresControlledW, boolean[][] squaresControlledB, int[] mostRecentPieceMov, int[] prevCoords){
        piecesW = BoardEval.reset(pieces, 'w');
        piecesB = BoardEval.reset(pieces, 'b');

        piecesW = BoardEval.getPossibleMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesW);
        piecesB = BoardEval.getPossibleMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesB);

        piecesW = BoardEval.removeIllegalMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);
        piecesB = BoardEval.removeIllegalMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);

        BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces);
        BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces);

        piecesW = BoardEval.cleanUpHashMap(piecesW, 'w');
        piecesB = BoardEval.cleanUpHashMap(piecesB, 'b');

        BoardEval.givePieceScores(piecesW, piecesB);
    }
}
