package GameComponents;

import GameComponents.GameMoves.*;
import GameComponents.Pieces.*;
import GameComponents.Players.PlayerType;

import java.util.ArrayList;
import java.util.List;

public class InternalUtils {
    private static final InternalUtils internalUtils = new InternalUtils();

    private InternalUtils() {

    }

    public static InternalUtils getInternalUtils() {
        return internalUtils;
    }

    public GameMove convertStringToMove( String input ) {

        String[] inputMoves = input.split( "\\s" );

        if ( inputMoves.length >= 3 ) {
            if ( inputMoves[ 0 ].equals( "move" ) ) {
                String from = inputMoves[ 1 ];
                String to = inputMoves[ 2 ];

                Square fromSquare = convertStringToSquare( from );
                Square toSquare = convertStringToSquare( to );

                if ( inputMoves.length >= 4 && inputMoves[ 3 ].equals( "promote" ) ) {
                    return new PromotedMove( fromSquare, toSquare );
                }

                return new NormalMove( fromSquare, toSquare );
            } else if ( inputMoves[ 0 ].equals( "drop" ) ) { // drop
                String pieceName = inputMoves[ 1 ];
                String dropTo = inputMoves[ 2 ];

                PieceType pieceType = PieceType.getPieceTypeByName( pieceName );
                Square dropToSquare = convertStringToSquare( dropTo );

                return new Drop( pieceType, dropToSquare );
            } else { // invalid move
                return null;
            }
        }

        return null;
    }

    public String convertMoveToString( GameMove gameMove ) {
        String result = "";

        switch ( gameMove.getGameMoveType() ) {
            case NORMAL_MOVE:
            case PROMOTED_MOVE:
                result += "move "
                    + convertSquareToString( ( ( NormalMove ) gameMove ).getFromSquare() )
                    + " "
                    + convertSquareToString( gameMove.getToSquare() )
                    + ( gameMove.getGameMoveType() == GameMoveType.PROMOTED_MOVE ? " promote" : "" );

                break;
            case DROP:
                result += "drop "
                    + PieceType.getPieceNameByType( ( ( Drop ) gameMove ).getToDropPieceType() )
                    + " "
                    + convertSquareToString( gameMove.getToSquare() );
        }

        return result;
    }

    public String convertSquareToString( Square square ) {
        int col = (int)('a') + square.getCol();
        int r = (int)('1') + square.getRow();

        char column = (char)col; // convert that to back to a char
        char row = (char)r;
        return column + "" + row;
    }

    public Square convertStringToSquare( String input ) {
        if (input == null || input.equals( "" )) {
            return null;
        }

        Character col = input.charAt( 0 );
        int column = col - 'a';
        int row = input.charAt( 1 ) - '1';
        return new Square( row, column );
    }

    public String[][] convertBoard( Board board ) {
        String[][] result = new String[ board.getHeight() ][ board.getWidth() ];

        for ( int row = 0; row < board.getHeight(); row++ ) {
            for ( int col = 0; col < board.getWidth(); col++ ) {
                Piece piece = board.getPieceByLocation( new Square( row, col ) );
                if ( piece != null ) {
                    result[ row ][ col ] = piece.getName();
                } else {
                    result[ row ][ col ] = "";
                }
            }
        }

        return result;
    }

    public Piece convertStringToPiece(String position, String pieceTypeString) {
        // example: k a1
        if (pieceTypeString == null || pieceTypeString.equals( "" )) {
            return null;
        }

        Piece piece = null;
        boolean isPromoted = false;
        Square square = convertStringToSquare( position );

        if (pieceTypeString.contains( "+" )) {
            isPromoted = true;
            pieceTypeString = pieceTypeString.substring( 1 );
        }

        PieceType pieceType = PieceType.getPieceTypeByName( pieceTypeString.toLowerCase() );
        PlayerType playerType = pieceTypeString.equals( pieceTypeString.toUpperCase() )
            ? PlayerType.UPPER : PlayerType.LOWER;

        switch ( pieceType ) {
            case KING:
                piece = new King(playerType, square, false, isPromoted );
                break;
            case BISHOP:
                piece = new Bishop(playerType, square, false, isPromoted );
                break;
            case ROOK:
                piece = new Rook(playerType, square, false, isPromoted );
                break;
            case GOLD_GENERAL:
                piece = new GoldGeneral(playerType, square, false, isPromoted );
                break;
            case SILVER_GENERAL:
                piece = new SilverGeneral(playerType, square, false, isPromoted );
                break;
            case PAWN:
                piece = new Pawn(playerType, square, false, isPromoted );
                break;
        }

        return piece;
    }

    public List<Piece > getPiecesFromString(String content) {
        String[] lines = content.split( "\n" );
        List<Piece> pieces = new ArrayList<>(  );

        for (String line: lines) {
            String[] elements = line.split( "\\s" );

            Piece piece = internalUtils.convertStringToPiece( elements[1], elements[0] );
            pieces.add( piece );
        }

        return pieces;
    }

}
