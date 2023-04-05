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
        /*for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print("[" + from[j][i].getType() + (from[j][i].getType().length() == 2 ? "" : "  ") + "]");
            System.out.println();
        }*/

        for(int r = 0; r < NUM_SQUARES; r++) {
            for (int c = 0; c < NUM_SQUARES; c++) {
                to[r][c] = new Piece(r, c, from[r][c].getType(), from[r][c].getSide(), from[r][c].numMoves);
            }
        }

        /*
        System.out.println();
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print("[" + to[j][i].getType() + (to[j][i].getType().length() == 2 ? "" : "  ") + "]");
            System.out.println();
        }
        System.out.println();*/
    }

    public static boolean[][] setArr(boolean[][] arr){
        boolean[][] temp = new boolean[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                temp[i][j] = arr[i][j];
        }
        return temp;
    }

    private static HashMap<Piece, ArrayList<int[]>> copyHashMap(HashMap<Piece, ArrayList<int[]>> map){
        HashMap<Piece, ArrayList<int[]>> temp = new HashMap<>();

        for(Piece p : map.keySet()){
            ArrayList<int[]> list = new ArrayList<int[]>();
            for(int[] arr : map.get(p))
                list.add(new int[]{arr[0], arr[1]});
            temp.put(new Piece(p), list);
        }
        return temp;
    }



    /**
     * Checks the squares controlled by the white side.
     * **/
    public static void checkControlledSquaresW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                               Piece[][] pieces, HashMap<Piece, ArrayList<int[]>> piecesW) {
        // Reset
        for (int i = 0; i < NUM_SQUARES; i++){
            for (int j = 0; j < NUM_SQUARES; j++)
                squaresControlledW[i][j] = false;
        }

        blackKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ', 0);

        // For every black piece
        for (Piece p : piecesW.keySet()) {
                // For every square on the board
                for (int pos1 = 0; pos1 < NUM_SQUARES; pos1++) {
                    for (int pos2 = 0; pos2 < NUM_SQUARES; pos2++) {

                        // Having control over that square:
                        // For testing purposes, we set the piece of an opposite side at that square.
                        // Then we see if that piece can be captured.
                        prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide(), pieces[pos1][pos2].numMoves);
                        pieces[pos1][pos2].setPiece(pos1, pos2, "bp", 'b', 0);

                        // Can make a capture (if necessary) at that position.
                        if (p.legalMove(pos1, pos2, pieces, new int[]{pos1, pos2}, squaresControlledW, squaresControlledB)) {
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

    /**
     * Checks the squares controlled by the black side.
     * **/
    public static void checkControlledSquaresB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                               Piece[][] pieces, HashMap<Piece, ArrayList<int[]>> piecesB) {
        // Reset
        for (int i = 0; i < NUM_SQUARES; i++) {
            for (int j = 0; j < NUM_SQUARES; j++)
                squaresControlledB[i][j] = false;
        }

        whiteKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ', 0);

        // For every black piece
        for(Piece p : piecesB.keySet()) {
            // For every square on the board
            for (int pos1 = 0; pos1 < NUM_SQUARES; pos1++) {
                for (int pos2 = 0; pos2 < NUM_SQUARES; pos2++) {

                    // Having control over that square:
                    // For testing purposes, we set the piece of an opposite side at that square.
                    // Then we see if that piece can be captured.
                    prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide(), pieces[pos1][pos2].numMoves);
                    pieces[pos1][pos2].setPiece(pos1, pos2, "wp", 'w', 0);

                    // Can make a capture (if necessary) at that position.
                    if (p.legalMove(pos1, pos2, pieces, new int[]{pos1, pos2}, squaresControlledW, squaresControlledB)) {
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

    /**
     * Updates the positions every white piece can move to.
     * **/
    public static void getPossibleMovesW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                         Piece[][] pieces, int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesW){
        // Reset
        HashMap<Piece, ArrayList<int[]>> copy = copyHashMap(piecesW);
        for(Piece p : piecesW.keySet()) piecesW.put(p, new ArrayList<int[]>());
        //piecesW.replaceAll((p, v) -> new ArrayList<int[]>());

        // For every black piece
        for(Piece p : copy.keySet()){
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
    public static void getPossibleMovesB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                         Piece[][] pieces, int[] mostRecentPieceMov, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Reset
        HashMap<Piece, ArrayList<int[]>> copy = copyHashMap(piecesB);
        for(Piece p : piecesB.keySet()) piecesB.put(p, new ArrayList<int[]>());

        // For every black piece
        for(Piece p : copy.keySet()){
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

            piecesB.put(new Piece(p), possibleMoves);
        }
    }

    /**
     * Removes the white pieces' moves that result in the white king being put in check.
     * **/
    public static void removeIllegalMovesW(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                           Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                                           HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Setup
        HashMap<Piece, ArrayList<int[]>> copy = copyHashMap(piecesW);
        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(Piece p : copy.keySet()){
            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : copy.get(p)){
                // Copy of board state before move
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, new int[]{p.getGridX(), p.getGridY()}, false);
                BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces, piecesB);

                //System.out.println(p + " move to (" + arr[0] + ", " + arr[1] + " resulted in a check to the white king: " + whiteKingInCheck);

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
    public static void removeIllegalMovesB(boolean[][] squaresControlledW, boolean[][] squaresControlledB,
                                           Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                                           HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB){
        // Setup
        HashMap<Piece, ArrayList<int[]>> copy = copyHashMap(piecesB);
        Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(Piece p : copy.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : copy.get(p)){
                // Copy array
                setPieces(pieces, piecesCopy);

                // Move piece to square and see if it results in check.
                p.playMove(arr[0], arr[1], pieces, piecesW, piecesB, new int[]{p.getGridX(), p.getGridY()}, false);
                BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces, piecesW);

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

                setPieces(piecesCopy, pieces);
                /*System.out.println();
                for(int i = 0; i < NUM_SQUARES; i++){
                    for(int j = 0; j < NUM_SQUARES; j++)
                        System.out.print("[" + pieces[j][i].getType() + (pieces[j][i].getType().length() == 2 ? "" : "  ") + "]");
                    System.out.println();
                }
                System.out.println();*/
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
                              Piece[][] pieces, int[] mostRecentPieceMov, int[] prevCoords,
                              HashMap<Piece, ArrayList<int[]>> piecesW, HashMap<Piece, ArrayList<int[]>> piecesB,
                              boolean whiteTurn){
        //BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces, piecesW);
        //BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces, piecesB);

        BoardEval.getPossibleMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesW);
        BoardEval.getPossibleMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesB);

        System.out.println("---------- Before removing illegal moves ----------");
        for(Piece p : piecesW.keySet()){
            System.out.print(p + ": ");
            for(int arr[] : piecesW.get(p))
                System.out.print("(" + arr[0] + ", " + arr[1] + ") ");
            System.out.println();
        }
        System.out.println();

        BoardEval.removeIllegalMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);
        BoardEval.removeIllegalMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);

        System.out.println("---------- After removing illegal moves ----------");
        for(Piece p : piecesW.keySet()){
            System.out.print(p + ": ");
            for(int arr[] : piecesW.get(p))
                System.out.print("(" + arr[0] + ", " + arr[1] + ") ");
            System.out.println();
        }
        System.out.println();

        BoardEval.givePieceScores(piecesW, piecesB);

        // Checkmate or stalemate
        if(possibleMovesW == 0 && whiteTurn) BoardEval.gameOver = true;
        if(possibleMovesB == 0 && !whiteTurn) BoardEval.gameOver = true;
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
}
