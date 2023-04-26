import java.util.*;

/**
 * AI for user to play against. Uses simple minimax with alpha-beta pruning to brute-force moves.
 * Takes some positional factors into account, and caches board states to improve speed later on in the game.
 * 
 * @author Gene Yang
 * @version April 26, 2023
 */

public class HackberryAI {
    private final int NUM_SQUARES = 8;
    private char side;
    private int DEPTH;

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

    public void print(Piece[][] pieces, int numTabs){
        for(int i = 0; i < NUM_SQUARES; i++){
            if(numTabs == 1) System.out.print("   ");
            else if(numTabs == 2) System.out.print("      ");
            else if(numTabs == 3) System.out.print("         ");
            for(int j = 0; j < NUM_SQUARES; j++)
                System.out.print("[" + pieces[j][i].getType() + (pieces[j][i].getType().length() == 2 ? " " : "   ") + "] ");
            System.out.println();
        }

        System.out.println();
    }

    public ArrayList<int[]> sortByCaptures(ArrayList<int[]> arr, Piece[][] pieces){
        ArrayList<int[]> ans = new ArrayList<>();

        for(int i = 0; i < arr.size(); i++){
            if(pieces[arr.get(i)[2]][arr.get(i)[3]].getSide() != ' ')
                ans.add(new int[]{arr.get(i)[0], arr.get(i)[1], arr.get(i)[2], arr.get(i)[3]});
        }

        for(int i = 0; i < arr.size(); i++){
            if(pieces[arr.get(i)[2]][arr.get(i)[3]].getSide() == ' ')
                ans.add(new int[]{arr.get(i)[0], arr.get(i)[1], arr.get(i)[2], arr.get(i)[3]});
        }

        return ans;
    }

    /*public double negamax(Piece[][] originalPieces, int[] move, int depth, double alpha, double beta, int curSide){
        // Make a copy and play the move.
        Piece[][] pieces = BoardEval.makeCopy(originalPieces);
        pieces[move[0]][move[1]].playMove(move[2], move[3], pieces);

        // Update possible moves for the computer
        ArrayList<int[]>[][] moveGen = BoardEval.updateControlledSquares(pieces);

        //print(pieces, 2 - depth + 1);


        // If the game is over with the other side to move
        if(BoardEval.gameOver(moveGen, pieces, -curSide) != -1) {
            //System.out.println("GAME OVER STATE FOUND");
            //System.exit(0);
            return BoardEval.gameOver(moveGen, pieces, -curSide);
        }

        // Max depth reached
        if(depth == 0) return BoardEval.boardScore(pieces);


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

        double val = -10000.0;
        for(int[] toTest : possibleMoves){
            val = Math.max(val, -negamax(pieces, toTest, depth - 1, -beta, -alpha, -curSide));

            alpha = Math.max(alpha, val);
            if(alpha >= beta)
                break;
        }

        return val;
    }*/

    public double minimax(Piece[][] originalBoard, int[] move, int depth, double alpha, double beta, int curSide){
        Piece[][] pieces = BoardEval.makeCopy(originalBoard);
        pieces[move[0]][move[1]].playMove(move[2], move[3], pieces);

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
        pieces[best[0]][best[1]].playMove(best[2], best[3], pieces);
        Notation.update(pieces, best[0], best[1], best[2], best[3]);

        System.out.println();
    }


    public ArrayList<int[]>[][] update(Piece[][] pieces, boolean[][] squaresControlledW, boolean[][] squaresControlledB, int[] prevCoords){
        /*Piece[][] piecesCopy = new Piece[NUM_SQUARES][NUM_SQUARES];
        ArrayList<Piece>[][] destSquares = new ArrayList<Piece>[NUM_SQUARES][NUM_SQUARES];

        setPieces(pieces, piecesCopy);

        piecesW = BoardEval.getPossibleMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesW);
        piecesB = BoardEval.getPossibleMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesB);

        piecesW = BoardEval.removeIllegalMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);
        piecesB = BoardEval.removeIllegalMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);

        BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces);
        BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces);

        BoardEval.givePieceScores(piecesW, piecesB);*/

        return null;
    }
}
