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

    private final int NUM_SQUARES = 8;
    private final int SQUARE_WIDTH = 50;
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;

    private final Color WHITE = new Color(184,139,74);
    private final Color BLACK = new Color(227,193,111);

    private int numClicks = 0;
    private int[] prevCoords = {-1, -1};
    private String prevPieceType = "";

    public void initSquares(){
        squares = new Square[NUM_SQUARES][NUM_SQUARES];

        boolean flag = true;

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(flag)
                    squares[i][j] = new Square(i, j, SQUARE_WIDTH, WHITE);
                else
                    squares[i][j] = new Square(i, j, SQUARE_WIDTH, BLACK);

                // Carrying on the color if at the end of the column
                if(j != NUM_SQUARES - 1)
                    flag = !flag;
            }
        }
    }

    public void initPieces(){
        pieces = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                // Reversed coordinates for display
                pieces[j][i] = new Piece(j, i, boardState[i][j]);
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
                    if(pieces[prevCoords[0]][prevCoords[1]].legalMove(i, j)){
                        g.setColor(Color.GRAY);
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
    }


    /* Mouse events */
    @Override
    public void mouseClicked(MouseEvent e){
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(squares[i][j].contains(e.getX(), e.getY())){
                    // Second click
                    if(numClicks == 1){
                        // Clicked same square (deselect)
                        if(i == prevCoords[0] && j == prevCoords[1]) {
                            squares[i][j].deselectSquare();
                        } else {
                            // Legal move
                            if(pieces[prevCoords[0]][prevCoords[1]].legalMove(i, j)){
                                pieces[i][j].setPiece(i, j, prevPieceType);
                                pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0], prevCoords[1],"");
                            }

                            squares[prevCoords[0]][prevCoords[1]].deselectSquare();
                        }

                        // Reset
                        squares[i][j].deselectSquare();
                        prevCoords[0] = -1;
                        prevCoords[1] = -1;
                        numClicks = 0;

                    } else {
                        numClicks++;
                        prevCoords[0] = i;
                        prevCoords[1] = j;
                        prevPieceType = pieces[i][j].getType();

                        squares[i][j].selectSquare();
                    }

                    repaint();
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
