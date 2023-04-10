import java.util.ArrayList;
import java.util.HashMap;

/**
 * AI for user to play against.
 * Makes random moves from the first possible piece.
 *
 * TODO: handle promotion
 */

public class HackberryAI {
    private char side;
    private int DEPTH;
    private Piece[][] piecesCopy;

    private final int NUM_SQUARES = 8;

    private boolean[][] squaresControlledW;

    private boolean[][] squaresControlledB;
    private HashMap<Piece, ArrayList<int[]>> piecesW;
    private HashMap<Piece, ArrayList<int[]>> piecesB;

    private int[] mostRecentPieceMov;
    private int[] prevCoords;


    public HackberryAI(char side, int depth){
        this.side = side;
        this.DEPTH = depth;

        squaresControlledW = new boolean[NUM_SQUARES][NUM_SQUARES];
        squaresControlledB = new boolean[NUM_SQUARES][NUM_SQUARES];

        piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++) {
                piecesCopy[i][j] = new Piece();
                squaresControlledW[i][j] = false;
                squaresControlledB[i][j] = false;
            }
        }
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

    public double minimax(int fromX, int fromY, int toX, int toY, Piece[][] pieces, int[] mostRecentPieceMov, boolean whiteTurn, int depth){
        System.out.println("Depth " + depth + ": Checking (" + fromX + ", " + fromY + ") to (" + toX + ", " + toY + ")");

        // Play the move specified
        setPieces(pieces, piecesCopy);
        piecesCopy[fromX][fromY].playMove(toX, toY, piecesCopy, new int[]{fromX, fromY});

        // Update stuff
        update(pieces, mostRecentPieceMov);

        if(depth == 0)
            return BoardEval.boardScore(piecesCopy);
        else {
            if(whiteTurn){
                double val = Integer.MIN_VALUE;

                // Seeking the highest board score
                for(Piece p : piecesW.keySet()){
                    for(int[] arr : piecesW.get(p)){
                        val = Math.max(val, minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], piecesCopy, mostRecentPieceMov, !whiteTurn, depth - 1));
                    }
                }

                return val;
            } else {
                double val = Integer.MAX_VALUE;

                // Seeking the lowest board score
                for(Piece p : piecesB.keySet()){
                    for(int[] arr : piecesB.get(p)){
                        val = Math.max(val, minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], piecesCopy, mostRecentPieceMov, !whiteTurn, depth - 1));
                    }
                }

                return val;
            }
        }
    }

    // Actual AI goes here
    // Find the 5 moves that offer the best position
    public void makeMove(Piece[][] pieces, int[] mostRecentPieceMov, boolean whiteTurn){
        update(pieces, mostRecentPieceMov);

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
                        double score = minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], pieces, mostRecentPieceMov, whiteTurn, DEPTH);

                        if(score > bestScore)
                            best = new int[]{p.getGridX(), p.getGridY(), arr[0], arr[1]};
                    }
                }
            } else {
                double bestScore = Integer.MAX_VALUE;

                for(Piece p : piecesB.keySet()){
                    for(int arr[] : piecesB.get(p)){
                        double score = minimax(p.getGridX(), p.getGridY(), arr[0], arr[1], pieces, mostRecentPieceMov, whiteTurn, DEPTH);
                        if(score < bestScore)
                            best = new int[]{p.getGridX(), p.getGridY(), arr[0], arr[1]};
                    }
                }
            }

            mostRecentPieceMov[0] = best[0];
            mostRecentPieceMov[1] = best[1];
            pieces[best[0]][best[1]].playMove(best[2], best[3], pieces, new int[]{best[0], best[1]});
        //}
    }


    public void update(Piece[][] pieces, int[] mostRecentPieceMov){
        BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces);
        BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces);

        piecesW = BoardEval.reset(pieces, 'w');
        piecesB = BoardEval.reset(pieces, 'b');

        piecesW = BoardEval.getPossibleMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesW);
        piecesB = BoardEval.getPossibleMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesB);

        piecesW = BoardEval.removeIllegalMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);
        piecesB = BoardEval.removeIllegalMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);

        BoardEval.givePieceScores(piecesW, piecesB);
        BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces);
        BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces);
        piecesW = BoardEval.cleanUpHashMap(piecesW, 'w');
        piecesB = BoardEval.cleanUpHashMap(piecesB, 'b');
    }
}
