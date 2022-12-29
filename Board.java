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

    private final Color WHITE = new Color(0xFFFFFF);
    private final Color BLACK = new Color(0x000000);

    private int numClicks = 0;
    private int[] prevCoords = {-1, -1};
    private String prevPieceType = "";

    public void initSquares(){
        squares = new Square[NUM_SQUARES][NUM_SQUARES];

        boolean flag = true;

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(flag)
                    squares[i][j] = new Square(X_OFFSET + i * SQUARE_WIDTH, Y_OFFSET + j * SQUARE_WIDTH, SQUARE_WIDTH, WHITE);
                else
                    squares[i][j] = new Square(X_OFFSET + i * SQUARE_WIDTH, Y_OFFSET + j * SQUARE_WIDTH, SQUARE_WIDTH, BLACK);

                // Carrying on the color if at the end of the column
                if(j != NUM_SQUARES - 1)
                    flag = !flag;
            }
        }
    }


    /*
    public void placePiece(int i, int j, char pieceType){
        switch(pieceType){
            case 'R':
                if(j == 0) pieces[i][j] = new Piece(i, j, "black-rook");
                else pieces[i][j] = new Piece(i, j, "white-rook");
            break;
            case 'N':
                if(j == 0) pieces[i][j] = new Piece(i, j, "black-knight");
                else pieces[i][j] = new Piece(i, j, "white-knight");
            break;
            case 'B':
                if(j == 0) pieces[i][j] = new Piece(i, j, "black-bishop");
                else pieces[i][j] = new Piece(i, j, "white-bishop");
            break;
            case 'Q':
                if(j == 0) pieces[i][j] = new Piece(i, j, "black-queen");
                else pieces[i][j] = new Piece(i, j, "white-queen");
            break;
            case 'K':
                if(j == 0) pieces[i][j] = new Piece(i, j, "black-king");
                else pieces[i][j] = new Piece(i, j, "white-king");
            break;
            case 'P':
                if(j == 1) pieces[i][j] = new Piece(i, j, "black-pawn");
                else pieces[i][j] = new Piece(i, j, "white-pawn");
            break;

            default:
                // None
        }
    }*/

    public void initPieces(){
        pieces = new Piece[NUM_SQUARES][NUM_SQUARES];

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                pieces[i][j] = new Piece(i * SQUARE_WIDTH + X_OFFSET, j * SQUARE_WIDTH + Y_OFFSET, boardState[i][j]);
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
                        numClicks = 0;
                        pieces[i][j].setPiece(i * SQUARE_WIDTH + X_OFFSET, j * SQUARE_WIDTH + Y_OFFSET, prevPieceType);
                        pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0] * SQUARE_WIDTH + X_OFFSET,
                                                                      prevCoords[1] * SQUARE_WIDTH + Y_OFFSET,
                                                                    "");

                        squares[i][j].deselectSquare();
                        squares[prevCoords[0]][prevCoords[1]].deselectSquare();

                        // Reset
                        prevCoords[0] = -1;
                        prevCoords[1] = -1;

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


        /* Pseudocode
        * IF clicked in a square
        *       numClicks++;
        *
        *       IF numClicks == 1
        *           numClicks = 0;
        *           Move the piece to that square.
        *           if(pieces[selectedX][selectedY].legalMove(i, j))
        *               pieces[selectedX][selectedY].moveTo(i, j);
        *               selectedX = -1;
        *               selectedY = -1;
        *               pieces[i][j] = null;
        *
        *       ELSE
        *           select that square
        *           selectedX = that_square_x;
         *          selectedY = that_square_y;
        *
        * */
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
