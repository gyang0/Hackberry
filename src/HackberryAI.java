import java.util.*;

/**
 * AI for user to play against.
 *
 * TODO: handle promotion (either a queen or a knight, no need to check others)
 * TODO: consider candidate moves first
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

    /*public ArrayList<int[]> getFilteredMoves(ArrayList<int[]> arr, Piece[][] originalPieces){
        ArrayList<Double> scores = new ArrayList<>(); // {move score, fromX, fromY, toX, toY}
        HashMap<Double, int[]> scoresToMoves = new HashMap<>();

        for(int[] move : arr){
            Piece[][] pieces = BoardEval.makeCopy(originalPieces);
            pieces[move[0]][move[1]].playMove(move[2], move[3], pieces);

            scores.add(BoardEval.boardScore(pieces));
            scoresToMoves.put(BoardEval.boardScore(pieces), new int[]{move[0], move[1], move[2], move[3]});
        }

        Collections.sort(scores);

        ArrayList<int[]> ans = new ArrayList<>();
        for(int i = scores.size() - 1; i >= Math.max(0, scores.size() - NUM_CANDIDATES); i--) {
            int[] bestMove = scoresToMoves.get(scores.get(i));
            ans.add(new int[]{bestMove[0], bestMove[1], bestMove[2], bestMove[3]});
        }

        return ans;
    }*/

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

    public double minimax(int fromX, int fromY, int toX, int toY, Piece[][] originalPieces,
                          char curSide, int depth,
                          double alpha, double beta){
        // Make a copy and play the move.
        Piece[][] pieces = BoardEval.makeCopy(originalPieces);
        pieces[fromX][fromY].playMove(toX, toY, pieces);

        //print(pieces);

        // Update possible moves for the computer
        ArrayList<int[]>[][] moveGen = BoardEval.updateControlledSquares(pieces);

        // Get the possible moves in this position
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

        // Reached max depth or no moves to make
        if(depth == 0) return BoardEval.boardScore(pieces);
        if(movesW.size() == 0 && movesB.size() == 0) return 0.0; // Stalemate
        if(curSide == 'w' && movesW.size() == 0) return Integer.MIN_VALUE; // Checkmate by black
        if(curSide == 'b' && movesB.size() == 0) return Integer.MAX_VALUE; // Checkmate by white


        // Sort by captures
        movesW = sortByCaptures(movesW, pieces);
        movesB = sortByCaptures(movesB, pieces);

        if(curSide == 'w') {
            double val = -100000.0;

            // Filter the best moves
            /*ArrayList<int[]> filteredMovesW = getFilteredMoves(movesW, pieces);
            for(int[] move : filteredMovesW){
                System.out.println("    - Filtered candidate: (" + move[0] + ", " + move[1] + ") to (" + move[2] + ", " + move[3] + ")");
            }*/

            for (int[] move : /*filteredMovesW*/movesW){
                val = Math.max(val, minimax(move[0], move[1], move[2], move[3], pieces, 'b', depth - 1, alpha, beta));

                if(val > beta)
                    break;
                alpha = Math.max(alpha, val);
            }
            return val;
        } else {
            double val = 100000.0;

            // Filter the best moves
            /*ArrayList<int[]> filteredMovesB = getFilteredMoves(movesB, pieces);
            for(int[] move : filteredMovesB){
                System.out.println("    - Filtered candidate: (" + move[0] + ", " + move[1] + ") to (" + move[2] + ", " + move[3] + ")");
            }*/

            for(int[] move : /*filteredMovesB*/movesB){
                val = Math.min(val, minimax(move[0], move[1], move[2], move[3], pieces, 'w', depth - 1, alpha, beta));

                if(val < alpha)
                    break;
                beta = Math.min(beta, val);
            }
            return val;
        }
    }

    public double negamax(Piece[][] originalPieces, int[] move, int depth, double alpha, double beta, int curSide){
        // Max depth reached
        if(depth == 0) return BoardEval.boardScore(originalPieces);

        // Make a copy and play the move.
        Piece[][] pieces = BoardEval.makeCopy(originalPieces);
        pieces[move[0]][move[1]].playMove(move[2], move[3], pieces);

        // Update possible moves for the computer
        ArrayList<int[]>[][] moveGen = BoardEval.updateControlledSquares(pieces);

        // Is the game over with the other side to move?
        if(BoardEval.gameOver(moveGen, pieces, -curSide) != -1)
            return BoardEval.gameOver(moveGen, pieces, -curSide);

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

        // Sort by captures
        possibleMoves = sortByCaptures(possibleMoves, pieces);

        double val = -10000.0;
        for(int[] toTest : possibleMoves){
            val = Math.max(val, -negamax(pieces, toTest, depth - 1, -beta, -alpha, -curSide));

            alpha = Math.max(alpha, val);
            if(alpha >= beta)
                break;
        }

        return val;
    }

    // Actual AI goes here
    // Find the 5 moves that offer the best position
    public void makeMove(Piece[][] pieces, boolean[][] squaresControlledW, boolean[][] squaresControlledB){
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
                    double score = negamax(pieces, move, DEPTH, -100000.0, 100000.0, 1);
                    System.out.println(score);

                    if(score > bestScore){
                        bestScore = score;
                        best = new int[]{move[0], move[1], move[2], move[3]};
                    }
                }

            } else if(this.side == 'b'){
                double bestScore = 100000.0;
                for(int[] move : movesB){
                    System.out.print("Considering move (" + move[0] + ", " + move[1] + ") to (" + move[2] + ", " + move[3] + ") --> ");
                    double score = negamax(pieces, move, DEPTH, -100000.0, 100000.0, -1);
                    System.out.println(score);

                    if(score < bestScore){
                        bestScore = score;
                        best = new int[]{move[0], move[1], move[2], move[3]};
                    }
                }
            }
        //}

        // Play the best move found
        pieces[best[0]][best[1]].playMove(best[2], best[3], pieces);

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
