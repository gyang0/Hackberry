import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Notation {
    File file = new File("/src/games.txt");

    public String gridify(int toX, int toY){
        return (char)(toX - '0' + 'a') + (char)toY + "";
    }

    public String getNotation(int toX, int toY, Piece curPiece, Piece[][] pieces, int numMoves){
        return "";
    }

    public void updateMoves(String moves){
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileOutputStream("scores.txt", true));

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open games.txt.");
            System.exit(0);
        }

        // Writes the message to the file
        output.println(moves);
        output.close();
    }
}
