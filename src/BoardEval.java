import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BoardEval {
    private static final int NUM_SQUARES = 8;
    private static boolean blackKingInCheck = false;
    private static boolean whiteKingInCheck = false;

    private static int possibleMovesW;
    private static int possibleMovesB;
    private static boolean gameOver = false;

    // Gives a score to each board (center squares > edge squares).
    private static final int[][] boardPositionValues = {
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
                to[r][c] = new Piece(r, c, from[r][c].getType(), from[r][c].getSide());
            }
        }
    }

    /**
     * Checks the squares controlled by the white side.
     * **/
    public static void checkControlledSquares(char side, boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                              HashMap<Piece, ArrayList<int[]>> piecesMap, Piece[][] pieces) {
        // Reset
        for (int i = 0; i < NUM_SQUARES; i++) {
            for (int j = 0; j < NUM_SQUARES; j++){
                if(side == 'w') squaresControlledW[i][j] = false;
                else squaresControlledB[i][j] = false;
            }
        }

        // Reset for testing
        blackKingInCheck = false;
        whiteKingInCheck = false;

        Piece prev = new Piece(0, 0, "", ' ');
        int[] mostRecentPieceMovCopy = new int[2];

        // For every piece of the current side
        for(Piece p : piecesMap.keySet()){
            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){

                    // Having control over that square:
                    // For testing purposes, we set the piece of an opposite side at that square.
                    // Then we see if that piece can be captured.
                    prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide());
                    pieces[pos1][pos2].setPiece(pos1, pos2, side == 'w' ? "bp" : "wp", side == 'w' ? 'b' : 'w');

                    // Copy of most recent piece's move
                    mostRecentPieceMovCopy[0] = pos1;
                    mostRecentPieceMovCopy[1] = pos2;

                    // Can make a capture (if necessary) at that position.
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMovCopy, squaresControlledW, squaresControlledB)){
                        if(side == 'w') squaresControlledW[pos1][pos2] = true;
                        else squaresControlledB[pos1][pos2] = true;

                        // If the piece at that position is a king, it's in check.
                        if(prev.getType().equals("bk") && side == 'w') blackKingInCheck = true;
                        else if(prev.getType().equals("wk") && side == 'b') whiteKingInCheck = true;
                    }

                    // Reset
                    pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide());
                }
            }
        }
    }

    /**
     * Updates the positions every white piece can move to.
     * **/
    public static void getPossibleMoves(HashMap<Piece, ArrayList<int[]>> pieceMap, Piece[][] pieces, int[] mostRecentPieceMov,
                                         boolean[][] squaresControlledW, boolean[][] squaresControlledB){
        // Copy to avoid ConCurrentModificationException
        HashMap<Piece, ArrayList<int[]>> ans = new HashMap<>(pieceMap);

        // Reset
        pieceMap.replaceAll((p, v) -> new ArrayList<int[]>());

        // For every piece
        for(Piece p : ans.keySet()){
            ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){
                    // Can move to that square
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        possibleMoves.add(new int[]{pos1, pos2});
                    }
                }
            }

            pieceMap.put(p, possibleMoves);
        }
    }

    /**
     * Removes the white pieces' moves that result in the white king being put in check.
     * **/
    public static void removeIllegalMovesW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                           HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB,
                                           Piece[][] pieces, int[] prevCoords){
        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
        for(Piece p : piecesW.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesW.get(p)){
                // Copy of board state before move
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                p.playMove(arr[0], arr[1], piecesCopy, piecesW, piecesB, prevCoords, false);
                BoardEval.checkControlledSquares('b', squaresControlledW, squaresControlledB, piecesB, piecesCopy);

                if(!whiteKingInCheck)
                    moves.add(arr);

                //setPieces(piecesCopy, pieces);
            }

            piecesW.put(p, moves);
        }
    }

    /**
     * Removes the black pieces' moves that result in the black king being put in check.
     * **/
    public static void removeIllegalMovesB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                           HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB,
                                           Piece[][] pieces, int[] prevCoords){
        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
        for(Piece p : piecesB.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesB.get(p)){
                // Copy of board state before move
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                p.playMove(arr[0], arr[1], piecesCopy, piecesW, piecesB, prevCoords, false);
                BoardEval.checkControlledSquares('w', squaresControlledW, squaresControlledB, piecesW, piecesCopy);

                if(!blackKingInCheck)
                    moves.add(arr);

                //setPieces(piecesCopy, pieces);
            }

            piecesB.put(p, moves);
        }
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


    /**
     * Combined usage of checking possible squares and getting possible moves for all sides.
     * **/
    public static void update(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                              HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB,
                              Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                              boolean myTurn){
        BoardEval.checkControlledSquares('b', squaresControlledW, squaresControlledB, piecesW, pieces);
        BoardEval.checkControlledSquares('w', squaresControlledW, squaresControlledB, piecesB, pieces);

        BoardEval.getPossibleMoves(piecesW, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB);
        BoardEval.getPossibleMoves(piecesB, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB);

        BoardEval.removeIllegalMovesW(squaresControlledW, squaresControlledB, piecesW, piecesB, pieces, prevCoords);
        BoardEval.removeIllegalMovesB(squaresControlledW, squaresControlledB, piecesW, piecesB, pieces, prevCoords);

        for(Piece p : piecesW.keySet()){
            System.out.print(p + ": ");
            for(int arr[] : piecesW.get(p))
                System.out.print("(" + arr[0] + ", " + arr[1] + ") ");
            System.out.println();
        } System.out.println();

        BoardEval.cleanUpHashMap('w', piecesW);
        BoardEval.cleanUpHashMap('b', piecesB);

        for(Piece p : piecesW.keySet()){
            System.out.print(p + ": ");
            for(int arr[] : piecesW.get(p))
                System.out.print("(" + arr[0] + ", " + arr[1] + ") ");
            System.out.println();
        } System.out.println();

        BoardEval.givePieceScores(piecesW, piecesB);

        //System.out.println("Internal board score: " + BoardEval.boardScore());

        // Checkmate or stalemate
        if(possibleMovesW == 0 && myTurn) BoardEval.gameOver = true;
        if(possibleMovesB == 0 && !myTurn) BoardEval.gameOver = true;
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
    public static int numPossibleMovesW(){ return BoardEval.possibleMovesW; }
    public static int numPossibleMovesB(){ return BoardEval.possibleMovesB; }

    public static void cleanUpHashMap(char side, HashMap<Piece, ArrayList<int[]>> map){
        //map.entrySet().removeIf(entry -> entry.getType().equals(""));

        ArrayList<Piece> toDelete = new ArrayList<Piece>();

        for(Piece p : map.keySet()) {
            if (p.getSide() != side || p.getType().equals(""))
                toDelete.add(p);
        }

        for(Piece p : toDelete)
            map.remove(p);
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
}
