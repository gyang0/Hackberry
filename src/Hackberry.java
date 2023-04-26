import javax.swing.*;
import java.awt.*;

/**
 * Chess piece images from https://commons.wikimedia.org/wiki/Category:PNG_chess_pieces/Standard_transparent
 * Alpha-beta algorithm from https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning
 *
 * @author Gene Yang
 * @version April 26, 2023
 **/

public class Hackberry {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 700;
    private Board board;

    public void run(){
        JFrame window = new JFrame();
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());

        board = new Board();
        window.add(board, BorderLayout.CENTER);
        window.setVisible(true);
    }

    public static void main(String[] args) {
        new Hackberry().run();
    }
}
