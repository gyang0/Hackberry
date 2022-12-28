/**
 * Combines the AI and Board.java class.
 * Implements most methods used in Main.
 * 
 * Does JFrame stuff.
 * **/


import javax.swing.*;
import java.awt.BorderLayout;

public class InitChessboard {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;


    private Board board;

    public InitChessboard(){
        JFrame window = new JFrame();
        window.setSize(WIDTH, HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLayout(new BorderLayout());
        board = new Board();

        window.add(board, BorderLayout.CENTER);

        window.setVisible(true);
    }
}
