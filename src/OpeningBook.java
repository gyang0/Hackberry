/**
 * Stores possible openings for Hackberry to reference without calculating moves.
 *
 * For white: Ruy Lopez
 *            English Opening
 *            Catalan
 *            Queen's Gambit
 *            Giuoco Piano
 *            Fried Liver
 *
 * For black: King's Indian
 *            Nimzo-Indian
 *            Sicilian Defense
 *            Slav Defense
 *            Caro-Kann Defense
 *            Refutation for Fried Liver
 */
public class OpeningBook {

    // Kind of a "tree" of openings.
    // chess.com/analysis
    public static String[][] continuations = new String[][]{
            {"e4", "e5", "Nf3", "Nf6", "Bb5", "a6", "Ba4", "Nf6", "O-O"}, // Ruy Lopez
            {"d4", "d5", "c4", "c6", "Nc3", "e6", "Nf3", "Nf6", "Bg5", "dxc4"}, // Queen's Gambit + slav defense
            {"d4", "d5", "c4", "Nf3", "Nf6", "Nc3", "c5", "cxd5", "exd5", "Bg5", "Be7", "dxc5"}, // Queen's Gambit Declined
            {"e4", "e5", "Nf3", "Nf6", "Bc4", "Nf6", "d3", "Bc5", "c3", "O-O", "O-O"}, // Giuoco Piano
            {"e4", "e5", "Nf3", "Nf6", "Bc4", "Nf6", "Ng5", "d4", "exd5", "Na5"}, // Fried Liver + refutation
            {"e4", "e5", "Nf3", "Nf6", "d4", "exd4"}, // Scotch Game

            {"e4", "c5", "Nf3", "d3", "d4", "cxd4", "Nxd4", "Nf6", "Nc3", "a6", "Be2", "e5"}, // Sicilian, Najdorf Defense
            {"d4", "Nf6", "c4", "g6", "Nc3", "Bg7", "e4", "d6", "Be2", "O-O"}, // King's Indian Defense
    };
}
