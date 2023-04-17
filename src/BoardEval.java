import java.util.ArrayList;

public class BoardEval {
    private static final int NUM_SQUARES = 8;
    Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];

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
                // Can't capture pieces on its own side
                if(pieces[xPos][yPos].getSide() == p.getSide())
                    break;

                // Can capture opponent's pieces
                else if(pieces[xPos][yPos].getSide() == (p.getSide() == 'w' ? 'b' : 'w')){
                    ans[xPos][yPos].add(new int[]{p.getGridX(), p.getGridY()});
                    break;
                }

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
                // Can't capture pieces on its own side
                if(pieces[xPos][yPos].getSide() == p.getSide())
                    break;

                // Can capture opponent's pieces
                else if(pieces[xPos][yPos].getSide() == (p.getSide() == 'w' ? 'b' : 'w')){
                    ans[xPos][yPos].add(new int[]{p.getGridX(), p.getGridY()});
                    break;
                }

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
                    //System.out.println("Can move to (" + (originX + king_dx[i]) + ", " + (originY + king_dy[i]) + ")");
                }
            }
        }
        //System.out.println();


        // Kingside castling
        ans[6][p.getGridY()].add(new int[]{4, p.getGridY()});

        // Queenside castling
        ans[2][p.getGridY()].add(new int[]{4, p.getGridY()});
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

    public static boolean whiteCanMoveHere(ArrayList<int[]> moves, Piece[][] pieces) {
        for (int[] arr : moves){
            if (pieces[arr[0]][arr[1]].getSide() == 'w')
                return true;
        }
        return false;
    }

    public static boolean blackCanMoveHere(ArrayList<int[]> moves, Piece[][] pieces) {
        for (int[] arr : moves){
            if (pieces[arr[0]][arr[1]].getSide() == 'b')
                return true;
        }
        return false;
    }

    public static void print(Piece[][] p){
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print("[" + p[j][i].getType() + (p[j][i].getType().equals("") ? "  " : "") + "] ");
            System.out.println();
        }
        System.out.println();
    }

    public static boolean[][] getControlled(ArrayList<int[]>[][] pseudoLegalMoves, Piece[][] pieces, char side){
        boolean[][] ans = new boolean[NUM_SQUARES][NUM_SQUARES];
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                ans[i][j] = false;

                for(int[] arr : pseudoLegalMoves[i][j]){
                    if(pieces[arr[0]][arr[1]].getSide() == side)
                        ans[i][j] = true;
                }
            }
        }

        return ans;
    }


    public static ArrayList<int[]>[][] testMoves(ArrayList<int[]>[][] pseudoLegalMoves, Piece[][] pieces){
        Piece[][] piecesCopy;

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

                    System.out.print("Testing (" + arr[0] + ", " + arr[1] + ") - " + pieces[arr[0]][arr[1]].getType() + " move to (" + i + ", " + j + ")");

                    // Remove illegal moves off the bat
                    if(!pieces[arr[0]][arr[1]].legalMove(i, j, pieces, controlledW, controlledB)) {
                        System.out.print("     -- FAILED CHECK 1");
                        System.out.println();
                        continue;
                    }

                    System.out.print("    -- OK check 1 --");

                    piecesCopy = makeCopy(pieces);
                    piecesCopy[arr[0]][arr[1]].playMove(i, j, piecesCopy);

                    //print(piecesCopy);

                    // Check the controlled squares after playing that move.
                    ArrayList<int[]>[][] movesCopy = getMoveGen(piecesCopy);


                    // If the piece that was moved and the king in check are in the same side, not legal.
                    boolean BInCheck = false;
                    boolean WInCheck = false;
                    for(int x = 0; x < NUM_SQUARES; x++){
                        for(int y = 0; y < NUM_SQUARES; y++){
                            if(piecesCopy[x][y].getType().equals("wk") && blackCanMoveHere(movesCopy[x][y], piecesCopy)) WInCheck = true;
                            if(piecesCopy[x][y].getType().equals("bk") && whiteCanMoveHere(movesCopy[x][y], piecesCopy)) BInCheck = true;
                        }
                    }

                    // Update moves and controlled squares
                    if(pieces[arr[0]][arr[1]].getSide() == 'b' && BInCheck){
                        System.out.println(" FAILED CHECK 2");
                        System.out.println();
                        continue;
                    }
                    else if(pieces[arr[0]][arr[1]].getSide() == 'w' && WInCheck){
                        System.out.print(" FAILED CHECK 2");
                        System.out.println();
                        continue;
                    }

                    System.out.print(" OK check 2");
                    System.out.println();
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

        return ans;
    }

}
