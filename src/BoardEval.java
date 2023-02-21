import java.util.ArrayList;
import java.util.HashMap;

public class BoardEval {
    private static final int NUM_SQUARES = 8;
    private static boolean[][] squaresControlledW;
    private static boolean[][] squaresControlledB;
    private static Piece[][] pieces;
    private static Piece[][] piecesCopy;
    private static int[] mostRecentPieceMov;
    private static int[] prevCoords;

    private static boolean blackKingInCheck = false;
    private static boolean whiteKingInCheck = false;
    private static HashMap<Piece, ArrayList<int[]>> piecesW;
    private static HashMap<Piece, ArrayList<int[]>> piecesB;

    // Number of possible moves white or black can make.
    private static int possibleMovesW = 0;
    private static int possibleMovesB = 0;
    private static boolean myTurn = false;
    private static boolean gameOver = false;


    // Gives a score to each board (center squares > edge squares).
    private static int boardPositionValues[][] = {
            {1, 1, 1, 1, 1, 1, 1, 1},
            {1, 3, 3, 3, 3, 3, 3, 1},
            {1, 3, 4, 4, 4, 4, 3, 1},
            {1, 3, 4, 5, 5, 4, 3, 1},
            {1, 3, 4, 5, 5, 4, 3, 1},
            {1, 3, 4, 4, 4, 4, 3, 1},
            {1, 3, 3, 3, 3, 3, 3, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    };

    public static void setBoard(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB,
                                Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords, boolean myTurn){
        BoardEval.squaresControlledW = squaresControlledW;
        BoardEval.squaresControlledB = squaresControlledB;

        BoardEval.piecesW = piecesW;
        BoardEval.piecesB = piecesB;

        BoardEval.pieces = pieces;
        BoardEval.mostRecentPieceMov = mostRecentPieceMov;
        BoardEval.prevCoords = prevCoords;

        BoardEval.myTurn = myTurn;

        piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
    }


    /**
     * Populates an array of Pieces to the same values as another array specified. (copy)
     *
     * @param from - The array of Piece objects to copy
     * @param to - The array of piece objects to copy to.
     * **/
    public static void setPieces(Piece[][] from, Piece[][] to){
        for(int r = 0; r < NUM_SQUARES; r++) {
            for (int c = 0; c < NUM_SQUARES; c++) {
                to[r][c] = new Piece(r, c, from[r][c].getType(), from[r][c].getSide());
            }
        }
    }

    /**
     * Checks the squares controlled by the white side.
     * **/
    public static void checkControlledSquaresW(){
        // Reset
        for(int i = 0; i < NUM_SQUARES; i++)
            for(int j = 0; j < NUM_SQUARES; j++)
                squaresControlledW[i][j] = false;

        blackKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ');
        int[] mostRecentPieceMovCopy = new int[2];

        // For every black piece
        for(Piece p : piecesW.keySet()){
            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){

                    // Having control over that square:
                    // For testing purposes, we set the piece of an opposite side at that square.
                    // Then we see if that piece can be captured.
                    prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide());
                    pieces[pos1][pos2].setPiece(pos1, pos2, "bp", 'b');

                    // Copy of most recent piece's move
                    mostRecentPieceMovCopy[0] = pos1;
                    mostRecentPieceMovCopy[1] = pos2;

                    // Can make a capture (if necessary) at that position.
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMovCopy, squaresControlledW, squaresControlledB)){
                        squaresControlledW[pos1][pos2] = true;

                        // If the piece at that position is a king, it's in check.
                        if(prev.getType().equals("bk"))
                            blackKingInCheck = true;
                    }

                    // Reset
                    pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide());
                }
            }
        }

    }

    /**
     * Checks the squares controlled by the black side.
     * **/
    public static void checkControlledSquaresB(){
        // Reset
        for(int i = 0; i < NUM_SQUARES; i++)
            for(int j = 0; j < NUM_SQUARES; j++)
                squaresControlledB[i][j] = false;

        whiteKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ');
        int[] mostRecentPieceMovCopy = new int[2];

        // For every black piece
        for(Piece p : piecesB.keySet()){
            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){

                    // Having control over that square:
                    // For testing purposes, we set the piece of an opposite side at that square.
                    // Then we see if that piece can be captured.
                    prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide());
                    pieces[pos1][pos2].setPiece(pos1, pos2, "wp", 'w');

                    // Copy of most recent piece's move
                    mostRecentPieceMovCopy[0] = pos1;
                    mostRecentPieceMovCopy[1] = pos2;

                    // Can make a capture (if necessary) at that position.
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        squaresControlledB[pos1][pos2] = true;

                        // If the piece at that position is a king, it's in check.
                        if(prev.getType().equals("wk"))
                            whiteKingInCheck = true;
                    }

                    pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide());
                }
            }
        }

    }

    /**
     * Updates the positions every white piece can move to.
     * **/
    public static void getPossibleMovesW(){
        // Reset
        piecesW.replaceAll((p, v) -> new ArrayList<int[]>());

        // For every black piece
        for(Piece p : piecesW.keySet()){
            ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){
                    // Can move to that square
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        possibleMoves.add(new int[]{pos1,pos2});
                    }
                }
            }

            piecesW.put(p, possibleMoves);
        }
    }

    /**
     * Updates the positions every black piece can move to.
     * **/
    public static void getPossibleMovesB(){
        // Reset
        piecesB.replaceAll((p, v) -> new ArrayList<int[]>());

        // For every black piece
        for(Piece p : piecesB.keySet()){
            ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){
                    // Can move to that square
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        possibleMoves.add(new int[]{pos1,pos2});
                    }
                }
            }

            piecesB.put(p, possibleMoves);
        }
    }

    /**
     * Removes the white pieces' moves that result in the white king being put in check.
     * **/
    public static void removeIllegalMovesW(){
        for(Piece p : piecesW.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesW.get(p)){
                // Copy of board state before move
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, prevCoords, false);
                BoardEval.checkControlledSquaresB();

                if(!whiteKingInCheck)
                    moves.add(arr);

                setPieces(piecesCopy, pieces);
            }

            piecesW.put(p, moves);
        }
    }

    /**
     * Removes the black pieces' moves that result in the black king being put in check.
     * **/
    public static void removeIllegalMovesB(){
        for(Piece p : piecesB.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesB.get(p)){
                // Copy array
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, prevCoords, false);
                BoardEval.checkControlledSquaresW();

                if(!blackKingInCheck)
                    moves.add(arr);

                setPieces(piecesCopy, pieces);
            }

            piecesB.put(p, moves);
        }
    }

    /**
     * Gives a score to each piece, depending on its base score, mobility, and position.
     */
    public static void givePieceScores(){
        for(Piece p : piecesW.keySet()){
            double baseScore = p.getBaseValue() * Piece.BASE_VAL_WEIGHT;
            double mobilityScore = (piecesW.get(p).size()) * Piece.MOBILITY_WEIGHT;
            double positionScore = boardPositionValues[p.getGridX()][p.getGridY()] * Piece.POSITION_WEIGHT;
            p.setValue(baseScore + mobilityScore + positionScore);

            possibleMovesW += piecesW.get(p).size();
        }

        for(Piece p : piecesB.keySet()){
            double baseScore = p.getBaseValue() * Piece.BASE_VAL_WEIGHT;
            double mobilityScore = (piecesB.get(p).size()) * Piece.MOBILITY_WEIGHT;
            double positionScore = boardPositionValues[p.getGridX()][p.getGridY()] * Piece.POSITION_WEIGHT;
            p.setValue(baseScore + mobilityScore + positionScore);

            possibleMovesB += piecesB.get(p).size();
        }
    }

    /**
     * Combined usage of checking possible squares and getting possible moves for all sides.
     * **/
    public static void updateControlledSquares(){
        BoardEval.checkControlledSquaresW();
        BoardEval.checkControlledSquaresB();

        // TODO: Bug here, removes a first-rank pawn somehow.
        BoardEval.getPossibleMovesW();
        BoardEval.getPossibleMovesB();

        BoardEval.removeIllegalMovesW();
        BoardEval.removeIllegalMovesB();

        BoardEval.givePieceScores();

        BoardEval.cleanUpHashMapW();
        BoardEval.cleanUpHashMapB();

        if(possibleMovesW == 0 && myTurn){
            BoardEval.gameOver = true;
        }

        if(possibleMovesB == 0 && !myTurn){
            BoardEval.gameOver = true;
        }
    }

    public static boolean gameOver(){
        return BoardEval.gameOver;
    }

    public static void cleanUpHashMapW(){
        ArrayList<Piece> toDelete = new ArrayList<Piece>();

        for(Piece p : piecesW.keySet())
            if(p.getSide() != 'w' || p.getType().equals(""))
                toDelete.add(p);

        for(Piece p : toDelete)
            piecesW.remove(p);
    }

    public static void cleanUpHashMapB(){
        ArrayList<Piece> toDelete = new ArrayList<Piece>();

        for(Piece p : piecesB.keySet())
            if(p.getSide() != 'b' || p.getType().equals(""))
                toDelete.add(p);

        for(Piece p : toDelete)
            piecesB.remove(p);
    }
}

