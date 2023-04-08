import java.util.ArrayList;
import java.util.HashMap;

public class BoardEval {
    private static final int NUM_SQUARES = 8;

    private static boolean blackKingInCheck = false;
    private static boolean whiteKingInCheck = false;

    // Number of possible moves white or black can make.
    public static int possibleMovesW = 0;
    public static int possibleMovesB = 0;
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


    /**
     * Populates an array of Pieces to the same values as another array specified. (copy)
     *
     * @param from - The array of Piece objects to copy
     * @param to - The array of piece objects to copy to.
     * **/
    public static void setPieces(Piece[][] from, Piece[][] to){
        for(int r = 0; r < NUM_SQUARES; r++) {
            for (int c = 0; c < NUM_SQUARES; c++) {
                to[r][c] = new Piece(r, c, from[r][c].getType(), from[r][c].getSide(), from[r][c].numMoves);
            }
        }
    }



    /**
     * Checks the squares controlled by the white side.
     * **/
    public static void checkControlledSquaresW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                               Piece[][] pieces) {
        // Reset
        for (int i = 0; i < NUM_SQUARES; i++){
            for (int j = 0; j < NUM_SQUARES; j++)
                squaresControlledW[i][j] = false;
        }

        blackKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ', 0);

        // For every white piece
        for (int i = 0; i < NUM_SQUARES; i++) {
            for(int j = 0; j < NUM_SQUARES; j++) {
                if(pieces[i][j].getSide() != 'w')
                    continue;

                // For every square on the board
                for (int pos1 = 0; pos1 < NUM_SQUARES; pos1++) {
                    for (int pos2 = 0; pos2 < NUM_SQUARES; pos2++) {

                        // Having control over that square:
                        // For testing purposes, we set the piece of an opposite side at that square.
                        // Then we see if that piece can be captured.
                        prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide(), pieces[pos1][pos2].numMoves);
                        pieces[pos1][pos2].setPiece(pos1, pos2, "bp", 'b', 0);

                        // Can make a capture (if necessary) at that position.
                        if (pieces[i][j].legalMove(pos1, pos2, pieces, new int[]{pos1, pos2}, squaresControlledW, squaresControlledB)) {
                            squaresControlledW[pos1][pos2] = true;

                            // If the piece at that position is a king, it's in check.
                            if (prev.getType().equals("bk"))
                                blackKingInCheck = true;
                        }

                        // Reset
                        pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide(), prev.numMoves);
                    }
                }
            }
        }

    }

    public static HashMap<Piece, ArrayList<int[]>> reset(Piece[][] pieces, char side){
        HashMap<Piece, ArrayList<int[]>> map = new HashMap<>();

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                if(pieces[i][j].getSide() == side)
                    map.put(new Piece(pieces[i][j]), new ArrayList<int[]>());
        }

        return map;
    }

    /**
     * Checks the squares controlled by the black side.
     * **/
    public static void checkControlledSquaresB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                               Piece[][] pieces) {
        // Reset
        for (int i = 0; i < NUM_SQUARES; i++) {
            for (int j = 0; j < NUM_SQUARES; j++)
                squaresControlledB[i][j] = false;
        }

        whiteKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ', 0);

        // For every black piece
        for(int i = 0; i < NUM_SQUARES; i++) {
            for(int j = 0; j < NUM_SQUARES; j++) {
                if(pieces[i][j].getSide() != 'b')
                    continue;

                // For every square on the board
                for (int pos1 = 0; pos1 < NUM_SQUARES; pos1++) {
                    for (int pos2 = 0; pos2 < NUM_SQUARES; pos2++) {

                        // Having control over that square:
                        // For testing purposes, we set the piece of an opposite side at that square.
                        // Then we see if that piece can be captured.
                        prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide(), pieces[pos1][pos2].numMoves);
                        pieces[pos1][pos2].setPiece(pos1, pos2, "wp", 'w', 0);

                        // Can make a capture (if necessary) at that position.
                        if (pieces[i][j].legalMove(pos1, pos2, pieces, new int[]{pos1, pos2}, squaresControlledW, squaresControlledB)) {
                            squaresControlledB[pos1][pos2] = true;

                            // If the piece at that position is a king, it's in check.
                            if (prev.getType().equals("wk"))
                                whiteKingInCheck = true;
                        }

                        pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide(), prev.numMoves);
                    }
                }
            }
        }

    }

    /**
     * Updates the positions every white piece can move to.
     * **/
    public static HashMap<Piece, ArrayList<int[]>> getPossibleMovesW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                         Piece[][] pieces, int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesW){
        // Reset
        HashMap<Piece, ArrayList<int[]>> copy = new HashMap<>();

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

            copy.put(p, possibleMoves);
        }

        return copy;
    }

    /**
     * Updates the positions every black piece can move to.
     * **/
    public static HashMap<Piece, ArrayList<int[]>> getPossibleMovesB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                         Piece[][] pieces, int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Reset
        HashMap<Piece, ArrayList<int[]>> copy = new HashMap<>();

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

            copy.put(new Piece(p), possibleMoves);
        }

        return copy;
    }

    /**
     * Removes the white pieces' moves that result in the white king being put in check.
     * **/
    public static HashMap<Piece, ArrayList<int[]>> removeIllegalMovesW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                           Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                                           HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Setup
        HashMap<Piece, ArrayList<int[]>> copy = new HashMap<>();
        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(Piece p : piecesW.keySet()){
            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesW.get(p)){
                // Copy of board state before move
                setPieces(pieces, piecesCopy);

                piecesCopy[p.getGridX()][p.getGridY()].playMove(arr[0], arr[1], piecesCopy, piecesW, piecesB, new int[]{p.getGridX(), p.getGridY()});

                // Move piece to square and see if it results in check.
                //piecesCopy[p.getGridX()][p.getGridY()] = new Piece();
                //piecesCopy[arr[0]][arr[1]] = new Piece(p);


                //p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, new int[]{p.getGridX(), p.getGridY()}, false);
                BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, piecesCopy);

                //System.out.println(p + " move to (" + arr[0] + ", " + arr[1] + " resulted in a check to the white king: " + whiteKingInCheck);

                if(!whiteKingInCheck)
                    moves.add(arr);
            }

            copy.put(p, moves);
        }

        return copy;
    }

    /**
     * Removes the black pieces' moves that result in the black king being put in check.
     * **/
    public static HashMap<Piece, ArrayList<int[]>> removeIllegalMovesB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                           Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                                           HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Setup
        HashMap<Piece, ArrayList<int[]>> copy = new HashMap<>();
        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(Piece p : piecesB.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesB.get(p)){
                // Copy array
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                piecesCopy[p.getGridX()][p.getGridY()].playMove(arr[0], arr[1], piecesCopy, piecesW, piecesB, new int[]{p.getGridX(), p.getGridY()});
                //piecesCopy[p.getGridX()][p.getGridY()] = new Piece();
                //piecesCopy[arr[0]][arr[1]] = new Piece(p);

                BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, piecesCopy);

                /*System.out.println();
                for(int i = 0; i < NUM_SQUARES; i++){
                    for(int j = 0; j < NUM_SQUARES; j++)
                        System.out.print("[" + pieces[j][i].getType() + (pieces[j][i].getType().length() == 2 ? "" : "  ") + "]");
                    System.out.println();
                }
                System.out.println();

                System.out.println(p + " move to (" + arr[0] + ", " + arr[1] + " resulted in a check to the black king: " + blackKingInCheck);*/

                if(!blackKingInCheck)
                    moves.add(arr);

                /*System.out.println();
                for(int i = 0; i < NUM_SQUARES; i++){
                    for(int j = 0; j < NUM_SQUARES; j++)
                        System.out.print("[" + pieces[j][i].getType() + (pieces[j][i].getType().length() == 2 ? "" : "  ") + "]");
                    System.out.println();
                }
                System.out.println();*/
            }

            copy.put(p, moves);
        }

        return copy;
    }

    /**
     * Gives a score to each piece, depending on its base score, mobility, and position.
     */
    public static void givePieceScores(HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
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

    public static boolean gameOver(){
        return BoardEval.gameOver;
    }

    public static boolean whiteKingInCheck(){
        return BoardEval.whiteKingInCheck;
    }

    public static boolean blackKingInCheck(){
        return BoardEval.blackKingInCheck;
    }


    // Something to evaluate the board state
    public static double boardScore(HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Sum of the relative values for each piece
        double whiteScore = 0.0,
                blackScore = 0.0;

        for(Piece p : piecesW.keySet())
            whiteScore += p.getValue();

        for(Piece p : piecesB.keySet())
            blackScore += p.getValue();

        return whiteScore - blackScore;
    }

    public static HashMap<Piece, ArrayList<int[]>> cleanUpHashMap(HashMap<Piece, ArrayList<int[]>> map, char side){
        HashMap<Piece, ArrayList<int[]>> copy = new HashMap<>();
        for(Piece p : map.keySet()){
            if(!p.getType().equals("") && p.getSide() == side)
                copy.put(new Piece(p), new ArrayList<>(map.get(p)));
        }

        return copy;
    }
}
