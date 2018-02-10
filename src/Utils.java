import java.io.*;
import java.util.*;

public class Utils {

    static class InitialPosition {

        String piece;
        String position;

        public InitialPosition(String pc, String pos) {
            piece = pc;
            position = pos;
        }
        public String getPiece() {
            return piece;
        }

        public String getPosition() {
            return position;
        }

        public String toString() {
            return piece + " " + position;
        }
    }

    static class TestCase {

        List<InitialPosition> initialPieces;
        List<String> upperCaptures;
        List<String> lowerCaptures;
        List<String> moves;

        public TestCase(List<InitialPosition> ip, List<String> uc, List<String> lc, List<String> m) {
            initialPieces = ip;
            upperCaptures = uc;
            lowerCaptures = lc;
            moves = m;
        }

        public List< InitialPosition > getInitialPieces() {
            return initialPieces;
        }

        public String toString() {
            String str = "";

            str += "initialPieces: [\n";
            for (InitialPosition piece : initialPieces) {
                str += piece + "\n";
            }
            str += "]\n";


            str += "upperCaptures: [";
            if (upperCaptures != null) {
                for (String piece : upperCaptures) {
                    str += piece + " ";
                }
            }
            str += "]\n";

            str += "lowerCaptures: [";
            if (lowerCaptures != null) {
                for (String piece : lowerCaptures) {
                    str += piece + " ";
                }
            }
            str += "]\n";

            str += "moves: [\n";
            if (moves != null) {
                for (String move : moves) {
                    str += move + "\n";
                }
            }
            str += "]";

            return str;
        }
    }

    public static String stringifyBoard(String[][] board) {

        String str = "";

        for (int row = board.length - 1; row >= 0; row--) {
            str += Integer.toString(row + 1) + " |";
            for (int col = 0; col < board[row].length; col++) {
                str += stringifySquare(board[row][col]);
            }
            str += System.getProperty("line.separator");
        }

        str += "    a  b  c  d  e" + System.getProperty("line.separator");

        return str;
    }

    private static String stringifySquare(String sq) {

        switch(sq.length()) {
            case 0:
                return "__|";
            case 1:
                return " " + sq + "|";
            case 2:
                return sq + "|";
        }

        throw new IllegalArgumentException("Board must be an array of strings like \"\", \"P\", or \"+P\"");
    }

    public static TestCase parseTestCase(String path) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine().trim();

        List<InitialPosition> initialPieces = new ArrayList<InitialPosition>();
        while (!line.equals("")) {
            String[] lineParts = line.split(" ");
            initialPieces.add(new InitialPosition(lineParts[0], lineParts[1]));
            line = br.readLine().trim();
        }

        line = br.readLine().trim();
        List<String> upperCaptures = Arrays.asList(line.substring(1, line.length() - 1).split(" "));

        line = br.readLine().trim();
        List<String> lowerCaptures = Arrays.asList(line.substring(1, line.length() - 1).split(" "));

        List<String> moves = new ArrayList<String>();

        if ((line = br.readLine()) != null) {
            line = line.trim();
            line = br.readLine().trim();

            while (line != null) {
                if (!line.equals( " " )) {
                    moves.add(line);
                }
                line = br.readLine();
            }

        }

        return new TestCase(initialPieces, upperCaptures, lowerCaptures, moves);
    }

//    public static TestCase parseTestCase(String content) throws Exception {
//        String[] lines = content.split( "\n" );
//        int index = 0;
//
//        String line = lines[index];
//
//        List<InitialPosition> initialPieces = new ArrayList<InitialPosition>();
//        while (!line.equals("")) {
//            String[] lineParts = line.split(" ");
//            initialPieces.add(new InitialPosition(lineParts[0], lineParts[1]));
//
//            index++;
//            if (index < line.length()) {
//                line = lines[index].trim();
//            }
//        }
//
//        line = lines[index].trim();
//        index++;
//        List<String> upperCaptures = Arrays.asList(line.substring(1, line.length() - 1).split(" "));
//
//        line = lines[index].trim();
//        index++;
//        List<String> lowerCaptures = Arrays.asList(line.substring(1, line.length() - 1).split(" "));
//
//        List<String> moves = new ArrayList<String>();
//
//        if ( index < lines.length && (line = lines[index]) != null) {
//            line = line.trim();
//            line = lines[index].trim();
//
//            while (index < lines.length && line != null) {
//                if (!line.equals( " " )) {
//                    moves.add(line);
//                }
//                line = lines[index];
//                index++;
//            }
//
//        }
//
//        return new TestCase(initialPieces, upperCaptures, lowerCaptures, moves);
//    }

}