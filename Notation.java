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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Notation {
    private static int numTurns = 1;
    File file = new File("src/games.txt");

    public static void updateNumTurns(){ numTurns++; }

    public static String pieceToStr(String pieceType){
        if(pieceType.equals("wp") || pieceType.equals("bp")) return "";
        else return pieceType.substring(1).toUpperCase();
    }

    public static String gridify(int toX, int toY){

        return (char)(toX + 97) + "" + (char)(8 - toY + 48) + "";
    }

    public static String getNotation(int toX, int toY, Piece curPiece){
        // White always starts a new turn
        if(curPiece.getSide() == 'w')
            return numTurns + ". " + Notation.pieceToStr(curPiece.getType()) + gridify(toX, toY) + " ";
        else
            return Notation.pieceToStr(curPiece.getType()) + gridify(toX, toY) + " ";
    }

    public static void updateMoves(int toX, int toY, Piece curPiece){
        PrintWriter output = null;
        try {
            output = new PrintWriter(new FileOutputStream("games.txt", true));

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't open games.txt.");
            System.exit(0);
        }

        // Writes the message to the file
        System.out.println(Notation.getNotation(toX, toY, curPiece));
        output.println(Notation.getNotation(toX, toY, curPiece));
        output.close();
    }
}
