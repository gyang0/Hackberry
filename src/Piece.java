import javax.swing.*;
import java.awt.*;

/**
 * Individual pieces on the chessboard.
 * 
 * @author Gene Yang
 * @version April 26, 2023
 * **/

public class Piece {
    private String type;
    private char side;

    private double baseValue; // Fixed value for each piece
    private double value; // Changeable value depending on the position

    public int numMoves = 0;

    // this.x and this.y are the displaying coordinates.
    private int x;
    private int y;

    // this.gridX and this.gridY are the grid-type coordinates ( Like (0, 1), (1, 1), etc.).
    private int gridX;
    private int gridY;

    private final int SQUARE_WIDTH = 50;
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;
    private final int NUM_SQUARES = 8;

    public boolean recentlyMoved;

    public double assignValue(){
        if(this.type.equals("")) return 0;

        switch(this.type.charAt(1)){
            case 'p': return 1;
            case 'r': return 5;
            case 'n': return 3;
            case 'b': return 3.5;
            case 'q': return 10;
            case 'k': return 1000;
        }

        return 0;
    }

    public Piece(){
        this.x = -1;
        this.y = -1;

        this.gridX = -1;
        this.gridY = -1;

        this.type = "";
        this.side = ' ';

        this.baseValue = 0;
        this.value = 0;

        this.recentlyMoved = false;
    }

    public Piece(Piece p){
        this.x = p.x;
        this.y = p.y;

        this.gridX = p.gridX;
        this.gridY = p.gridY;

        this.type = p.type;
        this.side = p.side;

        this.numMoves = p.numMoves;
        this.baseValue = p.baseValue;
        this.value = p.value;

        this.recentlyMoved = p.recentlyMoved;
    }

    public Piece(int i, int j, String type, char side, int numMoves){
        this.x = i * SQUARE_WIDTH + X_OFFSET;
        this.y = j * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = i;
        this.gridY = j;

        this.type = type;
        this.side = side;

        this.numMoves = numMoves;
        this.baseValue = this.assignValue();
        this.value = this.baseValue;

        this.recentlyMoved = false;
    }

    public void setPiece(int x, int y, String type, char side, int numMoves){
        this.x = x * SQUARE_WIDTH + X_OFFSET;
        this.y = y * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = x;
        this.gridY = y;

        this.type = type;
        this.side = side;

        this.numMoves = numMoves;
        this.baseValue = this.assignValue();
        this.value = this.baseValue;
    }
    public void setValue(double value){ this.value = value; }
    public void setBaseValue(double baseValue){ this.baseValue = baseValue; }

    public int getGridX(){ return this.gridX; }
    public int getGridY(){ return this.gridY; }
    public String getType(){
        return this.type;
    }
    public char getSide(){ return this.side; }
    public double getValue(){ return this.value; }
    public double getBaseValue(){ return this.baseValue; }

    public void paint(Graphics g){
        if(!this.type.equals("")){
            ImageIcon img = switch (this.type) {
                case "wp" -> WHITE_PAWN_IMG;
                case "wr" -> WHITE_ROOK_IMG;
                case "wn" -> WHITE_KNIGHT_IMG;
                case "wb" -> WHITE_BISHOP_IMG;
                case "wq" -> WHITE_QUEEN_IMG;
                case "wk" -> WHITE_KING_IMG;
                case "bp" -> BLACK_PAWN_IMG;
                case "br" -> BLACK_ROOK_IMG;
                case "bn" -> BLACK_KNIGHT_IMG;
                case "bb" -> BLACK_BISHOP_IMG;
                case "bq" -> BLACK_QUEEN_IMG;
                default -> BLACK_KING_IMG;
            };

            Image i = img.getImage();
            g.drawImage(i, this.x, this.y, SQUARE_WIDTH, SQUARE_WIDTH, null);
        }
    }

    /**
     * Checks if a move is legal for a rook.
     *
     * @param toX - The x-position of the square the rook is trying to move to.
     * @param toY - The y-position of the square the rook is trying to move to.
     * @param pieces - Array of pieces used in game.
     * @return - True if move is legal, false if otherwise.
     * **/
    public boolean rookCheck(int toX, int toY, Piece[][] pieces){
        int xPos = this.gridX,
            yPos = this.gridY;

        // Vertical
        if(this.gridX == toX){
            if(toY > this.gridY){
                while(yPos < toY){
                    if(pieces[xPos][yPos].side != ' ' && yPos != this.gridY) break;
                    yPos++;
                }
            } else if(toY < this.gridY){
                while(yPos > toY){
                    if(pieces[xPos][yPos].side != ' ' && yPos != this.gridY) break;
                    yPos--;
                }
            }
        }

        // Horizontal
        if(toY == this.gridY){
            if(toX > this.gridX) {
                while (xPos < toX) {
                    if (pieces[xPos][yPos].side != ' ' && xPos != this.gridX) break;
                    xPos++;
                }
            } else if(toX < this.gridX){
                while (xPos > toX) {
                    if (pieces[xPos][yPos].side != ' ' && xPos != this.gridX) break;
                    xPos--;
                }
            }
        }

        if(xPos == toX && yPos == toY) return true;

        return false;
    }

    /**
     * Checks if a move is legal for a bishop.
     *
     * @param toX - The x-position of the square the rook is trying to move to.
     * @param toY - The y-position of the square the rook is trying to move to.
     * @param pieces - Array of pieces used in game.
     * @return - True if move is legal, false if otherwise.
     * **/
    public boolean bishopCheck(int toX, int toY, Piece[][] pieces){
        int xPos = this.gridX,
            yPos = this.gridY;

        // While loop time
        if(toX > this.gridX && toY > this.gridY){
            while(toX > xPos && toY > yPos){
                if(pieces[xPos][yPos].side != ' ' && xPos != this.gridX && yPos != this.gridY) break;
                xPos++;
                yPos++;
            }
        } else if(toX < this.gridX && toY > this.gridY){
            while(toX < xPos && toY > yPos){
                if(pieces[xPos][yPos].side != ' ' && xPos != this.gridX && yPos != this.gridY) break;
                xPos--;
                yPos++;
            }
        } else if(toX > this.gridX && toY < this.gridY){
            while(toX > xPos && toY < yPos){
                if(pieces[xPos][yPos].side != ' ' && xPos != this.gridX && yPos != this.gridY) break;
                xPos++;
                yPos--;
            }
        } else if(toX < this.gridX && toY < this.gridY){
            while(toX < xPos && toY < yPos){
                if(pieces[xPos][yPos].side != ' ' && xPos != this.gridX && yPos != this.gridY) break;
                xPos--;
                yPos--;
            }
        }

        if(xPos == toX && yPos == toY) return true;

        return false;
    }

    /**
     * Checks if there are any kings nearby, helps remove illegal moves resulting in a king moving next to a king.
     * @param toX - The x-position of the square we're trying to move to.
     * @param toY - The y-position of the square we're trying to move to.
     * @param pieces - Array of pieces used in game.
     * @return True if there's a king within 1 square of (toX, toY), not including the square (toX, toY).
     */
    public boolean noKingNearby(int toX, int toY, Piece[][] pieces){
        String oppositeSide = (this.type.equals("wk") ? "bk": "wk");

        for(int dx = -1; dx <= 1; dx++){
            for(int dy = -1; dy <= 1; dy++){
                // Out-of-bounds
                if(toX + dx < 0 || toX + dx >= NUM_SQUARES || toY + dy < 0 || toY + dy >= NUM_SQUARES)
                    continue;

                if(pieces[toX + dx][toY + dy].getType().equals(oppositeSide))
                    return false;
            }
        }

        return true;
    }

    /**
     * Checks if a move is simply legal, with checks, pins, etc.
     * Moves passed to it MUST HAVE PASSED THE INITIAL CHECK. This is in BoardEval.
     * @param toX
     * @param toY
     * @param pieces
     * @param squaresControlledW
     * @param squaresControlledB
     * @return
     */
    public boolean legalMove(int toX, int toY, Piece[][] pieces, boolean[][] squaresControlledW, boolean[][] squaresControlledB){
        // Can't capture pieces on its own side
        if(pieces[toX][toY].side == this.side)
            return false;

        // Can't move to same square
        if(toX == this.gridX && toY == this.gridY)
            return false;

        switch(this.type){
            case "wp":
                // If the move is diagonal
                if(Math.abs(this.gridX - toX) == 1 && this.gridY - 1 == toY){
                    // A normal capture
                    if(pieces[toX][toY].getSide() == 'b')
                        return true;

                    // En passant
                    else if(pieces[toX][toY].getSide() == ' '){
                        if(pieces[toX][toY + 1].getType().equals("bp")){
                            if(pieces[toX][toY + 1].recentlyMoved && pieces[toX][toY + 1].numMoves == 1)
                                return true;
                        }
                    }
                }

                // Normal forward movement
                if(this.gridX == toX && pieces[toX][toY].side == ' '){
                    if(this.gridY == toY + 1) return true;

                    // If the pawn hasn't moved yet, it can move 2 spaces.
                    else if(this.numMoves == 0 && toY == this.gridY - 2 && pieces[toX][toY + 1].side == ' ') return true;
                }
            break;

            case "bp":
                // If the move is diagonal
                if(Math.abs(this.gridX - toX) == 1 && this.gridY + 1 == toY){
                    // A normal capture
                    if(pieces[toX][toY].getSide() == 'w')
                        return true;

                        // En passant
                    else if(pieces[toX][toY].getSide() == ' '){
                        if(pieces[toX][toY - 1].getType().equals("wp")){
                            if(pieces[toX][toY - 1].recentlyMoved && pieces[toX][toY - 1].numMoves == 1)
                                return true;
                        }
                    }
                }

                // Normal forward movement
                if(this.gridX == toX && pieces[toX][toY].side == ' '){
                    if(this.gridY + 1 == toY) return true;

                        // If the pawn hasn't moved yet, it can move 2 spaces.
                    else if(this.numMoves == 0 && this.gridY + 2 == toY && pieces[toX][toY - 1].side == ' ') return true;
                }
            break;

            case "wk":
                // Kingside castling
                if(this.numMoves == 0 && toX == 6 && toY == this.gridY && this.gridX == 4 && this.gridY == 7){
                    // Rook hasn't moved
                    if(pieces[7][this.gridY].getType().equals("wr") && pieces[7][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[5][this.gridY].side == ' ' && pieces[6][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledB[4][this.gridY] && !squaresControlledB[5][this.gridY] && !squaresControlledB[6][this.gridY])
                               return true;
                    }
                }

                // Queenside castling
                else if(this.numMoves == 0 && toX == 2 && toY == this.gridY && this.gridX == 4 && this.gridY == 7){
                    // Rook hasn't moved
                    if(pieces[0][this.gridY].getType().equals("wr") && pieces[0][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[1][this.gridY].side == ' ' && pieces[2][this.gridY].side == ' ' && pieces[3][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledB[4][this.gridY] && !squaresControlledB[1][this.gridY] && !squaresControlledB[2][this.gridY] && !squaresControlledB[3][this.gridY])
                                return true;
                    }
                }
                else if(Math.abs(toX - this.gridX) <= 1 && Math.abs(toY - this.gridY) <= 1 && !squaresControlledB[toX][toY]) {
                    // Because of the way controlled squares are defined (if the piece can capture other pieces),
                    // extra conditions are needed for kings facing each other.
                    if(noKingNearby(toX, toY, pieces)) return true;
                    else return false;
                }
            break;

            case "bk":
                // Kingside castling
                if(this.numMoves == 0 && toX == 6 && toY == this.gridY && this.gridX == 4 && this.gridY == 0){
                    // Rook hasn't moved
                    if(pieces[7][this.gridY].getType().equals("br") && pieces[7][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[5][this.gridY].side == ' ' && pieces[6][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledW[4][this.gridY] && !squaresControlledW[5][this.gridY] && !squaresControlledW[6][this.gridY])
                                return true;
                    }
                }

                // Queenside castling
                else if(this.numMoves == 0 && toX == 2 && toY == this.gridY && this.gridX == 4 && this.gridY == 0){
                    // Rook hasn't moved
                    if(pieces[0][this.gridY].getType().equals("br") && pieces[0][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[1][this.gridY].side == ' ' && pieces[2][this.gridY].side == ' ' && pieces[3][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledW[4][this.gridY] && !squaresControlledW[1][this.gridY] && !squaresControlledW[2][this.gridY] && !squaresControlledW[3][this.gridY])
                                return true;
                    }
                }
                else if(Math.abs(toX - this.gridX) <= 1 && Math.abs(toY - this.gridY) <= 1 && !squaresControlledW[toX][toY]) {
                    // Because of the way controlled squares are defined (if the piece can capture other pieces),
                    // extra conditions are needed for kings facing each other.
                    if(noKingNearby(toX, toY, pieces)) return true;
                    else return false;
                }
            break;
        }

        return true;
    }

    /**
     * Plays out the move on the board and checks for special moves (en passant, castling, etc.)
     * @param i - Row number of square to move to.
     * @param j - Column number of square to move to.
     * @param pieces - Array of Piece objects in the board.
     * **/
    public void playMove(int i, int j, Piece[][] pieces) {
        // En passant
        // The target pawn must have moved in the last move, moved two squares up, and be next to the current pawn.
        if (this.type.equals("wp") && j == this.gridY - 1 && Math.abs(i - this.gridX) == 1 && pieces[i][j].getSide() == ' ') {
            pieces[i][j].setPiece(i, j, this.type, this.side, this.numMoves);
            pieces[i][j + 1].setPiece(i, j + 1, "", ' ', 0);
        } else if (this.type.equals("bp") && j == this.gridY + 1 && Math.abs(i - this.gridX) == 1 && pieces[i][j].getSide() == ' ') {
            pieces[i][j].setPiece(i, j, this.type, this.side, this.numMoves);
            pieces[i][j - 1].setPiece(i, j - 1, "", ' ', 0);
        }

        // Castling - white, kingside & queenside
        else if (this.type.equals("wk") && this.numMoves == 0 && this.gridX == 4 && this.gridY == 7) {
            if (i == 6 && j == 7 && pieces[7][7].getType().equals("wr") && pieces[7][7].numMoves == 0) {
                pieces[5][7].setPiece(5, 7, "wr", 'w', 0);
                pieces[6][7].setPiece(6, 7, "wk", 'w', 0);
                pieces[7][7].setPiece(7, 7, "", ' ', 0);

            } else if (i == 2 && j == 7 && pieces[0][7].getType().equals("wr") && pieces[0][7].numMoves == 0) {
                pieces[3][7].setPiece(3, 7, "wr", 'w', 0);
                pieces[2][7].setPiece(2, 7, "wk", 'w', 0);
                pieces[0][7].setPiece(0, 7, "", ' ', 0);
            }
        }
        // Castling - black, kingside & queenside
        else if (this.type.equals("bk") && this.numMoves == 0 && this.gridX == 4 && this.gridY == 0) {
            if (i == 6 && j == 0 && pieces[7][0].getType().equals("br") && pieces[7][0].numMoves == 0) {
                pieces[5][0].setPiece(5, 0, "br", 'b', 0);
                pieces[6][0].setPiece(6, 0, "bk", 'b', 0);
                pieces[7][0].setPiece(7, 0, "", ' ', 0);

            } else if (i == 2 && j == 0 && pieces[0][0].getType().equals("br") && pieces[0][0].numMoves == 0) {
                pieces[3][0].setPiece(3, 0, "br", 'b', 0);
                pieces[2][0].setPiece(2, 0, "bk", 'b', 0);
                pieces[0][0].setPiece(0, 0, "", ' ', 0);
            }
        }

        // Other pieces
        pieces[i][j].setPiece(i, j, this.type, this.side, this.numMoves + 1);

        // Remove appropriate pieces and set numMoves
        pieces[i][j].setBaseValue(pieces[this.gridX][this.gridY].getBaseValue());
        pieces[this.gridX][this.gridY].setPiece(this.gridX, this.gridY, "", ' ', 0);


        // Reset everything except for current square
        for(int r = 0; r < NUM_SQUARES; r++){
            for(int c = 0; c < NUM_SQUARES; c++)
                pieces[r][c].recentlyMoved = false;
        }
        pieces[i][j].recentlyMoved = true;
    }


    @Override
    public boolean equals(Object o){
        if(o == null || o.getClass() != this.getClass())
            return false; // Thanks Luke

        Piece p = (Piece) o;
        return (this.getGridX() == p.getGridX() && this.getGridY() == p.getGridY() && this.getType().equals(p.getType()) && this.side == p.side);
    }

    @Override
    public String toString(){
        return "[ " + "(" + this.gridX + ", " + this.gridY + ") " + this.getType() + " " + this.getSide() + " ]";
    }

    @Override
    public int hashCode(){
        return (this.side == 'w' ? 1 : 2) * (int)Math.pow(NUM_SQUARES*NUM_SQUARES, 4) +
                convertType() * (int)Math.pow(NUM_SQUARES*NUM_SQUARES, 3) +
                this.gridX * (int)Math.pow(NUM_SQUARES*NUM_SQUARES, 2) +
                this.gridY * (NUM_SQUARES*NUM_SQUARES) +
                this.numMoves;
    }

    public int convertType(){
        switch (this.type){
            case "wp": return 1;
            case "wr": return 2;
            case "wn": return 3;
            case "wb": return 4;
            case "wq": return 5;
            case "wk": return 6;
            case "bp": return 7;
            case "br": return 8;
            case "bn": return 9;
            case "bb": return 10;
            case "bq": return 11;
            case "bk": return 12;
        }
        return 15;
    }


    // Images
    private final ImageIcon WHITE_PAWN_IMG = new ImageIcon("src/imgs/white_pawn.png");
    private final ImageIcon WHITE_ROOK_IMG = new ImageIcon("src/imgs/white_rook.png");
    private final ImageIcon WHITE_KNIGHT_IMG = new ImageIcon("src/imgs/white_knight.png");
    private final ImageIcon WHITE_BISHOP_IMG = new ImageIcon("src/imgs/white_bishop.png");
    private final ImageIcon WHITE_QUEEN_IMG = new ImageIcon("src/imgs/white_queen.png");
    private final ImageIcon WHITE_KING_IMG = new ImageIcon("src/imgs/white_king.png");

    private final ImageIcon BLACK_PAWN_IMG = new ImageIcon("src/imgs/black_pawn.png");
    private final ImageIcon BLACK_ROOK_IMG = new ImageIcon("src/imgs/black_rook.png");
    private final ImageIcon BLACK_KNIGHT_IMG = new ImageIcon("src/imgs/black_knight.png");
    private final ImageIcon BLACK_BISHOP_IMG = new ImageIcon("src/imgs/black_bishop.png");
    private final ImageIcon BLACK_QUEEN_IMG = new ImageIcon("src/imgs/black_queen.png");
    private final ImageIcon BLACK_KING_IMG = new ImageIcon("src/imgs/black_king.png");

}
