/**
 * A class used to write chess moves in algebraic notation to games.txt file.
 * All methods are static because I kind of like not having to make a new Notation();
 * Still a work in progress.
 *
 * @author Gene Yang
 * @version April 26, 2023
 * **/

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Notation {
    private static int numTurns = 1;
    private final static String fileName = "games.txt";
    public static ArrayList<String> PGNMoves = new ArrayList<String>();

    public static void updateNumTurns(){ numTurns++; }

    public static String pieceToStr(String pieceType){
        if(pieceType.equals("wp") || pieceType.equals("bp")) return "";
        else return pieceType.substring(1).toUpperCase();
    }

    public static String gridify(int toX, int toY){
        return (char)(toX + 97) + "" + (char)(8 - toY + 48) + "";
    }

    public static String getNotation(int toX, int toY, Piece curPiece){
        return Notation.pieceToStr(curPiece.getType()) + gridify(toX, toY);
    }

    public static void clearFile(){
        try {
            PrintWriter output = new PrintWriter(fileName);

            // Replace the file contents with a blank string.
            output.print("");
            output.close();

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open " + fileName + ".");
            System.exit(0);
        }
    }

    public static void update(Piece[][] pieces, int fromX, int fromY, int toX, int toY){
        try {
            PrintWriter output = new PrintWriter(fileName);
            output.print("Working on it");
            output.close();
        } catch(FileNotFoundException e){
            System.out.println("Couldn't open " + fileName + ".");
            System.exit(0);
        }
    }
}
