/**
 * A class used to write chess moves in algebraic notation to games.txt file.
 * All methods are static because I kind of like not having to make a new Notation();
 *
 * TODO: captures
 * TODO: checks and checkmate
 * TODO: score reports (0-1 or 1-0)
 * TODO: castling
 * TODO: promotion
 * TODO: actually save the game (maybe a button or something?)
 *
 * @author Gene Yang
 * @version January 22, 2023
 * **/

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Notation {
    private static int numTurns = 1;
    private static String fileName = "games.txt";

    public static void updateNumTurns(){ numTurns++; }

    public static String pieceToStr(String pieceType){
        if(pieceType.equals("wp") || pieceType.equals("bp")) return "";
        else return pieceType.substring(1).toUpperCase();
    }

    public static String gridify(int toX, int toY){
        return (char)(toX + 97) + "" + (char)(8 - toY + 48) + "";
    }

    public static String getNotation(int toX, int toY, Piece curPiece){
        String result = "";

        // White always starts a new turn
        if(curPiece.getSide() == 'w')
            result = numTurns + ". ";

        return result + Notation.pieceToStr(curPiece.getType()) + gridify(toX, toY) + " ";
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

    public static void updateMoves(int toX, int toY, Piece curPiece){
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileOutputStream(fileName, true));

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open " + fileName + ".");
            System.exit(0);
        }

        // Writes the message to the file
        output.print(Notation.getNotation(toX, toY, curPiece));
        output.close();
    }
}
