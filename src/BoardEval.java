import java.util.ArrayList;
import java.util.HashMap;


/**
 * Helper methods, legal move generation, and board evaluation.
 * @author Gene Yang
 * @version May 10, 2023
 */
public class BoardEval {
    private static final int NUM_SQUARES = 8;

    // For assigning scores to pieces
    public static final double BASE_VAL_WEIGHT = 0.95; // % base value
    public static final double MOBILITY_WEIGHT = 0.03; // % mobility
    public static final double POSITION_WEIGHT = 0.02; // % board position

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

    private final static int[] rook_dx = new int[]{-1, 1, 0, 0};
    private final static int[] rook_dy = new int[]{0, 0, -1, 1};

    private final static int[] knight_dx = new int[]{-2, -2, -1, -1, 1, 1, 2, 2};
    private final static int[] knight_dy = new int[]{-1, 1, -2, 2, -2, 2, -1, 1};

    private final static int[] bishop_dx = new int[]{-1, 1, -1, 1};
    private final static int[] bishop_dy = new int[]{-1, -1, 1, 1};

    private final static int[] king_dx = new int[]{-1, -1, -1, 0, 0, 1, 1, 1};
    private final static int[] king_dy = new int[]{1, 0, -1, 1, -1, 1, 0, -1};


    /** Move generation **/
    public static void pawnMoveGen(Piece p, Piece[][] pieces, ArrayList<int[]>[][] ans){
        int originX = p.getGridX(),
            originY = p.getGridY();

        if(p.getSide() == 'w'){
            // 1- and 2-square moves
            if(originY >= 1 && pieces[originX][originY - 1].getSide() == ' ') {
                ans[originX][originY - 1].add(new int[]{originX, originY});

                if (p.numMoves == 0 && originY >= 2 && pieces[originX][originY - 2].getSide() == ' ')
                    ans[originX][originY - 2].add(new int[]{originX, originY});
            }

            // Captures
            if(originX >= 1 && originY >= 1 && pieces[originX - 1][originY - 1].getSide() == 'b')
                ans[originX - 1][originY - 1].add(new int[]{originX, originY});
            if(originX < NUM_SQUARES - 1 && originY >= 1 && pieces[originX + 1][originY - 1].getSide() == 'b')
                ans[originX + 1][originY - 1].add(new int[]{originX, originY});

            // En passant
            if(originX >= 1 && originY >= 1 && pieces[originX - 1][originY].getType().equals("bp") &&
               pieces[originX - 1][originY].numMoves == 1 && pieces[originX - 1][originY].recentlyMoved)
                ans[originX - 1][originY - 1].add(new int[]{originX, originY});
            if(originX < NUM_SQUARES - 1 && originY >= 1 && pieces[originX + 1][originY].getType().equals("bp") &&
               pieces[originX + 1][originY].numMoves == 1 && pieces[originX + 1][originY].recentlyMoved)
                ans[originX + 1][originY - 1].add(new int[]{originX, originY});

        } else {
            // 1- and 2-square moves
            if(originY < NUM_SQUARES - 1 && pieces[originX][originY + 1].getSide() == ' ') {
                ans[originX][originY + 1].add(new int[]{originX, originY});

                if (p.numMoves == 0 && originY < NUM_SQUARES - 2 && pieces[originX][originY + 2].getSide() == ' ')
                    ans[originX][originY + 2].add(new int[]{originX, originY});
            }

            // Captures
            if(originX >= 1 && originY < NUM_SQUARES - 1 && pieces[originX - 1][originY + 1].getSide() == 'w')
                ans[originX - 1][originY + 1].add(new int[]{originX, originY});
            if(originX < NUM_SQUARES - 1 && originY < NUM_SQUARES - 1 && pieces[originX + 1][originY + 1].getSide() == 'w')
                ans[originX + 1][originY + 1].add(new int[]{originX, originY});

            // En passant
            if(originX >= 1 && originY < NUM_SQUARES - 1 && pieces[originX - 1][originY].getType().equals("wp") &&
               pieces[originX - 1][originY].numMoves == 1 && pieces[originX - 1][originY].recentlyMoved)
                ans[originX - 1][originY + 1].add(new int[]{originX, originY});
            if(originX < NUM_SQUARES - 1 && originY < NUM_SQUARES - 1 && pieces[originX + 1][originY].getType().equals("wp") &&
               pieces[originX + 1][originY].numMoves == 1 && pieces[originX + 1][originY].recentlyMoved)
                ans[originX + 1][originY + 1].add(new int[]{originX, originY});
        }
    }
    public static void rookMoveGen(Piece p, Piece[][] pieces, ArrayList<int[]>[][] ans){
        int xPos,
            yPos;

        for(int i = 0; i < rook_dx.length; i++) {
            xPos = p.getGridX() + rook_dx[i];
            yPos = p.getGridY() + rook_dy[i];

            while (xPos < NUM_SQUARES && yPos < NUM_SQUARES && xPos >= 0 && yPos >= 0) {
                // Can capture opponent's pieces
                if(pieces[xPos][yPos].getSide() != p.getSide() && pieces[xPos][yPos].getSide() != ' '){
                    ans[xPos][yPos].add(new int[]{p.getGridX(), p.getGridY()});
                    break;
                }
                else if(pieces[xPos][yPos].getSide() == p.getSide()){
                    break;
                }
                else
                    ans[xPos][yPos].add(new int[]{p.getGridX(), p.getGridY()});

                xPos += rook_dx[i];
                yPos += rook_dy[i];
            }
        }
    }
    public static void bishopMoveGen(Piece p, Piece[][] pieces, ArrayList<int[]>[][] ans){
        int xPos,
            yPos;

        // For every direction combination
        for(int i = 0; i < bishop_dx.length; i++){
            xPos = p.getGridX() + bishop_dx[i];
            yPos = p.getGridY() + bishop_dy[i];

            // Go in the direction and stop if a piece is found (blocking).
            while(xPos < NUM_SQUARES && yPos < NUM_SQUARES && xPos >= 0 && yPos >= 0){
                // Can capture opponent's pieces
                if(pieces[xPos][yPos].getSide() != p.getSide() && pieces[xPos][yPos].getSide() != ' '){
                    ans[xPos][yPos].add(new int[]{p.getGridX(), p.getGridY()});
                    break;
                }
                else if(pieces[xPos][yPos].getSide() == p.getSide()){
                    break;
                }
                else
                    ans[xPos][yPos].add(new int[]{p.getGridX(), p.getGridY()});

                xPos += bishop_dx[i];
                yPos += bishop_dy[i];
            }
        }

    }
    public static void knightMoveGen(Piece p, Piece[][] pieces, ArrayList<int[]>[][] ans){
        int originX = p.getGridX(),
            originY = p.getGridY();

        // For every direction combination
        for(int i = 0; i < knight_dx.length; i++){
            if(originX + knight_dx[i] < NUM_SQUARES && originX + knight_dx[i] >= 0 &&
               originY + knight_dy[i] < NUM_SQUARES && originY + knight_dy[i] >= 0) {

                // Can't capture pieces on its own side
                if(pieces[originX + knight_dx[i]][originY + knight_dy[i]].getSide() != pieces[originX][originY].getSide())
                    ans[originX + knight_dx[i]][originY + knight_dy[i]].add(new int[]{originX, originY});
            }
        }
    }
    public static void kingMoveGen(Piece p, Piece[][] pieces, ArrayList<int[]>[][] ans){
        int originX = p.getGridX(),
            originY = p.getGridY();

        //System.out.println("KING MOVE GEN - " + p.getSide());
        // For every direction combination
        for(int i = 0; i < king_dx.length; i++){
            // Check if it's within range
            if(originX + king_dx[i] < NUM_SQUARES && originX + king_dx[i] >= 0 &&
               originY + king_dy[i] < NUM_SQUARES && originY + king_dy[i] >= 0){

                // Can't capture pieces on its own side
                if(pieces[originX + king_dx[i]][originY + king_dy[i]].getSide() != p.getSide()) {
                    ans[originX + king_dx[i]][originY + king_dy[i]].add(new int[]{originX, originY});
                }
            }
        }
        //System.out.println();


        // Kingside castling
        if(p.getSide() == 'w') ans[6][NUM_SQUARES - 1].add(new int[]{4, NUM_SQUARES - 1});
        else ans[6][0].add(new int[]{4, 0});

        // Queenside castling
        if(p.getSide() == 'w') ans[2][NUM_SQUARES - 1].add(new int[]{4, NUM_SQUARES - 1});
        else ans[2][0].add(new int[]{4, 0});
    }


    // Gets the pseudo-legal moves that can be made by a piece depending on its movement pattern.
    // ArrayList returned generates [dx, dy] -> must use [x + dx[i], y + dy[i]]
    public static ArrayList<int[]>[][] getMoveGen(Piece[][] pieces){
        ArrayList<int[]>[][] ans = new ArrayList[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                ans[i][j] = new ArrayList<int[]>();
        }

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(pieces[i][j].getSide() == ' ') continue;

                char type = pieces[i][j].getType().charAt(1);
                if(type == 'p')
                    pawnMoveGen(pieces[i][j], pieces, ans);
                else if(type == 'r')
                    rookMoveGen(pieces[i][j], pieces, ans);
                else if(type == 'n')
                    knightMoveGen(pieces[i][j], pieces, ans);
                else if(type == 'b')
                    bishopMoveGen(pieces[i][j], pieces, ans);
                else if(type == 'q'){
                    bishopMoveGen(pieces[i][j], pieces, ans);
                    rookMoveGen(pieces[i][j], pieces, ans);
                } else if(type == 'k'){
                    kingMoveGen(pieces[i][j], pieces, ans);
                }
            }
        }

        return ans;
    }


    /** Helper methods **/
    public static Piece[][] makeCopy(Piece[][] arr){
        Piece[][] ans = new Piece[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                ans[i][j] = new Piece(arr[i][j]);
        }
        return ans;
    }

    public static boolean blackKingInCheck(boolean[][] controlledW, boolean[][] controlledB, Piece[][] pieces){
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(pieces[i][j].getType().equals("bk"))
                    return controlledW[i][j];
            }
        }

        return false;
    }
    public static boolean whiteKingInCheck(boolean[][] controlledW, boolean[][] controlledB, Piece[][] pieces){
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(pieces[i][j].getType().equals("wk"))
                    return controlledB[i][j];
            }
        }

        return false;
    }

    public static void print(Piece[][] p){
        for(int i = 0; i < NUM_SQUARES; i++){
            System.out.print("   ");
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print("[" + p[j][i].getType() + (p[j][i].getType().equals("") ? "  " : "") + "] ");
            System.out.println();
        }
        System.out.println();
    }
    public static void print(boolean[][] a){
        for(int i = 0; i < NUM_SQUARES; i++){
            System.out.print("   ");
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print((a[j][i] ? "[XX] " : "[  ] "));
            System.out.println();
        }
        System.out.println();
    }

    public static boolean[][] getControlled(ArrayList<int[]>[][] pseudoLegalMoves, Piece[][] pieces, char side){
        boolean[][] ans = new boolean[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                ans[i][j] = false;
        }

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){

                for(int[] arr : pseudoLegalMoves[i][j]){
                    // Pawns are an exception - can move forward but only captures diagonally.
                    if(pieces[arr[0]][arr[1]].getType().equals("wp") && side == 'w'){
                        if(arr[1] - 1 >= 0 && arr[0] > 0) ans[arr[0] - 1][arr[1] - 1] = true;
                        if(arr[1] - 1 >= 0 && arr[0] < NUM_SQUARES - 1) ans[arr[0] + 1][arr[1] - 1] = true;

                    } else if(pieces[arr[0]][arr[1]].getType().equals("bp") && side == 'b'){
                        if(arr[1] + 1 < NUM_SQUARES && arr[0] > 0) ans[arr[0] - 1][arr[1] + 1] = true;
                        if(arr[1] + 1 < NUM_SQUARES && arr[0] < NUM_SQUARES - 1) ans[arr[0] + 1][arr[1] + 1] = true;
                    }

                    else if(pieces[arr[0]][arr[1]].getSide() == side)
                        ans[i][j] = true;
                }
            }
        }

        return ans;
    }


    public static ArrayList<int[]>[][] testMoves(ArrayList<int[]>[][] pseudoLegalMoves, Piece[][] pieces){

        ArrayList<int[]>[][] ans = new ArrayList[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                ans[i][j] = new ArrayList<int[]>();
        }

        // Initial update of controlled squares
        boolean[][] controlledW = getControlled(pseudoLegalMoves, pieces, 'w');
        boolean[][] controlledB = getControlled(pseudoLegalMoves, pieces, 'b');

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                for(int[] arr : pseudoLegalMoves[i][j]){
                    if(!pieces[arr[0]][arr[1]].legalMove(i, j, pieces, controlledW, controlledB)){
                        continue;
                    }

                    char sideThatMoved = pieces[arr[0]][arr[1]].getSide();

                    // Simulate the move
                    Piece[][] piecesCopy = BoardEval.makeCopy(pieces);
                    piecesCopy[arr[0]][arr[1]].playMove(i, j, piecesCopy);

                    // Get move generation for the new board state
                    ArrayList<int[]>[][] moveGenCopy = BoardEval.getMoveGen(piecesCopy);
                    boolean[][] controlledWCopy = getControlled(moveGenCopy, piecesCopy, 'w');
                    boolean[][] controlledBCopy = getControlled(moveGenCopy, piecesCopy, 'b');

                    if(sideThatMoved == 'w' && whiteKingInCheck(controlledWCopy, controlledBCopy, piecesCopy)){
                        //System.out.println("Found illegal move for white");
                        continue;
                    } else if(sideThatMoved == 'b' && blackKingInCheck(controlledWCopy, controlledBCopy, piecesCopy)){
                        //System.out.println("Found illegal move for black");
                        continue;
                    }

                    ans[i][j].add(new int[]{arr[0], arr[1]});
                }
            }
        }

        return ans;
    }


    /**
     * Updates squaresControlledW and squaresControlledB by checking every piece on the board.
     * Tries to play each move and checks if it's possible.
     *
     * Also updates and returns the movableSquares array (which consists of all pieces that can move to a square).
     */
    public static ArrayList[][] updateControlledSquares(Piece[][] pieces){
        // Gets the "raw" possible moves.
        // Only based on blocked paths & numMoves, doesn't care about checks.
        ArrayList<int[]>[][] ans = getMoveGen(pieces);

        // Test each move and return the ArrayList representation of the legal moves.
        // Also updates the controlled squares.
        /** Sensitive test points: king moves, king opposition, castling **/
        ans = testMoves(ans, pieces);

        givePieceScores(pieces, ans);

        return ans;
    }

    public static double boardScore(Piece[][] pieces){
        double whiteScore = 0.0,
               blackScore = 0.0;
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(pieces[i][j].getSide() == 'w') whiteScore += pieces[i][j].getValue();
                else blackScore += pieces[i][j].getValue();
            }
        }

        return whiteScore - blackScore;
    }

    /**
     *
     * @param moveGen
     * @param pieces
     * @param curSide - the side to move (1 = white, -1 = black)
     * @return
     */
    public static int gameOver(ArrayList<int[]>[][] moveGen, Piece[][] pieces, int curSide){
        int bOptions = 0,
            wOptions = 0;
        boolean WKcheck = false,
                BKcheck = false;

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                int curWOptions = 0,
                        curBOptions = 0;

                for(int[] arr : moveGen[i][j]){
                    if(pieces[arr[0]][arr[1]].getSide() == 'w') curWOptions++;
                    else if(pieces[arr[0]][arr[1]].getSide() == 'b') curBOptions++;
                }

                // Checks
                if(pieces[i][j].getType().equals("wk") && curBOptions > 0) WKcheck = true;
                if(pieces[i][j].getType().equals("bk") && curWOptions > 0) BKcheck = true;

                bOptions += curBOptions;
                wOptions += curWOptions;
            }
        }

        // Checkmate
        if(WKcheck && wOptions == 0) return -1000;
        if(BKcheck && bOptions == 0) return 1000;

        // Stalemate
        if(bOptions == 0 && wOptions == 0) return 0;
        else return -1;
    }

    public static void givePieceScores(Piece[][] pieces, ArrayList<int[]>[][] moveGen){
        HashMap<Piece, Integer> mobility = new HashMap<>();
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                for(int[] arr : moveGen[i][j]){
                    mobility.put(new Piece(pieces[arr[0]][arr[1]]), mobility.get(pieces[arr[0]][arr[1]]) == null ? 1 : mobility.get(pieces[arr[0]][arr[1]]) + 1);
                }
            }
        }

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(pieces[i][j].getSide() == ' ')
                    continue;
                else if(pieces[i][j].getType().equals("wp"))
                    pieces[i][j].setValue(
                            BASE_VAL_WEIGHT * pieces[i][j].getBaseValue() +
                            MOBILITY_WEIGHT * (mobility.get(pieces[i][j]) == null ? 0 : mobility.get(pieces[i][j])) +
                            POSITION_WEIGHT * (NUM_SQUARES - i));
                else if(pieces[i][j].getType().equals("bp"))
                    pieces[i][j].setValue(
                            BASE_VAL_WEIGHT * pieces[i][j].getBaseValue() +
                            MOBILITY_WEIGHT * (mobility.get(pieces[i][j]) == null ? 0 : mobility.get(pieces[i][j])) +
                            POSITION_WEIGHT * i);
                else
                   pieces[i][j].setValue(
                            BASE_VAL_WEIGHT * pieces[i][j].getBaseValue() +
                            MOBILITY_WEIGHT * (mobility.get(pieces[i][j]) == null ? 0 : mobility.get(pieces[i][j])) +
                            POSITION_WEIGHT * boardPositionValues[i][j]
                   );
            }
        }
    }

    public static String FENify(Piece[][] pieces, int curSide){
        String ans = "";
        int blanks = 0;
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(pieces[i][j].getSide() == ' ')
                    blanks++;
                else {
                    if(blanks > 0){
                        ans += String.valueOf(blanks);
                        blanks = 0;
                    }
                    ans += pieces[i][j].getType().charAt(1);
                }
            }
        }

        if(blanks > 0)
            ans += String.valueOf(blanks);

        return ans + (curSide == 1 ? " -1" : " 1");
    }
}
