/**
 * A class used to write chess moves in algebraic notation to games.txt file.
 * All methods are static because I kind of like not having to make a new Notation();
 *
 * TODO: checks and checkmate
 * TODO: score reports (0-1 or 1-0)
 * TODO: promotion
 * TODO: en passant
 * TODO: actually save the game (maybe a button or something?)
 * TODO: if two knights can move to the same square, then the notation should reflect that. (same for pawns, rooks, queens, bishops)
 *
 * @author Gene Yang
 * @version May 10, 2023
 * **/


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Notation {
    private static String[][] squares = new String[][]{
            {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"},
            {"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7"},
            {"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6"},
            {"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5"},
            {"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4"},
            {"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3"},
            {"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2"},
            {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"}
    };

    private static String[][] continuations = new String[][]{
            {"e4", "e5", "Nf3", "Nf6", "Bb5", "a6", "Ba4", "Nf6", "O-O"}, // Ruy Lopez
            {"d4", "d5", "c4", "c6", "Nc3", "e6", "Nf3", "Nf6", "Bg5", "dxc4"}, // Queen's Gambit + slav defense
            {"d4", "d5", "c4", "Nf3", "Nf6", "Nc3", "c5", "cxd5", "exd5", "Bg5", "Be7", "dxc5"}, // Queen's Gambit Declined
            {"e4", "e5", "Nf3", "Nf6", "Bc4", "Nf6", "d3", "Bc5", "c3", "O-O", "O-O"}, // Giuoco Piano
            {"e4", "e5", "Nf3", "Nf6", "Bc4", "Nf6", "Ng5", "d4", "exd5", "Na5"}, // Fried Liver + refutation
            {"e4", "e5", "Nf3", "Nf6", "d4", "exd4"}, // Scotch Game

            {"e4", "c5", "Nf3", "d3", "d4", "cxd4", "Nxd4", "Nf6", "Nc3", "a6", "Be2", "e5"}, // Sicilian, Najdorf Defense
            {"d4", "Nf6", "c4", "g6", "Nc3", "Bg7", "e4", "d6", "Be2", "O-O"}, // King's Indian Defense
    };

    private final static String fileName = "games.txt";
    public static ArrayList<String> PGNMoves = new ArrayList<String>();

    public static String pieceToStr(Piece p){
        switch(p.getType()){
            case "wp": return "";
            case "bp": return "";
            case "wr": return "R";
            case "br": return "R";
            case "wb": return "B";
            case "bb": return "B";
            case "wn": return "N";
            case "bn": return "N";
            case "wq": return "Q";
            case "bq": return "Q";
            case "wk": return "K";
            case "bk": return "K";
        }

        return "";
    }

    public static void update(Piece[][] pieces, int fromX, int fromY, int toX, int toY){
        // Castling
        if(pieces[fromX][fromY].getType().equals("wk") && toX == 6) PGNMoves.add("O-O");
        else if(pieces[fromX][fromY].getType().equals("wk") && toX == 2) PGNMoves.add("O-O-O");
        else if(pieces[fromX][fromY].getType().equals("bk") && toX == 6) PGNMoves.add("O-O");
        else if(pieces[fromX][fromY].getType().equals("bk") && toX == 2) PGNMoves.add("O-O-O");
        else {
            String move = "";
            move += pieceToStr(pieces[fromX][fromY]);

            // Captures & en passant
            if ((pieces[fromX][fromY].getType().equals("wp") || pieces[fromX][fromY].getType().equals("bp")) && toX != fromX)
                move += "x";
            else if (pieces[toX][toY].getSide() != ' ') move += "x";

            move += squares[toY][toX];

            PGNMoves.add(move);
        }
    }

    public static void addPromotion(int i, int j, String type){
        PGNMoves.add(squares[i][j] + "=" + type);
    }

    public static void printGame(){
        try {
            PrintWriter output = new PrintWriter(fileName);

            for(int i = 0; i < PGNMoves.size(); i++){
                if(i % 2 == 0) output.print(i/2 + ". ");
                output.print(PGNMoves.get(i) + " ");
            }

            output.close();
        } catch(FileNotFoundException e){
            System.out.println("Couldn't open " + fileName + ".");
            System.exit(0);
        }
    }
}
