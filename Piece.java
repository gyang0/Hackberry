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
    private static final String PIECE_FONT = "Helvetica Neue-bold-40";
    private final int NUM_SQUARES = 8;

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

    public String getType(){
        return this.type;
    }

    public char getSide(){ return this.side; }

    public void paint(Graphics g){
        g.setColor(Color.YELLOW);
        g.setFont(Font.decode(PIECE_FONT));

        //BufferedImage img = ImageIO.read(new File("/imgs/white_pawn.png"));

        //if(this.type.equals("wp")) img = ImageIO.read(new File("/imgs/white_pawn.png"));
        //else if(this.type.equals("wr")) img = ImageIO.read(new File("/imgs/white_rook.png"));

        if(!this.type.equals("")){
            ImageIcon img = null;

            if(this.type.equals("wp")) img = new ImageIcon("src/imgs/white_pawn.png");
            else if(this.type.equals("wr")) img = new ImageIcon("src/imgs/white_rook.png");
            else if(this.type.equals("wn")) img = new ImageIcon("src/imgs/white_knight.png");
            else if(this.type.equals("wb")) img = new ImageIcon("src/imgs/white_bishop.png");
            else if(this.type.equals("wq")) img = new ImageIcon("src/imgs/white_queen.png");
            else if(this.type.equals("wk")) img = new ImageIcon("src/imgs/white_king.png");
            else if(this.type.equals("bp")) img = new ImageIcon("src/imgs/black_pawn.png");
            else if(this.type.equals("br")) img = new ImageIcon("src/imgs/black_rook.png");
            else if(this.type.equals("bn")) img = new ImageIcon("src/imgs/black_knight.png");
            else if(this.type.equals("bb")) img = new ImageIcon("src/imgs/black_bishop.png");
            else if(this.type.equals("bq")) img = new ImageIcon("src/imgs/black_queen.png");
            else img = new ImageIcon("src/imgs/black_king.png");

            Image i = img.getImage();
            g.drawImage(i, this.x, this.y, SQUARE_WIDTH, SQUARE_WIDTH, null);
        }
    }


    public boolean legalMove(int toX, int toY, Piece[][] pieces){
        // Can't capture pieces on its own side
        if(pieces[toX][toY].side == this.type.charAt(0))
            return false;

        if (this.type.equals("wp")) {
            // If the move is diagonal
            if(toX == this.gridX - 1 || toX == this.gridX + 1){
                if(toX == this.gridX - 1 && toY == this.gridY - 1 && pieces[toX][toY].side == 'b') return true;
                else if(toX == this.gridX + 1 && toY == this.gridY - 1 && pieces[toX][toY].side == 'b') return true;
            }

            // If the pawn hasn't moved yet
            if(this.gridY == 6){
                if(this.gridX == toX && toY == 5 && pieces[toX][toY].side == ' ') return true;
                if(this.gridX == toX && toY == 4 && pieces[toX][toY].side == ' ' && pieces[toX][toY + 1].side == ' ') return true;
            }

            else if(toX == this.gridX && toY == this.gridY - 1 && pieces[toX][toY].side == ' ') return true;

        } else if(this.type.equals("bp")){
            // If the move is diagonal
            if(toX == this.gridX - 1 || toX == this.gridX + 1){
                if(toX == this.gridX - 1 && toY == this.gridY + 1 && pieces[toX][toY].side == 'w') return true;
                else if(toX == this.gridX + 1 && toY == this.gridY + 1 && pieces[toX][toY].side == 'w') return true;
            }

            // If the pawn hasn't moved yet
            if(this.gridY == 1){
                if(this.gridX == toX && toY == 2 && pieces[toX][toY].side == ' ') return true;
                if(this.gridX == toX && toY == 3 && pieces[toX][toY].side == ' ' && pieces[toX][toY - 1].side == ' ') return true;
            }

            else if(toX == this.gridX && toY == this.gridY + 1 && pieces[toX][toY].side == ' ') return true;

        } else if(this.type.equals("wn") || this.type.equals("bn")){
            if(Math.abs(toX - this.gridX) == 2 && Math.abs(toY - this.gridY) == 1)
                return true;
            else if(Math.abs(toX - this.gridX) == 1 && Math.abs(toY - this.gridY) == 2)
                return true;

        } else if(this.type.equals("wb") || this.type.equals("bb")){
            if(Math.abs(toX - this.gridX) == Math.abs(toY - this.gridY))
                return true;

        } else if(this.type.equals("wr") || this.type.equals("br")){
            if(toX == this.gridX) return true;
            if(toY == this.gridY) return true;

        } else if(this.type.equals("wk")){
            // Kingside castling
            if(this.numMoves == 0 && toX == 6 && toY == this.gridY){
                // Rook hasn't moved
                if(pieces[7][this.gridY].getType().equals("wr") && pieces[7][this.gridY].numMoves == 0){
                    // All spaces cleared
                    if(pieces[5][this.gridY].side == ' ' && pieces[6][this.gridY].side == ' ')
                        return true;
                }
            }

            // Queenside castling
            else if(this.numMoves == 0 && toX == 2 && toY == this.gridY){
                // Rook hasn't moved
                if(pieces[0][this.gridY].getType().equals("wr") && pieces[0][this.gridY].numMoves == 0){
                    // All spaces cleared
                    if(pieces[1][this.gridY].side == ' ' && pieces[2][this.gridY].side == ' ' && pieces[3][this.gridY].side == ' ')
                        return true;
                }
            }


            if(Math.abs(toX - this.gridX) <= 1 && Math.abs(toY - this.gridY) <= 1)
                return true;

        } else if(this.type.equals("bk")){
            // Kingside castling
            if(this.numMoves == 0 && toX == 6 && toY == this.gridY){
                // Rook hasn't moved
                if(pieces[7][this.gridY].getType().equals("br") && pieces[7][this.gridY].numMoves == 0){
                    // All spaces cleared
                    if(pieces[5][this.gridY].side == ' ' && pieces[6][this.gridY].side == ' ')
                        return true;
                }
            }

            // Queenside castling
            else if(this.numMoves == 0 && toX == 2 && toY == this.gridY){
                // Rook hasn't moved
                if(pieces[0][this.gridY].getType().equals("br") && pieces[0][this.gridY].numMoves == 0){
                    // All spaces cleared
                    if(pieces[1][this.gridY].side == ' ' && pieces[2][this.gridY].side == ' ' && pieces[3][this.gridY].side == ' ')
                        return true;
                }
            }


            if(Math.abs(toX - this.gridX) <= 1 && Math.abs(toY - this.gridY) <= 1)
                return true;

        } else if(this.type.equals("wq") || this.type.equals("bq")){
            // Queen is a rook and bishop combined.
            if(toX == this.gridX) return true;
            if(toY == this.gridY) return true;
            if(Math.abs(toX - this.gridX) == Math.abs(toY - this.gridY)) return true;
        }

        return false;
    }

    public void moveToSquare(int toX, int toY){
        this.x = toX;
        this.y = toY;
    }
}
