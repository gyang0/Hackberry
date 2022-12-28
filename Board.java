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
    private char boardState[][] = {
            {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'},
            {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
            {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
    };

    private final int NUM_SQUARES = 8;
    private final int SQUARE_WIDTH = 50;
    private final Color WHITE = new Color(0xFFFFFF);
    private final Color BLACK = new Color(0x000000);

    private final int x = 100;
    private final int y = 100;

    public void initSquares(){
        squares = new Square[NUM_SQUARES][NUM_SQUARES];

        boolean flag = true;

        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(flag)
                    squares[i][j] = new Square(x + i * SQUARE_WIDTH, y + j * SQUARE_WIDTH, SQUARE_WIDTH, WHITE);
                else
                    squares[i][j] = new Square(x + i * SQUARE_WIDTH, y + j * SQUARE_WIDTH, SQUARE_WIDTH, BLACK);

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
        /*
        * Set squares[i][j].curPiece to something of type "Piece."
        * */
    }

    public Board(){
        this.initSquares();
        this.initPieces();


        // Adds mouse listener
        addMouseListener(this);
    }

    public Square[][] getSquares(){
        return this.squares;
    }

    @Override
    public void paintComponent(Graphics g) {
        // Board outline
        g.setColor(BLACK);
        g.fillRoundRect(this.x - 10, this.y - 10, NUM_SQUARES*SQUARE_WIDTH + 20, NUM_SQUARES*SQUARE_WIDTH + 20, 3, 3);

        for (int i = 0; i < NUM_SQUARES; i++){
            for (int j = 0; j < NUM_SQUARES; j++) {
                squares[i][j].paint(g);
            }
        }
    }


    /* Mouse events */
    @Override
    public void mouseClicked(MouseEvent e){
        for(int i = 0; i < NUM_SQUARES; i++){
            for(int j = 0; j < NUM_SQUARES; j++){
                if(squares[i][j].contains(e.getX(), e.getY())){
                    squares[i][j].selectCurSquare();
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
