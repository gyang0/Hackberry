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

    // First char is for what side (white or black), second char is for the piece (N for knight, R for rook, etc.)
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
            {"bp", "bp", "wp", "bp", "bp", "bp", "bp", "bp"},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"", "", "", "", "", "", "", ""},
            {"wp", "wp", "wp", "wp", "wp", "wp", "wp", "wp"},
            {"wr", "wn", "wb", "wq", "wk", "wb", "wn", "wr"}
    };

    private final int NUM_SQUARES = 8;
    private final int SQUARE_WIDTH = 50;
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;

    private final Color WHITE = new Color(184,139,74);
    private final Color BLACK = new Color(227,193,111);
    private Color OPAQUE_GRAY = new Color(100, 100, 100, 100);

    private int numClicks = 0;
    private int[] prevCoords = {-1, -1};
    private String prevPieceType = "";
    private char prevPieceSide = ' ';
    private boolean myTurn = true;

    PromoOptions promoOption = new PromoOptions(0, 0, ' ');
    private boolean showPromoOptions = false;
    private int promoClicks = 0;
    private int promoX;
    private int promoY;

    public void initSquares() {
        squares = new Square[NUM_SQUARES][NUM_SQUARES];

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
                    if(pieces[prevCoords[0]][prevCoords[1]].legalMove(i, j, pieces)){
                        g.setColor(OPAQUE_GRAY);
                        g.fillRoundRect(i*SQUARE_WIDTH + X_OFFSET, j*SQUARE_WIDTH + Y_OFFSET, SQUARE_WIDTH, SQUARE_WIDTH, 0, 0);
                    }
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
                                if (pieces[prevCoords[0]][prevCoords[1]].legalMove(i, j, pieces)) {
                                    pieces[prevCoords[0]][prevCoords[1]].playMove(i, j, pieces);
                                    pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0], prevCoords[1], "", ' ');

                                    // Successfully made a legal move
                                    myTurn = !myTurn;
                                    pieces[i][j].numMoves++;
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
