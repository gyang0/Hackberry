import javax.swing.*;
import java.awt.*;

/**
 * Promotion options when a pawn reaches the back rank.
 * Should be used in Board class.
 * In essence, a button that displays whenever a pawn is on the back rank.
 * The Board class handles clicking of these individual buttons and sets the piece type.
 * **/

public class PromoOptions {
    private int x = 0;
    private int y = 0;
    private char side = ' ';

    private int BUTTON_WIDTH = 45;

    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;
    private final Color PROMO_CIRCLE_COL = new Color(100, 100, 100);
    public PromoOptions(int x, int y, char side){
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public void setPos(int x, int y, char side){
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public boolean contains(int x, int y){
        return false;
    }

    public void paint(Graphics g){
        // Nice little circles
        g.setColor(PROMO_CIRCLE_COL);
        g.fillOval(X_OFFSET + this.x * BUTTON_WIDTH - 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH);
        g.fillOval(X_OFFSET + this.x * BUTTON_WIDTH - 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH);
        g.fillOval(X_OFFSET + this.x * BUTTON_WIDTH + 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH);
        g.fillOval(X_OFFSET + this.x * BUTTON_WIDTH + 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH);

        if(this.side == 'w') {
            g.drawImage(WHITE_ROOK_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH - 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
            g.drawImage(WHITE_KNIGHT_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH - 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
            g.drawImage(WHITE_BISHOP_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH + 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
            g.drawImage(WHITE_QUEEN_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH + 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
        } else if(this.side == 'b') {
            g.drawImage(BLACK_ROOK_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH - 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
            g.drawImage(BLACK_KNIGHT_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH - 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
            g.drawImage(BLACK_BISHOP_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH + 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
            g.drawImage(BLACK_QUEEN_IMG.getImage(), X_OFFSET + this.x * BUTTON_WIDTH + 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH, null);
        }
    }


    private final ImageIcon WHITE_ROOK_IMG = new ImageIcon("src/imgs/white_rook.png");
    private final ImageIcon WHITE_KNIGHT_IMG = new ImageIcon("src/imgs/white_knight.png");
    private final ImageIcon WHITE_BISHOP_IMG = new ImageIcon("src/imgs/white_bishop.png");
    private final ImageIcon WHITE_QUEEN_IMG = new ImageIcon("src/imgs/white_queen.png");

    private final ImageIcon BLACK_ROOK_IMG = new ImageIcon("src/imgs/black_rook.png");
    private final ImageIcon BLACK_KNIGHT_IMG = new ImageIcon("src/imgs/black_knight.png");
    private final ImageIcon BLACK_BISHOP_IMG = new ImageIcon("src/imgs/black_bishop.png");
    private final ImageIcon BLACK_QUEEN_IMG = new ImageIcon("src/imgs/black_queen.png");
}
