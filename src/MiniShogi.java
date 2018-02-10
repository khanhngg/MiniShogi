import GameComponents.*;
import GameComponents.GameMoves.GameMove;
import GameComponents.Pieces.Piece;
import GameComponents.Players.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class MiniShogi implements GameDelegate {
    private final static MiniShogi miniShogi = new MiniShogi();
    private static boolean isInteractiveMode;

    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;

    private static List<String> outputs = new ArrayList<>( );

    // decide if -i or -f
    public static void main( String[] args ) throws Exception {
        // invalid command line arguments length
        if ( args.length < 1 ) {
            displayErrorMessage();
            return;
        }

        // parse command line input
        String mode = args[ 0 ];

        String inputFile = "";
        if ( args.length == 2 ) {
            inputFile = args[ 1 ];
        }

        // instantiate player here -i or -f players
        Player upperPlayer;
        Player lowerPlayer;

        // instantiate board using input file for game states
        Game game;
        Board board = null;

        // decide which mode
        if ( mode.equals( "-i" ) ) {
            List< Piece > pieces = InternalUtils.getInternalUtils().getPiecesFromString( "k a1\n" +
                "g b1\n" +
                "s c1\n" +
                "b d1\n" +
                "r e1\n" +
                "p a2\n" +
                "K e5\n" +
                "G d5\n" +
                "S c5\n" +
                "B b5\n" +
                "R a5\n" +
                "P e4" );
            List< Piece > capturedPiecesForUpper = new ArrayList<>();
            List< Piece > capturedPiecesForLower = new ArrayList<>();

            // filter out upper pieces, lower pieces
            List< Piece > upperPieces = new ArrayList<>();
            List< Piece > lowerPieces = new ArrayList<>();

            for ( Piece piece : pieces ) {
                if ( piece.getPlayerType() == PlayerType.LOWER ) {
                    lowerPieces.add( piece );
                } else {
                    upperPieces.add( piece );
                }
            }

            board = new Board( WIDTH, HEIGHT, upperPieces, lowerPieces, capturedPiecesForUpper, capturedPiecesForLower );

            Scanner scanner = new Scanner( System.in );
            isInteractiveMode = true;
            upperPlayer = new InteractivePlayer( PlayerType.UPPER, scanner );
            lowerPlayer = new InteractivePlayer( PlayerType.LOWER, scanner );

        } else {
            Utils.TestCase parsedStates = Utils.parseTestCase( inputFile );
            List< Piece > pieces = createPieces( parsedStates.initialPieces );
            List< Piece > capturedPiecesForUpper = createCapturedPieces( parsedStates.upperCaptures );
            List< Piece > capturedPiecesForLower = createCapturedPieces( parsedStates.lowerCaptures );

            // filter out upper pieces, lower pieces
            List< Piece > upperPieces = new ArrayList<>();
            List< Piece > lowerPieces = new ArrayList<>();

            for ( Piece piece : pieces ) {
                if ( piece.getPlayerType() == PlayerType.LOWER ) {
                    lowerPieces.add( piece );
                } else {
                    upperPieces.add( piece );
                }
            }
            board = new Board( WIDTH, HEIGHT, upperPieces, lowerPieces, capturedPiecesForUpper, capturedPiecesForLower );

            isInteractiveMode = false;
            List< GameMove > upperMoves = new ArrayList<>(  );
            List< GameMove > lowerMoves = new ArrayList<>(  );

            List< GameMove > moves = createMoves( parsedStates.moves );

            for ( int i = 0; i < moves.size(); i++ ) {
                GameMove move = moves.get( i );
                if ( i % 2 == 0 ) {
                    lowerMoves.add( move );
                } else {
                    upperMoves.add( move );
                }
            }

            upperPlayer = new FilePlayer( PlayerType.UPPER, upperMoves );
            lowerPlayer = new FilePlayer( PlayerType.LOWER, lowerMoves );
        }

        //isInteractiveMode = true;
        game = new Game( upperPlayer, lowerPlayer, board );
        game.gameDelegate = getMiniShogi();

        // start game
        game.startGame();
    }

    public static MiniShogi getMiniShogi() {
        return miniShogi;
    }

    private static List< Piece > createPieces( List< Utils.InitialPosition > initialPositions ) {
        List< Piece > pieces = new ArrayList<>();

        for ( Utils.InitialPosition initialPosition : initialPositions ) {
            // create new piece
            Piece piece = InternalUtils.getInternalUtils().convertStringToPiece( initialPosition.position, initialPosition.piece );

            if (piece != null) {
                // add new piece to pieces list
                pieces.add( piece );
            }
        }

        return pieces;
    }

    private static List< Piece > createCapturedPieces( List< String > capturedPieces ) {
        List< Piece > pieces = new ArrayList<>();

        for ( String capturedPiece : capturedPieces ) {
            // create new piece
            Piece piece = InternalUtils.getInternalUtils().convertStringToPiece( null, capturedPiece );

            if (piece != null) {
            // add new piece to pieces list
                pieces.add( piece );
            }
        }

        return pieces;
    }

    private static List< GameMove > createMoves( List< String > inputFileMoves ) {
        List< GameMove > moves = new ArrayList<>();

        // convert string to move
        for ( String move : inputFileMoves ) {
            moves.add( InternalUtils.getInternalUtils().convertStringToMove( move ) );
        }

        return moves;
    }

    private static void displayErrorMessage() {
        System.out.println( "Please re-enter your command:\n" +
            "$ java MiniShogiApplication mode [additional args]\n" +
            "    MODES:\n" +
            "      -i                          for interactive mode\n" +
            "      -f <absolute/file/path>     for file mode" );
    }

    @Override
    public void newGameStatus( Game game ) {
        if ( isInteractiveMode ) { // interactive mode
            printGameStatus( game );
        } else if ( game.getGameStatus().getGameStatusType() != GameStatusType.IN_PROGRESS ) {
            printGameStatus( game );
        }
    }

    public void printGameStatus( Game game ) {
        // get game status
        GameStatus gameStatus = game.getGameStatus();
        System.out.println("printGameStatus gameStatus="+gameStatus.getGameStatusType());

        // display command
        if ( game.getLastGameMove() != null ) {
            System.out.println( PlayerType.getName( game.getCurrentPlayerType() ) + " player action: "
                + InternalUtils.getInternalUtils().convertMoveToString( game.getLastGameMove() ) );
        }

        // display board
        Board board = game.getBoard();
        String[][] boardMap = InternalUtils.getInternalUtils().convertBoard( board );
        System.out.println( Utils.stringifyBoard( boardMap ) );

        // display captures upper, lower
        System.out.print( "Captures UPPER:" );
        for ( Piece upperCapture : board.getCapturedPieces( PlayerType.UPPER ) ) {
            System.out.print( " " + upperCapture.getName() );
        }

        if (board.getCapturedPieces( PlayerType.UPPER ).size() == 0){
            System.out.print(" ");
        }
        System.out.println();

        System.out.print( "Captures lower:" );
        for ( Piece lowerCapture : board.getCapturedPieces( PlayerType.LOWER ) ) {
            System.out.print( " " + lowerCapture.getName() );
        }

        if (board.getCapturedPieces( PlayerType.LOWER ).size() == 0){
            System.out.print(" ");
        }
        System.out.println();
        System.out.println();


        // display available moves if in check
        if ( gameStatus.getGameStatusType() == GameStatusType.CHECK ) {
            System.out.println( PlayerType.getName( game.getOtherPlayerType() ) + " player is in check!" );
            System.out.println( "Available moves:" );

            List< GameMove > availableMoves = gameStatus.getAvailableMoves();
            List< String > availableMovesToString = new ArrayList<>();

            for ( GameMove gameMove : availableMoves ) {
                availableMovesToString.add( InternalUtils.getInternalUtils().convertMoveToString( gameMove ) );
            }

            // sort moves in string
            Collections.sort( availableMovesToString );

            for ( String availableMove : availableMovesToString ) {
                System.out.println( availableMove );
            }
        } else if ( gameStatus.getGameStatusType() == GameStatusType.CHECK_MATE ) {
            System.out.println( PlayerType.getName( game.getCurrentPlayerType() ) + " player wins.  Checkmate." );
            return;
        } if ( gameStatus.getGameStatusType() == GameStatusType.ILLEGAL_MOVE ) {
            System.out.println( PlayerType.getName( game.getOtherPlayerType() ) + " player wins.  Illegal move." );
            return;
        } else if ( gameStatus.getGameStatusType() == GameStatusType.TIE ) {
            System.out.println( "Tie game.  Too many moves." );
            return;
        }

        // print prompt
        System.out.print( PlayerType.getName( game.getOtherPlayerType() ) + "> " );
        System.out.println();
    }
}

