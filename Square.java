/**
 * Individual squares of the chessboard
 *
 * TODO
 * Mouse events (clicking)
 *
 * **/

import java.awt.*;
import javax.swing.JFrame;

public class Square extends JFrame {
    private int x;
    private int y;

    private int w;
    private Color col;
    private boolean activeMode;

    private final Color ACTIVE_COLOR = new Color(50, 50, 50, 200);
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;

    public Square(int x, int y, int w, Color col){
        this.x = x * w + X_OFFSET;
        this.y = y * w + Y_OFFSET;
        this.w = w;
        this.col = col;
    }

    public void paint(Graphics g){
        if(this.activeMode)
            g.setColor(this.ACTIVE_COLOR);
        else
            g.setColor(this.col);

        g.fillRoundRect(this.x, this.y, this.w, this.w, 0, 0);
    }

    public boolean contains(int curX, int curY){
        return curX > this.x && curX < this.x + this.w && curY > this.y && curY < this.y + this.w;
    }

    public void setColor(Color c){
        this.col = c;
    }

    public void selectSquare(){
        this.activeMode = true;
    }

    public void deselectSquare(){
        this.activeMode = false;
    }
}
