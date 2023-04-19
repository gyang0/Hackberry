/**
 * Draws the entire board, combining the Piece.java and Square.java.
 *
 * @author Gene Yang
 * @version April 3, 2023
 * **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends JComponent implements MouseListener {
    /*private String boardState[][] = {
            {"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"},
            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"}
    };*/
    private String boardState[][] = {
            {"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"},
            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", "wq"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {"wr", "wn", "wb", "", "wk", "wb", "wn", "wr"}
    };


    private HackberryAI hackberryAI; // AI
    private char userSide = 'w'; // User's side

    // The squares that can be moved to by each side
    private boolean[][] squaresControlledW;
    private boolean[][] squaresControlledB;


    // For each square, keeps track of the pieces that can potentially move there.
    ArrayList<int[]>[][] movableSquares;


    // Just stores the positions of the pieces.
    private Piece[][] pieces;

    private Square[][] squares;

    private final int NUM_SQUARES = 8;
    private final int SQUARE_WIDTH = 50;
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;
    private final Color WHITE = new Color(184,139,74);
    private final Color BLACK = new Color(227,193,111);
    private final Color OPAQUE_GRAY = new Color(100, 100, 100, 100);
    private final Color CHECK_COLOR = new Color(255, 0, 0, 100);
    private final Font MSG_FONT = new Font("serif", Font.BOLD, 20);

    private int numClicks = 0;
    private int[] prevCoords = {-1, -1};

    private Piece curPiece = new Piece(0, 0, "", ' ', 0); // Currently selected piece

    private boolean whiteTurn = true;

    PromoOptions promoOption = new PromoOptions(0, 0, ' ');
    private boolean showPromoOptions = false;
    private int promoX;
    private int promoY;

    private String message = "";
    private boolean gameOver = false;

    double boardScore = 0.0;

    /**
     * Shows a message to the user for help and also for debugging purposes.
     *
     * @param msg - The message to display.
     * **/
    public void setMessage(String msg){
        message = msg;
        repaint();
    }

    /**
     * Fills in the squares array with Square objects.
     * **/
    public void initSquares() {
        squares = new Square[NUM_SQUARES][NUM_SQUARES];
        squaresControlledW = new boolean[NUM_SQUARES][NUM_SQUARES];
        squaresControlledB = new boolean[NUM_SQUARES][NUM_SQUARES];

        boolean flag = true;

        for (int i = 0; i < NUM_SQUARES; i++) {
            for (int j = 0; j < NUM_SQUARES; j++) {
                if (flag) squares[i][j] = new Square(i, j, SQUARE_WIDTH, WHITE);
                else squares[i][j] = new Square(i, j, SQUARE_WIDTH, BLACK);

                // Carrying on the color if at the end of the column
                if (j != NUM_SQUARES - 1)
                    flag = !flag;

                // Setting the squares controlled
                squaresControlledW[i][j] = false;
                squaresControlledB[i][j] = false;
            }
        }
    }

    /**
     * Fills in the pieces array with Piece objects, and also puts pieces into piecesW and piecesB.
     * **/
    public void initPieces(){
        pieces = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(!boardState[i][j].equals("")){
                    pieces[j][i] = new Piece(j, i, boardState[i][j], boardState[i][j].charAt(0), 0);

                    // Reversed coordinates for display
                    /*if(boardState[i][j].charAt(0) == 'w')
                        piecesW.put(pieces[j][i], new ArrayList<int[]>());
                    else
                        piecesB.put(pieces[j][i], new ArrayList<int[]>());*/
                } else
                    pieces[j][i] = new Piece(j, i, "", ' ', 0);
            }
        }
    }

    public void update(){


        /*System.out.println("Updating...");
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++) {
                System.out.print("[" + i + ", " + j + "]: ");
                for(int[] arr : movableSquares[i][j])
                    System.out.print("[" + arr[0] + ", " + arr[1] + "] ");
                System.out.println();
            }
        }
        System.out.println();*/

        //boardScore = BoardEval.getEval(movableSquares, pieces);

        /*piecesW = BoardEval.getPossibleMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesW);
        piecesB = BoardEval.getPossibleMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, piecesB);

        piecesW = BoardEval.removeIllegalMovesW(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);
        piecesB = BoardEval.removeIllegalMovesB(squaresControlledW, squaresControlledB, pieces, mostRecentPieceMov, prevCoords, piecesW, piecesB);

        BoardEval.checkControlledSquaresW(squaresControlledW, squaresControlledB, pieces);
        BoardEval.checkControlledSquaresB(squaresControlledW, squaresControlledB, pieces);

        piecesW = BoardEval.cleanUpHashMap(piecesW, 'w');
        piecesB = BoardEval.cleanUpHashMap(piecesB, 'b');

        BoardEval.givePieceScores(piecesW, piecesB);*/

        // Checkmate or stalemate
        //if(BoardEval.possibleMovesW == 0 && whiteTurn) gameOver = true;
        //if(BoardEval.possibleMovesB == 0 && !whiteTurn) gameOver = true;
    }

    public boolean checkGameOver() {
        // Checkmate by black
        /*if (BoardEval.whiteKingInCheck() && BoardEval.possibleMovesW == 0 && whiteTurn) {
            setMessage("Game over - Black wins by checkmate.");
            return true;
        }

        // Checkmate by white
        if (BoardEval.blackKingInCheck() && BoardEval.possibleMovesB == 0 && !whiteTurn) {
            setMessage("Game over - White wins by checkmate.");
            return true;
        }

        // Stalemate by black
        if (!BoardEval.whiteKingInCheck() && BoardEval.possibleMovesW == 0 && whiteTurn) {
            setMessage("Game over - Stalemate.");
            return true;
        }

        // Stalemate by white
        if (!BoardEval.blackKingInCheck() && BoardEval.possibleMovesB == 0 && !whiteTurn) {
            setMessage("Game over - Stalemate.");
            return true;
        }*/

        return false;
    }


    /**
     * Constructor for Board class.
     * Adds the mouse listener and initializes the squares and pieces.
     * **/
    public Board(){
        //piecesW = new HashMap<Piece, ArrayList<int[]>>();
        //piecesB = new HashMap<Piece, ArrayList<int[]>>();

        this.initSquares();
        this.initPieces();


        // AI
        hackberryAI = new HackberryAI(userSide == 'w' ? 'b' : 'w', 2);

        // Add mouse listener
        addMouseListener(this);

        movableSquares = BoardEval.updateControlledSquares(pieces);
    }


    /**
     * Checks the specific piece's corresponding ArrayList to see if a move is legal.
     *
     * @param fromX - row number of the current piece.
     * @param fromY - column number of the current piece.
     * @param toX - row number of square to move to.
     * @param toY - column number of square to move to.
     * **/
    public boolean canMoveTo(int fromX, int fromY, int toX, int toY) {
        for (int[] arr : movableSquares[toX][toY]){
            if (arr[0] == fromX && arr[1] == fromY)
                return true;
        }

        return false;
    }

    /**
     * Checks if promotion is possible at the current board state.
     * Updates boolean showPromoOptions.
     * */
    public void checkPromotion(){
        // Search for a pawn on the back rank
        for(int i = 0; i < NUM_SQUARES; i++){
            if(pieces[i][0].getType().equals("wp") && this.userSide == 'w'){
                promoOption.setPos(i, 0, pieces[i][0].getSide());
                this.showPromoOptions = true;
                promoX = i;
                promoY = 0;
            } else if(pieces[i][NUM_SQUARES - 1].getType().equals("bp") && this.userSide == 'b'){
                promoOption.setPos(i, NUM_SQUARES - 1, pieces[i][NUM_SQUARES - 1].getSide());
                this.showPromoOptions = true;
                promoX = i;
                promoY = NUM_SQUARES - 1;
            }
        }
    }

    /**
     * Replaces a pawn with the user's choice piece for promotion.
     *
     * @param choice - The ID of the piece for the promotion option (0 - rook, 1 - knight, etc.)
     * **/
    public void promotePawn(int choice){
        boolean done = false;
        switch(choice){
            case 0:
                pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wr" : "br", promoOption.side, 0);
                done = true;
            break;
            case 1:
                pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wn" : "bn", promoOption.side, 0);
                done = true;
            break;
            case 2:
                pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wb" : "bb", promoOption.side, 0);
                done = true;
            break;
            case 3:
                pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wq" : "bq", promoOption.side, 0);
                done = true;
            break;
        }

        if(done) {
            showPromoOptions = false;
            movableSquares = BoardEval.updateControlledSquares(pieces);
            repaint();
        }
    }

    /**
     * Makes a move for the user's side.
     *
     * @param i - The row number of the square to move to.
     * @param j - The column number of the square to move to.
     * **/
    public void makeMove(int i, int j){
        pieces[prevCoords[0]][prevCoords[1]].playMove(i, j, pieces);

        // A few changes to make.
        //pieces[i][j].numMoves = pieces[prevCoords[0]][prevCoords[1]].numMoves + 1;
        pieces[i][j].setBaseValue(pieces[prevCoords[0]][prevCoords[1]].getBaseValue());

        pieces[prevCoords[0]][prevCoords[1]].numMoves = 0;
        squares[prevCoords[0]][prevCoords[1]].deselectSquare();
    }


    @Override
    public void paintComponent(Graphics g) {
        // Board outline
        g.setColor(BLACK);
        g.fillRoundRect(X_OFFSET - 10, Y_OFFSET - 10, NUM_SQUARES*SQUARE_WIDTH + 20, NUM_SQUARES*SQUARE_WIDTH + 20, 3, 3);


        for (int i = 0; i < NUM_SQUARES; i++){
            for (int j = 0; j < NUM_SQUARES; j++) {
                squares[i][j].paint(g);

                // Selected a piece
                if(prevCoords[0] != -1 && prevCoords[1] != -1){
                    if(this.canMoveTo(prevCoords[0], prevCoords[1], i, j)){
                        //System.out.println("Can move to [" + i + ", " + j + "] ");

                        g.setColor(OPAQUE_GRAY);
                        g.fillRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH);
                    }
                }

                // King in check.
                /*if((BoardEval.blackKingInCheck() && pieces[i][j].getType().equals("bk")) ||
                   (BoardEval.whiteKingInCheck() && pieces[i][j].getType().equals("wk"))){
                    g.setColor(CHECK_COLOR);
                    g.fillRoundRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH, 0, 0);

                    //System.out.println("it's a check");
                }*/

                if(squaresControlledW[i][j]){
                    g.setColor(new Color(0, 0, 255, 50));
                    g.fillRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH);
                }
                if(squaresControlledB[i][j]){
                    g.setColor(new Color(255, 0, 0, 50));
                    g.fillRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH);
                }

                g.setColor(Color.BLACK);
                //g.drawString(String.valueOf(pieces[i][j].hashCode()), i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET);
                g.drawString(String.valueOf(pieces[i][j].getValue()), i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET + 20);
                g.drawString(i + ", " + j, i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET + 30);

                // Piece images
                if(pieces[i][j].getSide() != ' ')
                    pieces[i][j].paint(g);
            }
        }

        //g.setColor(new Color(255, 255, 0, 100));
        //g.fillRoundRect(mostRecentPieceMov[0]*SQUARE_WIDTH + X_OFFSET, mostRecentPieceMov[1]*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH, 0, 0);


        // Opaque color scheme for better focus when promoting
        if(showPromoOptions){
            g.setColor(OPAQUE_GRAY);
            g.fillRoundRect(X_OFFSET - 10, Y_OFFSET - 10, NUM_SQUARES*SQUARE_WIDTH + 20, NUM_SQUARES*SQUARE_WIDTH + 20, 3, 3);

            promoOption.paint(g);
        }

        // Message
        g.setColor(Color.BLACK);
        g.setFont(MSG_FONT);

        // Centering text
        FontMetrics f = g.getFontMetrics();
        g.drawString(message, 300 - (f.stringWidth(message)/2), 550);
        //g.drawString("White has " + BoardEval.possibleMovesW + " moves", 200, 600);
        //g.drawString("Black has " + BoardEval.possibleMovesB + " moves", 200, 650);

        //this.givePieceScores();
        //g.drawString("Board evaluation: " + (int)(BoardEval.boardScore() * 100000)/100000.0, 270, 570);
    }

    /* Mouse events */
    @Override
    public void mouseClicked(MouseEvent e){
        if(this.gameOver) // Ignore any interactions after the game.
            return;

        if(showPromoOptions){
            int choice = promoOption.handleMouseInteractions(e.getX(), e.getY());
            promotePawn(choice);

            if(choice == -1) return;

            //update(userSide == 'w' ? 'b' : 'w');

            hackberryAI.makeMove(pieces, squaresControlledW, squaresControlledB);
            whiteTurn = !whiteTurn;

            // Update controlled squares
            movableSquares = BoardEval.updateControlledSquares(pieces);
            if(checkGameOver()){
                repaint();
                return;
            }


            repaint();
            return;
        }

        else {
            // Did we click a valid square?
            if (e.getX() >= X_OFFSET && e.getX() <= X_OFFSET + NUM_SQUARES * SQUARE_WIDTH &&
                    e.getY() >= Y_OFFSET && e.getY() <= Y_OFFSET + NUM_SQUARES * SQUARE_WIDTH) {
                int i = (e.getX() - X_OFFSET) / SQUARE_WIDTH; // Taking advantage of integer division.
                int j = (e.getY() - Y_OFFSET) / SQUARE_WIDTH; // Used for quick square collisions.

                // Second click (move)
                if(numClicks == 1){
                    // Clicked same square (deselect)
                    if (i == prevCoords[0] && j == prevCoords[1]) {
                        squares[i][j].deselectSquare();
                        numClicks = 0;
                        prevCoords[0] = -1; prevCoords[1] = -1;
                        repaint();
                        return;
                    } else {
                        // Go through possible moves for that piece and check for legal moves
                        if(this.canMoveTo(prevCoords[0], prevCoords[1], i, j)) {
                            makeMove(i, j);
                            whiteTurn = !whiteTurn;

                            this.checkPromotion();
                            if(showPromoOptions){
                                repaint();
                                numClicks = 0;
                                return;
                            }
                        }
                        else return;
                    }

                    // Reset
                    squares[i][j].deselectSquare();
                    prevCoords[0] = -1;
                    prevCoords[1] = -1;
                    numClicks++;
                }
                // First click (choice)
                else if(numClicks == 0){
                    // Chose a movable piece
                    if((whiteTurn && pieces[i][j].getSide() != 'w') || (!whiteTurn && pieces[i][j].getSide() != 'b'))
                        return;

                    // Currently selected piece
                    curPiece.setPiece(i, j, pieces[i][j].getType(), pieces[i][j].getSide(), pieces[i][j].numMoves);
                    squares[i][j].selectSquare();

                    prevCoords[0] = i;
                    prevCoords[1] = j;

                    numClicks++;
                    repaint();
                }
            }
        }


        if(numClicks == 2) {

            // If the user checkmated the computer
            if(checkGameOver()){
                repaint();
                return;
            }

            hackberryAI.makeMove(pieces, squaresControlledW, squaresControlledB);

            // Update possible moves for the user
            movableSquares = BoardEval.updateControlledSquares(pieces);

            if(checkGameOver()){
                repaint();
                return;
            }

            whiteTurn = !whiteTurn;

            numClicks = 0;
            repaint();
        }
    }

    @Override
    public void mousePressed(MouseEvent e){}

    @Override
    public void mouseReleased(MouseEvent e){}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
