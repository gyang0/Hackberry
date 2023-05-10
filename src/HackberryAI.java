import java.util.*;

/**
 * AI for user to play against. Uses simple minimax with alpha-beta pruning to brute-force moves.
 * Takes some positional factors into account, and caches board states to improve speed later on in the game.
 *
 * @author Gene Yang
 * @version May 10, 2023
 */

/**
 * Stores possible openings for Hackberry to reference without calculating moves.
 *
 * For white: Ruy Lopez
 *            English Opening
 *            Catalan
 *            Queen's Gambit
 *            Giuoco Piano
 *            Fried Liver
 *
 * For black: King's Indian
 *            Nimzo-Indian
 *            Sicilian Defense
 *            Slav Defense
 *            Caro-Kann Defense
 *            Refutation for Fried Liver
 */

public class HackberryAI {
    private final int NUM_SQUARES = 8;
    private char side;
    private int DEPTH;

    public static String[][] continuations = new String[][]{
            {"e4", "e5", "Nf3", "Nf6", "Bb5", "a6", "Ba4", "Nf6", "O-O"}, // Ruy Lopez
            {"d4", "d5", "c4", "c6", "Nc3", "e6", "Nf3", "Nf6", "Bg5", "dxc4"}, // Queen's Gambit + slav defense
            {"d4", "d5", "c4", "Nf3", "Nf6", "Nc3", "c5", "cxd5", "exd5", "Bg5", "Be7", "dxc5"}, // Queen's Gambit Declined
            {"e4", "e5", "Nf3", "Nf6", "Bc4", "Nf6", "d3", "Bc5", "c3", "O-O", "O-O"}, // Giuoco Piano
            {"e4", "e5", "Nf3", "Nf6", "Bc4", "Nf6", "Ng5", "d4", "exd5", "Na5"}, // Fried Liver + refutation
            {"e4", "e5", "Nf3", "Nf6", "d4", "exd4"}, // Scotch Game

            {"e4", "c5", "Nf3", "d3", "d4", "cxd4", "Nxd4", "Nf6", "Nc3", "a6", "Be2", "e5"}, // Sicilian, Najdorf Defense
            {"d4", "Nf6", "c4", "g6", "Nc3", "Bg7", "e4", "d6", "Be2", "O-O"}, // King's Indian Defense
    };

    HashMap<String, Double> boardCache;


    public HackberryAI(char side, int depth){
        this.side = side;
        this.DEPTH = depth;

        this.boardCache = new HashMap<>();
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

    public void autopromote(Piece[][] pieces){
        Piece[][] pieceCopy = BoardEval.makeCopy(pieces);

        // Just promote to a queen.
        for(int i = 0; i < NUM_SQUARES; i++){
            if(pieces[i][0].getType().equals("wp"))
                pieces[i][0] = new Piece(i, 0, "wq", 'w', 0);
            if(pieces[i][NUM_SQUARES - 1].getType().equals("bp"))
                pieces[i][NUM_SQUARES - 1] = new Piece(i, NUM_SQUARES - 1, "bq", 'b', 0);
        }
    }

    public double minimax(Piece[][] originalBoard, int[] move, int depth, double alpha, double beta, int curSide){
        Piece[][] pieces = BoardEval.makeCopy(originalBoard);
        pieces[move[0]][move[1]].playMove(move[2], move[3], pieces);
        this.autopromote(pieces);

        //System.out.println();
        //System.out.println("DEPTH " + depth);
        //print(pieces, 2 - depth + 1);

        ArrayList<int[]>[][] moveGen = BoardEval.updateControlledSquares(pieces);
        if(BoardEval.gameOver(moveGen, pieces, -curSide) != -1){
            //System.out.println("BOARD STATE FOUND: GAME OVER -- " + BoardEval.gameOver(moveGen, pieces, -curSide));
            return BoardEval.gameOver(moveGen, pieces, -curSide);
        }
        if(depth == 0) return BoardEval.boardScore(pieces);

        String cache = BoardEval.FENify(originalBoard, -curSide);
        if(boardCache.containsKey(cache))
            return boardCache.get(cache);

        // Get the possible moves in this position
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                for(int[] arr : moveGen[i][j]){
                    if((pieces[arr[0]][arr[1]].getSide() == 'w' && curSide == -1) ||
                       (pieces[arr[0]][arr[1]].getSide() == 'b' && curSide == 1))
                        possibleMoves.add(new int[]{arr[0], arr[1], i, j});
                }
            }
        }

        if(curSide == -1){
            double val = -100000.0;
            for(int[] candidate : possibleMoves){
                //System.out.println("    Candidate: (" + candidate[0] + ", " + candidate[1] + ") to (" + candidate[2] + ", " + candidate[3] + ")");
                double t = minimax(pieces, candidate, depth - 1, alpha, beta, -curSide);
                val = Math.max(val, t);
                //System.out.println(t);

                alpha = Math.max(alpha, val);
                if(val >= beta) break;
            }
            return val;

        } else {
            double val = 100000.0;
            for(int[] candidate : possibleMoves){
                val = Math.min(val, minimax(pieces, candidate, depth - 1, alpha, beta, -curSide));

                beta = Math.min(beta, val);
                if(val <= alpha) break;
            }

            return val;
        }
    }

    // Actual AI goes here
    // Find the 5 moves that offer the best position
    public void makeMove(Piece[][] pieces){
        // Update possible moves for the computer
        ArrayList<int[]>[][] moveGen = BoardEval.updateControlledSquares(pieces);

        // Get list of all possible moves
        ArrayList<int[]> movesW = new ArrayList<>();
        ArrayList<int[]> movesB = new ArrayList<>();

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                for(int[] arr : moveGen[i][j]){
                    if(pieces[arr[0]][arr[1]].getSide() == 'w')
                        movesW.add(new int[]{arr[0], arr[1], i, j});
                    else
                        movesB.add(new int[]{arr[0], arr[1], i, j});
                }
            }
        }

        //int ind = (int)(Math.random() * moves.size());
        //pieces[moves.get(ind)[0]][moves.get(ind)[1]].playMove(moves.get(ind)[2], moves.get(ind)[3], pieces);

        /*if(this.stillInOpening()){
            playOpening();
        } else {*/
            int[] best = new int[4];
            if(this.side == 'w'){

                // Get the best move that maximizes the board score
                double bestScore = -100000.0;
                for(int[] move : movesW){
                    System.out.print("Considering move (" + move[0] + ", " + move[1] + ") to (" + move[2] + ", " + move[3] + ") --> ");
                    double score = minimax(pieces, move, DEPTH, -100000.0, 100000.0, 1);
                    //double score = negamax(pieces, move, DEPTH, -100000.0, 100000.0, 1);
                    System.out.println(score);

                    // Save board cache
                    boardCache.put(BoardEval.FENify(pieces, 1), score);

                    if(score > bestScore){
                        bestScore = score;
                        best = new int[]{move[0], move[1], move[2], move[3]};
                    }
                }

            } else if(this.side == 'b'){
                double bestScore = 100000.0;
                for(int[] move : movesB){
                    System.out.print("Considering move (" + move[0] + ", " + move[1] + ") to (" + move[2] + ", " + move[3] + ") --> ");
                    double score = minimax(pieces, move, DEPTH, -100000.0, 100000.0, -1);
                    //double score = negamax(pieces, move, DEPTH, -100000.0, 100000.0, -1);
                    System.out.println(score);

                    // Save board cache
                    boardCache.put(BoardEval.FENify(pieces, -1), score);

                    if(score < bestScore){
                        bestScore = score;
                        best = new int[]{move[0], move[1], move[2], move[3]};
                    }
                }
            }
        //}

        // Play the best move found
        // Promotion
        if((pieces[best[0]][best[1]].getType().equals("wp") || pieces[best[0]][best[1]].getType().equals("bp")) &&
           (best[3] == 0 || best[3] == NUM_SQUARES - 1))
            Notation.addPromotion(best[2], best[3], "Q");
        else
            Notation.update(pieces, best[0], best[1], best[2], best[3]);

        pieces[best[0]][best[1]].playMove(best[2], best[3], pieces);
        autopromote(pieces);

        System.out.println();
    }
}
