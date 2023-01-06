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
    public char side = ' ';
    private int BUTTON_WIDTH = 45;
    private final int X_OFFSET = 100;
    private final int Y_OFFSET = 100;

    private final ImageIcon WHITE_ROOK_IMG = new ImageIcon("src/imgs/white_rook.png");
    private final ImageIcon WHITE_KNIGHT_IMG = new ImageIcon("src/imgs/white_knight.png");
    private final ImageIcon WHITE_BISHOP_IMG = new ImageIcon("src/imgs/white_bishop.png");
    private final ImageIcon WHITE_QUEEN_IMG = new ImageIcon("src/imgs/white_queen.png");
    private final ImageIcon BLACK_ROOK_IMG = new ImageIcon("src/imgs/black_rook.png");
    private final ImageIcon BLACK_KNIGHT_IMG = new ImageIcon("src/imgs/black_knight.png");
    private final ImageIcon BLACK_BISHOP_IMG = new ImageIcon("src/imgs/black_bishop.png");
    private final ImageIcon BLACK_QUEEN_IMG = new ImageIcon("src/imgs/black_queen.png");

    private PromoOptionBtn btn1 = new PromoOptionBtn(0, 0, 0);
    private PromoOptionBtn btn2 = new PromoOptionBtn(0, 0, 0);
    private PromoOptionBtn btn3 = new PromoOptionBtn(0, 0, 0);
    private PromoOptionBtn btn4 = new PromoOptionBtn(0, 0, 0);

    public PromoOptions(int x, int y, char side){
        this.x = x;
        this.y = y;
        this.side = side;
    }

    public void setPos(int x, int y, char side){
        this.x = x;
        this.y = y;
        this.side = side;

        btn1.setPos(X_OFFSET + this.x * BUTTON_WIDTH - 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH);
        btn2.setPos(X_OFFSET + this.x * BUTTON_WIDTH - 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH);
        btn3.setPos(X_OFFSET + this.x * BUTTON_WIDTH + 25, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH);
        btn4.setPos(X_OFFSET + this.x * BUTTON_WIDTH + 75, Y_OFFSET + this.y * BUTTON_WIDTH, BUTTON_WIDTH);
    }

    public boolean contains(int x, int y){
        return false;
    }

    public void paint(Graphics g){
        // Nice little circles
        btn1.paint(g);
        btn2.paint(g);
        btn3.paint(g);
        btn4.paint(g);

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

    public int handleMouseInteractions(int mouseX, int mouseY){
        if(btn1.contains(mouseX, mouseY)) return 0;
        else if(btn2.contains(mouseX, mouseY)) return 1;
        else if(btn3.contains(mouseX, mouseY)) return 2;
        else if(btn4.contains(mouseX, mouseY)) return 3;

        return -1;
    }


    public class PromoOptionBtn {
        private int x;
        private int y;
        private int radius;
        private final Color PROMO_CIRCLE_COL = new Color(100, 100, 100, 150);
        private final Color ACTIVE_PROMO_CIRCLE_COL = new Color(50, 50, 50, 150);
        private final Color CURRENT_COL = new Color(0, 0, 0, 0);
        public boolean mouseInside = false;

        public PromoOptionBtn(int x, int y, int radius){
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public void setPos(int x, int y, int radius){
            this.x = x;
            this.y = y;
            this.radius = radius;
        }

        public boolean contains(int mouseX, int mouseY){
            // Avoiding slow square roots
            return (Math.pow(this.x - mouseX, 2) + Math.pow(this.y - mouseY, 2) < Math.pow(radius, 2));
        }

        public void paint(Graphics g){
            g.setColor(CURRENT_COL);
            g.fillOval(this.x, this.y, this.radius, this.radius);
        }

        public void handleMouseInteractions(int mouseX, int mouseY){
            if(this.contains(mouseX, mouseY)){
                mouseInside = true;
            }
        }

    }

}
