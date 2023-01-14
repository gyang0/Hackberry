import javax.swing.*;
import java.awt.*;

/**
 * Individual pieces on the chessboard
 * **/

public class Piece {
    private String type;
    private char side;

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

    public Piece(int i, int j, String type, char side){
        this.x = i * SQUARE_WIDTH + X_OFFSET;
        this.y = j * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = i;
        this.gridY = j;

        this.type = type;
        this.side = side;
    }

    public void setPiece(int x, int y, String type, char side){
        this.x = x * SQUARE_WIDTH + X_OFFSET;
        this.y = y * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = x;
        this.gridY = y;

        this.type = type;
        this.side = side;
    }

    public int getGridX(){ return this.gridX; }
    public int getGridY(){ return this.gridY; }

    public String getType(){
        return this.type;
    }

    public char getSide(){ return this.side; }

    public void paint(Graphics g){
        if(!this.type.equals("")){
            ImageIcon img = null;

            if(this.type.equals("wp")) img = WHITE_PAWN_IMG;
            else if(this.type.equals("wr")) img = WHITE_ROOK_IMG;
            else if(this.type.equals("wn")) img = WHITE_KNIGHT_IMG;
            else if(this.type.equals("wb")) img = WHITE_BISHOP_IMG;
            else if(this.type.equals("wq")) img = WHITE_QUEEN_IMG;
            else if(this.type.equals("wk")) img = WHITE_KING_IMG;
            else if(this.type.equals("bp")) img = BLACK_PAWN_IMG;
            else if(this.type.equals("br")) img = BLACK_ROOK_IMG;
            else if(this.type.equals("bn")) img = BLACK_KNIGHT_IMG;
            else if(this.type.equals("bb")) img = BLACK_BISHOP_IMG;
            else if(this.type.equals("bq")) img = BLACK_QUEEN_IMG;
            else img = BLACK_KING_IMG;

            Image i = img.getImage();
            g.drawImage(i, this.x, this.y, SQUARE_WIDTH, SQUARE_WIDTH, null);
        }
    }

    /**
     * Checks if a move is legal for a rook.
     *
     * @param toX - The x-position of the square the rook is trying to move to.
     * @param toY - The y-position of the square the rook is trying to move to.
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

    public boolean legalMove(int toX, int toY, Piece[][] pieces, int[] mostRecentPieceMov, boolean[][] squaresControlledW, boolean[][] squaresControlledB){
        // Can't capture pieces on its own side
        if(pieces[toX][toY].side == this.side)
            return false;

        // Can't move to same square
        if(toX == this.gridX && toY == this.gridY)
            return false;

        boolean isLegal = false;
        switch(this.type){
            case "wp":
                // If the move is diagonal
                if((toX == this.gridX - 1 || toX == this.gridX + 1) && toY == this.gridY - 1){
                    if(toX == this.gridX - 1 && pieces[toX][toY].side == 'b') isLegal = true;
                    else if(toX == this.gridX + 1 && pieces[toX][toY].side == 'b') isLegal = true;

                    // En passant
                    if(toY == this.gridY - 1 && Math.abs(toX - this.gridX) == 1 && pieces[toX][toY].side == ' ' && pieces[toX][toY + 1].type.equals("bp") && pieces[toX][toY + 1].numMoves == 1) {
                        if(mostRecentPieceMov[0] == toX && mostRecentPieceMov[1] == toY + 1) {
                            isLegal = true;
                        }
                    }
                }

                // If the pawn hasn't moved yet
                if(this.gridY == 6){
                    if(this.gridX == toX && toY == 5 && pieces[toX][toY].side == ' ') isLegal = true;
                    if(this.gridX == toX && toY == 4 && pieces[toX][toY].side == ' ' && pieces[toX][toY + 1].side == ' ') isLegal = true;
                }

                else if(toX == this.gridX && toY == this.gridY - 1 && pieces[toX][toY].side == ' ') isLegal = true;
            break;

            case "bp":
                // If the move is diagonal
                if(toX == this.gridX - 1 || toX == this.gridX + 1){
                    if(toX == this.gridX - 1 && toY == this.gridY + 1 && pieces[toX][toY].side == 'w') isLegal = true;
                    else if(toX == this.gridX + 1 && toY == this.gridY + 1 && pieces[toX][toY].side == 'w') isLegal = true;

                    // En passant
                    if(toY == this.gridY + 1 && Math.abs(toX - this.gridX) == 1 && pieces[toX][toY].side == ' ' && pieces[toX][toY - 1].type.equals("wp") && pieces[toX][toY - 1].numMoves == 1) {
                        if(mostRecentPieceMov[0] == toX && mostRecentPieceMov[1] == toY - 1) {
                            isLegal = true;
                        }
                    }
                }

                // If the pawn hasn't moved yet
                if(this.gridY == 1){
                    if(this.gridX == toX && toY == 2 && pieces[toX][toY].side == ' ') isLegal = true;
                    if(this.gridX == toX && toY == 3 && pieces[toX][toY].side == ' ' && pieces[toX][toY - 1].side == ' ') isLegal = true;
                }

                else if(toX == this.gridX && toY == this.gridY + 1 && pieces[toX][toY].side == ' ') isLegal = true;
            break;

            case "wn":
            case "bn":
                if(Math.abs(toX - this.gridX) == 2 && Math.abs(toY - this.gridY) == 1)
                    isLegal = true;
                else if(Math.abs(toX - this.gridX) == 1 && Math.abs(toY - this.gridY) == 2)
                    isLegal = true;
            break;

            case "wb":
            case "bb":
                // Easy check
                if(Math.abs(toX - this.gridX) != Math.abs(toY - this.gridY))
                    isLegal = false;
                else
                   isLegal = this.bishopCheck(toX, toY, pieces);
            break;

            case "wr":
            case "br":
                // Easy check
                if(toX != this.gridX && toY != this.gridY)
                    isLegal = false;
                else
                   isLegal = this.rookCheck(toX, toY, pieces);
            break;

            case "wk":
                // Kingside castling
                if(this.numMoves == 0 && toX == 6 && toY == this.gridY){
                    // Rook hasn't moved
                    if(pieces[7][this.gridY].getType().equals("wr") && pieces[7][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[5][this.gridY].side == ' ' && pieces[6][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledB[4][this.gridY] && !squaresControlledB[5][this.gridY] && !squaresControlledB[6][this.gridY])
                               isLegal = true;
                    }
                }

                // Queenside castling
                else if(this.numMoves == 0 && toX == 2 && toY == this.gridY){
                    // Rook hasn't moved
                    if(pieces[0][this.gridY].getType().equals("wr") && pieces[0][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[1][this.gridY].side == ' ' && pieces[2][this.gridY].side == ' ' && pieces[3][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledB[4][this.gridY] && !squaresControlledB[1][this.gridY] && !squaresControlledB[2][this.gridY] && !squaresControlledB[3][this.gridY])
                                isLegal = true;
                    }
                }
                else if(Math.abs(toX - this.gridX) <= 1 && Math.abs(toY - this.gridY) <= 1 && !squaresControlledB[toX][toY])
                    isLegal = true;
            break;

            case "bk":
                // Kingside castling
                if(this.numMoves == 0 && toX == 6 && toY == this.gridY){
                    // Rook hasn't moved
                    if(pieces[7][this.gridY].getType().equals("br") && pieces[7][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[5][this.gridY].side == ' ' && pieces[6][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledB[4][this.gridY] && !squaresControlledB[5][this.gridY] && !squaresControlledB[6][this.gridY])
                                isLegal = true;
                    }
                }

                // Queenside castling
                else if(this.numMoves == 0 && toX == 2 && toY == this.gridY){
                    // Rook hasn't moved
                    if(pieces[0][this.gridY].getType().equals("br") && pieces[0][this.gridY].numMoves == 0){
                        // All spaces cleared
                        if(pieces[1][this.gridY].side == ' ' && pieces[2][this.gridY].side == ' ' && pieces[3][this.gridY].side == ' ')
                            // Not in check
                            if(!squaresControlledB[4][this.gridY] && !squaresControlledB[1][this.gridY] && !squaresControlledB[2][this.gridY] && !squaresControlledB[3][this.gridY])
                                isLegal = true;
                    }
                }
                else if(Math.abs(toX - this.gridX) <= 1 && Math.abs(toY - this.gridY) <= 1 && !squaresControlledW[toX][toY])
                    isLegal = true;
            break;

            case "wq":
            case "bq":
                // Queen is a rook and bishop combined.
                if(Math.abs(toX - this.gridX) != Math.abs(toY - this.gridY)) isLegal = false;
                if(toX != this.gridX && toY != this.gridY) isLegal = false;

                isLegal = bishopCheck(toX, toY, pieces) || rookCheck(toX, toY, pieces);
            break;
        }

        return isLegal;
    }

    public void playMove(int i, int j, Piece[][] pieces) {
        Piece prev = new Piece(i, j, pieces[i][j].getType(), pieces[i][j].getSide());

        // En passant
        // The target pawn must have moved in the last move, moved two squares up, and be next to the current pawn.
        if(this.type.equals("wp") && j == this.gridY - 1 && Math.abs(i - this.gridX) == 1 && pieces[i][j].getSide() == ' '){
            pieces[i][j].setPiece(i, j, this.type, this.side);
            pieces[i][j + 1].setPiece(i, j + 1, "", ' ');
        }

        else if(this.type.equals("bp") && j == this.gridY + 1 && Math.abs(i - this.gridX) == 1 && pieces[i][j].getSide() == ' '){
            pieces[i][j].setPiece(i, j, this.type, this.side);
            pieces[i][j - 1].setPiece(i, j - 1, "", ' ');
        }

        // Castling - white, kingside & queenside
        else if(this.type.equals("wk")){
            if(i == 6) {
                pieces[5][7].setPiece(5, 7, "wr", 'w');
                pieces[6][7].setPiece(6, 7, "wk", 'w');
                pieces[7][7].setPiece(7, 7, "", ' ');
            } else if(i == 2){
                pieces[3][7].setPiece(3, 7, "wr", 'w');
                pieces[2][7].setPiece(2, 7, "wk", 'w');
                pieces[0][7].setPiece(0, 7, "", ' ');
            } else
                pieces[i][j].setPiece(i, j, "wk", 'w');
        }
        // Castling - black, kingside & queenside
        else if(this.type.equals("bk")){
            if(i == 6) {
                pieces[5][0].setPiece(5, 0, "br", 'b');
                pieces[6][0].setPiece(6, 0, "bk", 'b');
                pieces[7][0].setPiece(7, 0, "", ' ');
            } else if(i == 2){
                pieces[3][0].setPiece(3, 0, "br", 'b');
                pieces[2][0].setPiece(2, 0, "bk", 'b');
                pieces[0][0].setPiece(0, 0, "", ' ');
            } else
                pieces[i][j].setPiece(i, j, "bk", 'b');
        }

        // Other pieces
        else {
            pieces[i][j].setPiece(i, j, this.getType(), this.getSide());

        }

        pieces[this.gridX][this.gridY].setPiece(this.gridX, this.gridY, "", ' ');

        // Check if king is in check (no pun intended).
        /*
        board.updateControlledSquares();
        if((this.getSide() == 'w' && board.whiteKingInCheck) || (this.getSide() == 'b' && board.blackKingInCheck)){
            board.setMessage("King is in check");

            pieces[this.gridX][this.gridY].setPiece(this.gridX, this.gridY, this.getType(), this.getSide());
            //pieces[i][j].setPiece(i, j, prev.getType(), prev.getSide());
            System.out.println(prev);
        }*/
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
