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
    private int x;
    private int y;
    private Square[][] squares = new Board().getSquares();;

    public Piece(int x, int y, String type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void paint(Graphics g){
        if(this.type.equals("white-pawn")){
            g.setColor(Color.GRAY);
            g.fillRoundRect(this.x, this.y, 10, 10, 0, 0);
        }
    }

    public boolean legalMove(int toX, int toY){
        if ("white-pawn".equals(this.type)) {/*
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
        } else {
            return false;
        }

        return false;
    }

    public void moveToSquare(int toX, int toY){
        this.x = toX;
        this.y = toY;
    }
}
