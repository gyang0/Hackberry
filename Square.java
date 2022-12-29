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

    private final Color ACTIVE_COLOR = new Color(255, 255, 25);

    public Square(int x, int y, int w, Color col){
        this.x = x;
        this.y = y;
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
