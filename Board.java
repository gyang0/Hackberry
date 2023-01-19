/**
 * Draws the entire board, combining the Piece.java and Square.java
 * **/

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

public class Board extends JComponent implements MouseListener {
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

    // Just stores the positions of the pieces. (HashMap does the rest, below.)
    private Piece[][] pieces;
    private Square[][] squares;

    // HashMap of the pieces for each side and where they can move to.
    private HashMap<Piece, ArrayList<int[]>> piecesW;
    private HashMap<Piece, ArrayList<int[]>> piecesB;

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

    private Piece curPiece = new Piece(0, 0, "", ' '); // Currently selected piece

    private boolean myTurn = true;

    PromoOptions promoOption = new PromoOptions(0, 0, ' ');
    private boolean showPromoOptions = false;
    private int promoX;
    private int promoY;

    public boolean whiteKingInCheck = false;
    public boolean blackKingInCheck = false;


    Font MSG_FONT = new Font("serif", Font.BOLD, 20);
    private String message = "";

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
                    pieces[j][i] = new Piece(j, i, boardState[i][j], boardState[i][j].charAt(0));

                    // Reversed coordinates for display
                    if(boardState[i][j].charAt(0) == 'w')
                        piecesW.put(pieces[j][i], new ArrayList<int[]>());
                    else
                        piecesB.put(pieces[j][i], new ArrayList<int[]>());
                } else
                    pieces[j][i] = new Piece(j, i, "", ' ');
            }
        }
    }

    /**
     * Checks the squares controlled by the white side.
     * **/
    public void checkControlledSquaresW(){
        // Reset
        for(int i = 0; i < NUM_SQUARES; i++)
            for(int j = 0; j < NUM_SQUARES; j++)
                squaresControlledW[i][j] = false;

        blackKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ');

        // For every black piece
        for(Piece p : piecesW.keySet()){
            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){

                    // Having control over that square:
                    // For testing purposes, we set the piece of an opposite side at that square.
                    // Then we see if that piece can be captured.
                    prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide());
                    pieces[pos1][pos2].setPiece(pos1, pos2, "bp", 'b');

                    // Can make a capture (if necessary) at that position.
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        squaresControlledW[pos1][pos2] = true;

                        // If the piece at that position is a king, it's in check.
                        if(prev.getType().equals("bk"))
                            blackKingInCheck = true;
                    }

                    pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide());
                }
            }
        }

    }

    /**
     * Checks the squares controlled by the black side.
     * **/
    public void checkControlledSquaresB(){
        // Reset
        for(int i = 0; i < NUM_SQUARES; i++)
            for(int j = 0; j < NUM_SQUARES; j++)
                squaresControlledB[i][j] = false;

        whiteKingInCheck = false;
        Piece prev = new Piece(0, 0, "", ' ');

        // For every black piece
        for(Piece p : piecesB.keySet()){
            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){

                    // Having control over that square:
                    // For testing purposes, we set the piece of an opposite side at that square.
                    // Then we see if that piece can be captured.
                    prev.setPiece(pos1, pos2, pieces[pos1][pos2].getType(), pieces[pos1][pos2].getSide());
                    pieces[pos1][pos2].setPiece(pos1, pos2, "wp", 'w');

                    // Can make a capture (if necessary) at that position.
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        squaresControlledB[pos1][pos2] = true;

                        // If the piece at that position is a king, it's in check.
                        if(prev.getType().equals("wk"))
                            whiteKingInCheck = true;
                    }

                    pieces[pos1][pos2].setPiece(pos1, pos2, prev.getType(), prev.getSide());
                }
            }
        }

    }

    /**
     * Updates the positions every white piece can move to.
     * **/
    public void getPossibleMovesW(){
        // Reset
        for(Piece p : piecesW.keySet()) piecesW.put(p, new ArrayList<int[]>());
        Piece prev = new Piece(0, 0, "", ' ');

        // For every black piece
        for(Piece p : piecesW.keySet()){
            ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){
                    // Can move to that square
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        possibleMoves.add(new int[]{pos1,pos2});
                    }
                }
            }

            piecesW.put(p, possibleMoves);
        }
    }

    /**
     * Updates the positions every black piece can move to.
     * **/
    public void getPossibleMovesB(){
        // Reset
        for(Piece p : piecesB.keySet()) piecesB.put(p, new ArrayList<int[]>());

        // For every black piece
        for(Piece p : piecesB.keySet()){
            ArrayList<int[]> possibleMoves = new ArrayList<int[]>();

            // For every square on the board
            for(int pos1 = 0; pos1 < NUM_SQUARES; pos1++){
                for(int pos2 = 0; pos2 < NUM_SQUARES; pos2++){
                    // Can move to that square
                    if(p.legalMove(pos1, pos2, pieces, mostRecentPieceMov, squaresControlledW, squaresControlledB)){
                        possibleMoves.add(new int[]{pos1,pos2});
                    }
                }
            }

            piecesB.put(p, possibleMoves);
        }
    }

    /**
     * Removes the white pieces' moves that result in the white king being put in check.
     * **/
    public void removeIllegalMovesW(){
        Piece prev = new Piece(0, 0,  "", ' ');
        int prevX, prevY;

        for(Piece p : piecesW.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesW.get(p)){
                // See if it results in check
                prevX = p.getGridX();
                prevY = p.getGridY();
                prev.setPiece(arr[0], arr[1], pieces[arr[0]][arr[1]].getType(), pieces[arr[0]][arr[1]].getSide());
                p.playMove(arr[0], arr[1], pieces, piecesW, piecesB);
                this.checkControlledSquaresB();

                if(!whiteKingInCheck)
                    moves.add(arr);

                pieces[arr[0]][arr[1]].playMove(prevX, prevY, pieces, piecesW, piecesB);
                pieces[arr[0]][arr[1]].setPiece(arr[0], arr[1], prev.getType(), prev.getSide());
            }

            piecesW.put(p, moves);
        }

    }

    /**
     * Removes the black pieces' moves that result in the black king being put in check.
     * **/
    public void removeIllegalMovesB(){
        Piece prev = new Piece(0, 0,  "", ' ');
        int prevX, prevY;

        for(Piece p : piecesB.keySet()){

            ArrayList<int[]> moves = new ArrayList<int[]>();

            // Go through the possible listed moves
            for(int[] arr : piecesB.get(p)){
                // See if it results in check
                prevX = p.getGridX();
                prevY = p.getGridY();
                prev.setPiece(arr[0], arr[1], pieces[arr[0]][arr[1]].getType(), pieces[arr[0]][arr[1]].getSide());
                p.playMove(arr[0], arr[1], pieces, piecesW, piecesB);
                this.checkControlledSquaresW();

                if(!blackKingInCheck)
                    moves.add(arr);

                pieces[arr[0]][arr[1]].playMove(prevX, prevY, pieces, piecesW, piecesB);
                pieces[arr[0]][arr[1]].setPiece(arr[0], arr[1], prev.getType(), prev.getSide());
            }

            piecesB.put(p, moves);
        }

    }


    /**
     * Combined usage of checking possible squares and getting possible moves for all sides.
     * **/
    public void updateControlledSquares(){
        this.getPossibleMovesW();
        this.getPossibleMovesB();

        this.removeIllegalMovesW();
        this.removeIllegalMovesB();

        this.checkControlledSquaresW();
        this.checkControlledSquaresB();
    }



    /**
     * Constructor for Board class.
     * Adds the mouse listener and initializes the squares and pieces.
     * **/
    public Board(){
        piecesW = new HashMap<Piece, ArrayList<int[]>>();
        piecesB = new HashMap<Piece, ArrayList<int[]>>();

        this.initSquares();
        this.initPieces();

        this.updateControlledSquares();

        // Add mouse listener
        addMouseListener(this);
    }


    /**
     * Checks the specific piece's corresponding ArrayList to see if a move is legal.
     *
     * @param fromX - row number of the current piece.
     * @param fromY - column number of the current piece.
     * @param toX - row number of square to move to.
     * @param toY - column number of square to move to.
     * **/
    public boolean canMoveTo(int fromX, int fromY, int toX, int toY){
        if(curPiece.getSide() == 'w'){
            if(piecesW.get(pieces[fromX][fromY]) == null) return false;

            for(int[] arr : piecesW.get(pieces[fromX][fromY])){
                if(arr[0] == toX && arr[1] == toY)
                    return true;
            }

        } else if(curPiece.getSide() == 'b'){
            if(piecesB.get(pieces[fromX][fromY]) == null) return false;

            for(int[] arr : piecesB.get(pieces[fromX][fromY])){
                if(arr[0] == toX && arr[1] == toY)
                    return true;
            }
        }

        return false;
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
            this.updateControlledSquares();
            repaint();
        }
    }

    /**
     * Makes a move for the white side.
     *
     * @param i - The row number of the square to move to.
     * @param j - The column number of the square to move to.
     * **/
    public void whiteMove(int i, int j){
        Piece prev = new Piece(0, 0, "", ' ');

        // Go through possible moves for that piece and check for legal moves
        if (this.canMoveTo(prevCoords[0], prevCoords[1], i, j)) {
            prev.setPiece(i, j, pieces[i][j].getType(), pieces[i][j].getSide());
            pieces[prevCoords[0]][prevCoords[1]].playMove(i, j, pieces, piecesW, piecesB);

            piecesW.remove(pieces[prevCoords[0]][prevCoords[1]]);
            piecesW.put(pieces[i][j], new ArrayList<int[]>()); // Set the value to an empty ArrayList.

            // Update controlled squares
            this.updateControlledSquares();

            // King in check, can't move, backtrack changes.
            if(whiteKingInCheck){
                this.setMessage("King in check");

                pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0], prevCoords[1], pieces[i][j].getType(), pieces[i][j].getSide());
                pieces[i][j].setPiece(i, j, prev.getType(), prev.getSide());

                this.updateControlledSquares();
            } else {
                pieces[i][j].numMoves = pieces[prevCoords[0]][prevCoords[1]].numMoves + 1;
                mostRecentPieceMov[0] = i;
                mostRecentPieceMov[1] = j;
                myTurn = !myTurn;
            }
        }
        squares[prevCoords[0]][prevCoords[1]].deselectSquare();
    }


    /**
     * Makes a move for the black side.
     *
     * @param i - The row number of the square to move to.
     * @param j - The column number of the square to move to.
     * **/
    public void blackMove(int i, int j){
        Piece prev = new Piece(0, 0, "", ' ');

        // Go through possible moves for that piece and check for legal moves
        if (this.canMoveTo(prevCoords[0], prevCoords[1], i, j)) {
            prev.setPiece(i, j, pieces[i][j].getType(), pieces[i][j].getSide());
            pieces[prevCoords[0]][prevCoords[1]].playMove(i, j, pieces, piecesW, piecesB);

            piecesB.put(pieces[i][j], new ArrayList<int[]>()); // Set the value to an empty ArrayList.

            // Update controlled squares
            this.updateControlledSquares();

            // King in check, can't move, backtrack changes.
            if(blackKingInCheck){
                this.setMessage("King in check");

                pieces[prevCoords[0]][prevCoords[1]].setPiece(prevCoords[0], prevCoords[1], pieces[i][j].getType(), pieces[i][j].getSide());
                pieces[i][j].setPiece(i, j, prev.getType(), prev.getSide());

                this.updateControlledSquares();
            } else {
                pieces[i][j].numMoves = pieces[prevCoords[0]][prevCoords[1]].numMoves + 1;
                mostRecentPieceMov[0] = i;
                mostRecentPieceMov[1] = j;
                myTurn = !myTurn;
            }
        }
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


        // Piece images
        for(Piece p : piecesW.keySet())
            p.paint(g);
        for(Piece p : piecesB.keySet())
            p.paint(g);


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

        // Message
        g.setColor(Color.BLACK);
        g.setFont(MSG_FONT);
        g.drawString(message, 300, 550);
    }

    /* Mouse events */
    @Override
    public void mouseClicked(MouseEvent e){
        this.setMessage("");

        if(showPromoOptions){
            int choice = promoOption.handleMouseInteractions(e.getX(), e.getY());
            promotePawn(choice);
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
                    } else {
                        if(myTurn) whiteMove(i, j);
                        else blackMove(i, j);
                    }

                    // Reset
                    squares[i][j].deselectSquare();
                    prevCoords[0] = -1;
                    prevCoords[1] = -1;
                    numClicks = 0;
                }

                // First click (choice)
                else if(numClicks == 0){
                    // Chose a movable piece
                    if((myTurn && pieces[i][j].getSide() != 'w') || (!myTurn && pieces[i][j].getSide() != 'b'))
                        return;

                    // Currently selected piece
                    curPiece.setPiece(i, j, pieces[i][j].getType(), pieces[i][j].getSide());
                    squares[i][j].selectSquare();

                    prevCoords[0] = i;
                    prevCoords[1] = j;

                    numClicks++;
                }
            }

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
