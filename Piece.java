import javax.swing.*;
import java.awt.*;

/**
 * Individual pieces on the chessboard
 * **/

public class Piece {
    private String type;

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

    public Piece(int i, int j, String type){
        this.x = i * SQUARE_WIDTH + X_OFFSET;
        this.y = j * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = i;
        this.gridY = j;

        this.type = type;
    }

    public void setPiece(int x, int y, String type){
        this.x = x * SQUARE_WIDTH + X_OFFSET;
        this.y = y * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = x;
        this.gridY = y;

        this.type = type;
    }

    public String getType(){
        return this.type;
    }

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


    public boolean legalMove(int toX, int toY){
        if (this.type.equals("wp")) {/*
         * Still on first rank
         * - Path not blocked
         * - One move forward or one move backwards
         *
         * Not on first rank
         * - Path not blocked
         * - A diagonal capture
         *
         * Not between 6th and 8th rank - return false;
         * */

            // Basic form for now
            if(toX == this.gridX && (this.gridY - toY == 1 || this.gridY - toY == 2))
                return true;

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

        } else if(this.type.equals("wk") || this.type.equals("bk")){
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
