import java.awt.*;

/**
 * Individual pieces on the chessboard
 *
 * TODO
 * Moving to different squares
 *
 *
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
        this.x = j * SQUARE_WIDTH + X_OFFSET;
        this.y = i * SQUARE_WIDTH + Y_OFFSET;

        this.gridX = i;
        this.gridY = j;

        this.type = type;
    }

    public void setPiece(int x, int y, String type){
        this.x = y * SQUARE_WIDTH + X_OFFSET;
        this.y = x * SQUARE_WIDTH + Y_OFFSET;

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

        if(this.type.equals("wp")) g.drawString("\u2659", this.x, this.y + 40);
        else if(this.type.equals("wr")) g.drawString("\u2656", this.x, this.y + 40);
        else if(this.type.equals("wn")) g.drawString("\u2658", this.x, this.y + 40);
        else if(this.type.equals("wb")) g.drawString("\u2657", this.x, this.y + 40);
        else if(this.type.equals("wq")) g.drawString("\u2655", this.x, this.y + 40);
        else if(this.type.equals("wk")) g.drawString("\u2654", this.x, this.y + 40);

        else if(this.type.equals("bp")) g.drawString("\u265F", this.x, this.y + 40);
        else if(this.type.equals("br")) g.drawString("\u265C", this.x, this.y + 40);
        else if(this.type.equals("bn")) g.drawString("\u265E", this.x, this.y + 40);
        else if(this.type.equals("bb")) g.drawString("\u265D", this.x, this.y + 40);
        else if(this.type.equals("bq")) g.drawString("\u265B", this.x, this.y + 40);
        else if(this.type.equals("bk")) g.drawString("\u265A", this.x, this.y + 40);
        else g.drawString("", this.x, this.y + 40);
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
        }

        return false;
    }

    public void moveToSquare(int toX, int toY){
        this.x = toX;
        this.y = toY;
    }
}
