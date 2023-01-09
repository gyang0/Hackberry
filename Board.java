/**
 * Draws the entire board, combining the Piece.java and Square.java
 *
 * **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JComponent implements MouseListener {
    private Square[][] squares;
    private Piece[][] pieces;
    private String boardState[][] = {
            {"br", "bn", "bb", "bq", "bk", "bb", "bn", "br"},
            {"bp", "bp", "bp", "bp", "bp", "bp", "bp", "bp"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"}
    };

    // The squares controlled by each side
    private boolean squaresControlledW[][];
    private boolean squaresControlledB[][];

    private final int NUM_SQUARES = 8;
    private final int SQUARE_WIDTH = 50;
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;

    private final Color WHITE = new Color(184,139,74);
    private final Color BLACK = new Color(227,193,111);
    private final Color OPAQUE_GRAY = new Color(100, 100, 100, 100);
    private final Color CHECK_COLOR = new Color(255, 0, 0, 100);

    private int numClicks = 0;
    private int[] prevCoords = {-1, -1};

    // First two indices hold the x and y position of white's move.
    // Third and fourth indices hold the x and y position of black's move.
    private int[] mostRecentPieceMov = {-1, -1};
    private String prevPieceType = "";
    private char prevPieceSide = ' ';
    private boolean myTurn = true;

    PromoOptions promoOption = new PromoOptions(0, 0, ' ');
    private boolean showPromoOptions = false;
    private int promoX;
    private int promoY;

    public boolean whiteKingInCheck = false;
    public boolean blackKingInCheck = false;

    public void initSquares() {
        squares = new Square[NUM_SQUARES][NUM_SQUARES];
        squaresControlledW = new boolean[NUM_SQUARES][NUM_SQUARES];
        squaresControlledB = new boolean[NUM_SQUARES][NUM_SQUARES];

        boolean flag = true;

        for (int i = 0; i < NUM_SQUARES; i++) {
            for (int j = 0; j < NUM_SQUARES; j++) {
                if (flag)
                    squares[i][j] = new Square(i, j, SQUARE_WIDTH, WHITE);
                else
                    squares[i][j] = new Square(i, j, SQUARE_WIDTH, BLACK);

                // Carrying on the color if at the end of the column
                if (j != NUM_SQUARES - 1)
                    flag = !flag;


                // Setting the squares controlled
                squaresControlledW[i][j] = false;
                squaresControlledB[i][j] = false;
            }
        }
    }

    public void initPieces(){
        pieces = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                // Reversed coordinates for display
                pieces[j][i] = new Piece(j, i, boardState[i][j], boardState[i][j].equals("") ? ' ' : boardState[i][j].charAt(0));
            }
        }
    }

    /**
     * Checks the squares controlled by the side specified
     *
     * @param arr - Array of the squares controlled to be updated, either from squaresControlledW or squaresControlledB.
     * @param whichSide - The side of the current side to check.
     * **/
    public void checkControlledSquares(boolean arr[][], char whichSide){
        // Reset
        for(int i = 0; i < NUM_SQUARES; i++)
            for(int j = 0; j < NUM_SQUARES; j++)
                arr[i][j] = false;

        if(whichSide == 'w')
            blackKingInCheck = false;
        else
            whiteKingInCheck = false;

        // Brute-force approach of iterating through every square, finding a piece, and going through again.
        // Worst case is 8^4 = 4096 iterations, which is fine but might need optimization later.

        Piece prev = new Piece(0, 0, "", ' ');

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){

                // Found a piece from that side.
                if(pieces[i][j].getSide() == whichSide){

                    // Go through board again and check off the controlled squares.
                    for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                        for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){
                            // For testing purposes, we set the piece of an opposite side at that square.
                            // Then we see if that piece can be captured.
                            prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide());
                            pieces[pos1][pos2].setPiece(pos1, pos2, whichSide == 'w' ? "bp" : "wp", whichSide == 'w' ? 'b' : 'w');

                            // Can make a capture (if necessary) at that position.
                            if(pieces[i][j].legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                                arr[pos1][pos2] = true;

                                // If the piece at that position is a king, it's in check.
                                if(prev.getType().equals("bk") && pieces[i][j].getSide() == 'w')
                                    blackKingInCheck = true;
                                else if(prev.getType().equals("wk") && pieces[i][j].getSide() == 'b')
                                    whiteKingInCheck = true;

                            }

                            pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide());
                        }
                    }
                }

            }
        }
    }

    public void updateControlledSquares(){
        checkControlledSquares(squaresControlledW, 'w');
        checkControlledSquares(squaresControlledB, 'b');
    }

    /**
     * Constructor for Board class.
     * Adds the mouse listener and initializes the squares and pieces.
     * **/
    public Board(){
        this.initSquares();
        this.initPieces();

        // Add mouse listener
        addMouseListener(this);
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
                    if(pieces[prevCoords[0]][prevCoords[1]].legalMove(i, j, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        g.setColor(OPAQUE_GRAY);
                        g.fillRoundRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH, 0, 0);
                    }
                }

                // King in check.
                if((blackKingInCheck && pieces[i][j].getType().equals("bk")) || (whiteKingInCheck && pieces[i][j].getType().equals("wk"))){
                    g.setColor(CHECK_COLOR);
                    g.fillRoundRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH, 0, 0);
                }

            }
        }

        for (int i = 0; i < NUM_SQUARES; i++){
            for (int j = 0; j < NUM_SQUARES; j++) {
                pieces[i][j].paint(g);
            }
        }


        // Search for a pawn on the back rank
        for(int i = 0; i < NUM_SQUARES; i++){
            if(pieces[i][0].getType().equals("wp")){
                promoOption.setPos(i, 0, pieces[i][0].getSide());
                this.showPromoOptions = true;
                promoX = i;
                promoY = 0;
            } else if(pieces[i][NUM_SQUARES - 1].getType().equals("bp")){
                promoOption.setPos(i, NUM_SQUARES - 1, pieces[i][NUM_SQUARES - 1].getSide());
                this.showPromoOptions = true;
                promoX = i;
                promoY = NUM_SQUARES - 1;
            }
        }

        // Opaque color scheme for better focus when promoting
        if(this.showPromoOptions){
            g.setColor(OPAQUE_GRAY);
            g.fillRoundRect(X_OFFSET - 10, Y_OFFSET - 10, NUM_SQUARES*SQUARE_WIDTH + 20, NUM_SQUARES*SQUARE_WIDTH + 20, 3, 3);

            promoOption.paint(g);
        }
    }


    /* Mouse events */
    @Override
    public void mouseClicked(MouseEvent e){
        if(showPromoOptions){
            int choice = promoOption.handleMouseInteractions(e.getX(), e.getY());

                boolean done = false;
                switch(choice){
                    case 0:
                        pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wr" : "br", promoOption.side);
                        done = true;
                    break;
                    case 1:
                        pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wn" : "bn", promoOption.side);
                        done = true;
                    break;
                    case 2:
                        pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wb" : "bb", promoOption.side);
                        done = true;
                    break;
                    case 3:
                        pieces[promoX][promoY].setPiece(promoX, promoY, promoOption.side == 'w' ? "wq" : "bq", promoOption.side);
                        done = true;
                    break;
                }

                if(done) {
                    showPromoOptions = false;
                    repaint();
                }
        }
        else {

            for (int i = 0; i < NUM_SQUARES; i++) {
                for (int j = 0; j < NUM_SQUARES; j++) {
                    if (squares[i][j].contains(e.getX(), e.getY())) {
                        // Second click
                        if (numClicks == 1) {
                            // Clicked same square (deselect)
                            if (i == prevCoords[0] && j == prevCoords[1]) {
                                squares[i][j].deselectSquare();
                            } else {
                                // Legal move
                                if (pieces[prevCoords[0]][prevCoords[1]].legalMove(i, j, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)) {
                                    Piece prev = new Piece(i, j, pieces[i][j].getType(), pieces[i][j].getSide());

                                    pieces[prevCoords[0]][prevCoords[1]].playMove(i, j, pieces);
                                    pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0], prevCoords[1], "", ' ');

                                    // Update controlled squares
                                    this.updateControlledSquares();

                                    // King is in check?
                                    if((whiteKingInCheck && myTurn) || (blackKingInCheck && !myTurn)){
                                        // Then we can't make that move.
                                        // Take it back.
                                        pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0], prevCoords[1], pieces[i][j].getType(), pieces[i][j].getSide());
                                        pieces[i][j].setPiece(i, j, prev.getType(), prev.getSide());

                                        squares[i][j].deselectSquare();
                                        squares[prevCoords[0]][prevCoords[1]].deselectSquare();
                                        prevCoords[0] = -1;
                                        prevCoords[1] = -1;
                                        numClicks = 0;

                                        this.updateControlledSquares();

                                        return;
                                    }


                                    // Successfully made a legal move
                                    myTurn = !myTurn;
                                    pieces[i][j].numMoves = pieces[prevCoords[0]][prevCoords[1]].numMoves + 1;
                                    mostRecentPieceMov[0] = i;
                                    mostRecentPieceMov[1] = j;

                                }

                                squares[prevCoords[0]][prevCoords[1]].deselectSquare();
                            }

                            // Reset
                            squares[i][j].deselectSquare();
                            prevCoords[0] = -1;
                            prevCoords[1] = -1;
                            numClicks = 0;

                        } else {
                            // Not a blank square
                            if (!pieces[i][j].getType().equals("")) {
                                if ((myTurn && pieces[i][j].getType().charAt(0) == 'w') ||
                                    (!myTurn && pieces[i][j].getType().charAt(0) == 'b')) {
                                    numClicks++;
                                    prevCoords[0] = i;
                                    prevCoords[1] = j;
                                    prevPieceType = pieces[i][j].getType();
                                    prevPieceSide = pieces[i][j].getSide();

                                    squares[i][j].selectSquare();
                                }
                            }
                        }

                        repaint();

                        // No point in going through the rest of the squares.
                        return;
                    }
                }
            }


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
