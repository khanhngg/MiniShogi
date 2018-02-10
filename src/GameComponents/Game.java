package GameComponents;

import GameComponents.GameMoves.Drop;
import GameComponents.GameMoves.GameMove;
import GameComponents.GameMoves.GameMoveType;
import GameComponents.GameMoves.NormalMove;
import GameComponents.Pieces.King;
import GameComponents.Pieces.Piece;
import GameComponents.Pieces.PieceType;
import GameComponents.Players.Player;
import GameComponents.Players.PlayerType;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public GameDelegate gameDelegate;

    private Board board;

    private Player upperPlayer;
    private Player lowerPlayer;

    private int upperPlayerMovesCount;
    private int lowerPlayerMovesCount;

    private PlayerType currentPlayerType;
    private GameStatus gameStatus;


    private GameMove lastGameMove;

    public Game( Player upperPlayer, Player lowerPlayer, Board board ) {
        this.upperPlayer = upperPlayer;
        this.lowerPlayer = lowerPlayer;
        this.board = board;
        this.currentPlayerType = PlayerType.UPPER;
    }

    public void startGame() {

        boolean isMoveValid = true;
        MoveStatusType moveStatus = MoveStatusType.VALID;

        while ( true ) {

            // other player
            PlayerType otherPlayerType = getOtherPlayerType();
          System.out.println("moveStatus="+moveStatus);

            // update game status based on move validation
            if ( moveStatus == MoveStatusType.ILLEGAL_MOVE
                || moveStatus == MoveStatusType.NO_MORE_MOVE
                || moveStatus == MoveStatusType.TIE) {

                gameStatus = new GameStatus( moveStatus == MoveStatusType.ILLEGAL_MOVE
                    ? GameStatusType.ILLEGAL_MOVE :
                    (moveStatus == MoveStatusType.NO_MORE_MOVE ?
                        GameStatusType.NO_MORE_MOVE : GameStatusType.TIE), null );

            } else {
                gameStatus = checkGameStatus( board, currentPlayerType, otherPlayerType ); // todo which player later

                // update move count
                if (currentPlayerType == PlayerType.UPPER) {
                    upperPlayerMovesCount++;
                } else {
                    lowerPlayerMovesCount++;
                }
            }

            // update game status
            if ( gameDelegate != null ) {

              if (isOutOfMoves( gameStatus ) && gameStatus.getGameStatusType() == GameStatusType.IN_PROGRESS) {
                gameStatus = new GameStatus( GameStatusType.NO_MORE_MOVE, null );
                gameDelegate.newGameStatus( this );
                System.out.println("if ( gameDelegate != null ) && outOfMove -->"+gameStatus.getGameStatusType());
                break;
              }
              System.out.println("if ( gameDelegate != null ) -->"+gameStatus.getGameStatusType());
              gameDelegate.newGameStatus( this );
            }

//            if (isOutOfMoves( gameStatus )) {
//              break;
//            }

            // break from game loop if game ends based on game status
            if ( isGameEnd( gameStatus ) ) {
                break;
            }

            // switch player
            this.currentPlayerType = otherPlayerType;

            // process move
            moveStatus = move( board, currentPlayerType );
          System.out.println("moveStatus = move"+ moveStatus);

        }

    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public PlayerType getCurrentPlayerType() {
        return currentPlayerType;
    }

    public PlayerType getOtherPlayerType() {
        return this.currentPlayerType == PlayerType.LOWER ? PlayerType.UPPER : PlayerType.LOWER;
    }

    public int getUpperPlayerMovesCount() {
        return upperPlayerMovesCount;
    }

    public int getLowerPlayerMovesCount() {
        return lowerPlayerMovesCount;
    }

    private MoveStatusType move( Board board, PlayerType currentPlayerType ) {
        if (upperPlayerMovesCount >= 200 && lowerPlayerMovesCount >= 200) {
            return MoveStatusType.TIE;
        }
        // get player's next move
        Player currentPlayer = getPlayer( currentPlayerType );

        if ( !currentPlayer.hasMoreMoves()) {
            return MoveStatusType.NO_MORE_MOVE;
        }

        GameMove nextMove = currentPlayer.nextMove(); // nextMove already format validated in Player

        if ( nextMove == null ) {
          System.out.println("---->nextMove == null ");
            return MoveStatusType.ILLEGAL_MOVE;
        }

        this.lastGameMove = nextMove;

        // get piece at position using board

        // check if move direction is valid for piece

        // check type of next move
        if ( nextMove.getGameMoveType() == GameMoveType.DROP ) {
            Drop drop = ( Drop ) nextMove;
            PieceType currentPieceType = drop.getToDropPieceType();
            Piece toDropPiece = board.getToDropPiece( currentPieceType, currentPlayerType );

            // if valid move for the board
            if ( isDropLegal( toDropPiece, board, drop ) == MoveStatusType.ILLEGAL_MOVE ) {
              System.out.println("---->ILLEGAL_MOVE == drop ");
                return MoveStatusType.ILLEGAL_MOVE;
            }

            // drop()
            board.drop( toDropPiece, drop.getToSquare() );

        } else {
            // move/promote
            NormalMove move = ( NormalMove ) nextMove;
            System.out.println("move to:"+nextMove.getToSquare().getRow() + " "+nextMove.getToSquare().getCol());

            Piece currentPiece = board.getPieceByLocation( move.getFromSquare() );

            // new rule!
            // board reset the list of newMoves for currentPiece
//            board.resetNewMoves(currentPiece);

            // inherits piece from behind
            board.inheritMovesFromPieceInFront(currentPiece);

            // if valid move for the board
            if ( isMoveLegal( currentPiece, board, move ) == MoveStatusType.ILLEGAL_MOVE ) {
              System.out.println(currentPiece.getName() + " " + currentPiece.getSquare().getRow()+ " "+ currentPiece.getSquare().getCol());
              System.out.println("---->ILLEGAL_MOVE == move ");
              return MoveStatusType.ILLEGAL_MOVE;
            }

            boolean isPromotionRequired = move.isPromotionRequired();

            if ( !isPromotionRequired && currentPiece.getPieceType() == PieceType.PAWN ) {
                if ( !currentPiece.isPromoted() ) {
                    // force promotion if Pawn moves to promotion zone
                    isPromotionRequired = board.isSquareInPromotionZone( currentPlayerType, nextMove.getToSquare() );
                }
            }

            // move()
            board.move( currentPiece, move.getToSquare(), isPromotionRequired );


        }

        return MoveStatusType.VALID;
    }

    private MoveStatusType isMoveLegal( Piece piece, Board board, NormalMove move ) {
        if (piece == null || board == null || move == null) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        // check if move valid at Piece's level
        if ( !piece.isMoveValid( move ) ) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        // check if move valid at Board's level
        // are you moving to your own piece
        Piece destinationPiece = board.getPieceByLocation( move.getToSquare() );


        if ( destinationPiece != null && piece.getPlayerType() == destinationPiece.getPlayerType() ) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        // if any piece on the way
        // get all squares on your path from current to dest location
        // board checks if any pieces on the path
        List< Square > squaresOnPath = piece.getSquaresOnPath( move.getToSquare() );
        squaresOnPath.remove( move.getToSquare() );
        squaresOnPath.remove( piece.getSquare() );

        List< Piece > piecesOnPath = board.getPiecesOnSquares( squaresOnPath );
        if ( piecesOnPath.size() > 0 ) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        // illegal if putting your king in check/checkmate
        GameStatus gameStatus = checkGameStatusOfFutureMove( board, move, getOppositePlayerType(piece.getPlayerType()) );
        if ( gameStatus.getGameStatusType() == GameStatusType.CHECK ||
            gameStatus.getGameStatusType() == GameStatusType.CHECK_MATE ) {
          for (GameMove gameMove: gameStatus.getAvailableMoves()) {
            System.out.println("-- game move=" + InternalUtils.getInternalUtils().convertMoveToString( gameMove ));
          }
          if (!gameStatus.getAvailableMoves().contains( move )) {
            System.out.println("-- move=" + InternalUtils.getInternalUtils().convertMoveToString( move ));
            System.out.println("---> moving into check");
            return MoveStatusType.ILLEGAL_MOVE;
          }
        }

        return MoveStatusType.VALID;
    }

    private PlayerType getOppositePlayerType( PlayerType playerType ) {
        return playerType == PlayerType.UPPER ? PlayerType.LOWER : PlayerType.UPPER;
    }

    private GameStatus checkGameStatusOfFutureMove( Board board, GameMove futureMove, PlayerType currentPlayerType ) {
      System.out.println("===>checkGameStatusOfFutureMove");
      System.out.println( InternalUtils.getInternalUtils().convertMoveToString( futureMove ) );
      System.out.println("currentplayer="+ currentPlayerType);
        // clone of current board
        Board clonedBoard = board.deepCopy();
        PlayerType otherPlayerType = getOppositePlayerType( currentPlayerType );

        if ( futureMove.getGameMoveType() == GameMoveType.DROP ) {
            PieceType dropPieceType = ( ( Drop ) futureMove ).getToDropPieceType();
            Piece toDropPiece = board.getToDropPiece( dropPieceType, currentPlayerType );

            // can't find piece to drop, return illegal move
            if (toDropPiece != null) {
                clonedBoard.drop( toDropPiece , futureMove.getToSquare() );
            } else {
                return new GameStatus( GameStatusType.ILLEGAL_MOVE, null );
            }

        } else {
            Piece piece = clonedBoard.getPieceByLocation( ( ( NormalMove ) futureMove ).getFromSquare() );
            clonedBoard.move( piece, futureMove.getToSquare(), ( ( NormalMove ) futureMove ).isPromotionRequired() );
          System.out.println("clonedBoard.move");
        }

        System.out.println( currentPlayerType + " " + otherPlayerType + "\n");
        return checkGameStatus( clonedBoard, currentPlayerType, otherPlayerType );
    }

    private MoveStatusType isDropLegal( Piece piece, Board board, Drop drop ) {
        if (piece == null || board == null || drop == null) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        if ( !piece.isMoveValid( drop ) ) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        // if destination is empty
        Piece destinationPiece = board.getPieceByLocation( drop.getToSquare() );
        if ( destinationPiece != null ) {
            return MoveStatusType.ILLEGAL_MOVE;
        }

        // check dropping if piece is a pawn
        if ( piece.getPieceType() == PieceType.PAWN ) {

            // if piece is pawn, cant move to promotion zone
            if ( board.isSquareInPromotionZone( piece.getPlayerType(), drop.getToSquare() ) ) {
                return MoveStatusType.ILLEGAL_MOVE;
            }

            // if piece is pawn and immediate checkmate on other player, illegal
            if (checkGameStatusOfFutureMove( board, drop, piece.getPlayerType() ).getGameStatusType()
                == GameStatusType.CHECK_MATE) {
                return MoveStatusType.ILLEGAL_MOVE;
            }

            // if 2 unpromoted pawns in same column, illegal
            // get all unpromoted pawns of same player and check their columns
            List<Piece> currentPlayerPieces = board.getPieces( piece.getPlayerType() );
            for(Piece currentPlayerPiece: currentPlayerPieces) {

                if (currentPlayerPiece.getPieceType() == PieceType.PAWN
                    && !currentPlayerPiece.isPromoted()
                    && currentPlayerPiece.getCol() == drop.getToSquare().getCol()) {

                    return MoveStatusType.ILLEGAL_MOVE;
                }
            }

        }

        return MoveStatusType.VALID;
    }

    private Player getPlayer( PlayerType playerType ) {
        return playerType == PlayerType.LOWER ? lowerPlayer : upperPlayer;

    }

    private GameStatus checkGameStatus( Board board, PlayerType currentPlayerType, PlayerType otherPlayerType ) {
        // if king is in check
        // get available moves for king of other player
        King otherKing = board.getKingPiece( otherPlayerType );

        // available moves for other player
        List< GameMove > availableMoves = new ArrayList<>();

        // pieces of threatening player
        List< Piece > currentPlayerPieces = board.getPieces( currentPlayerType );

        // threatening pieces
        List< Piece > threateningPieces = new ArrayList<>();
        for ( Piece piece : currentPlayerPieces ) {
            if ( canPieceMoveToSquare( piece, otherKing.getSquare() ) ) {
                threateningPieces.add( piece );
            }
        }

        boolean isOtherKingInCheck = threateningPieces.size() > 0;

        // if king is in checkmate
        if ( isOtherKingInCheck ) {

            // ask board what are avail moves for king
            boolean isKingSquareSafe = true;
            List< Square > kingNextSquares = board.getPossibleNextSquaresForKing( otherKing );
            if ( !kingNextSquares.isEmpty() ) {
                for ( Square kingSquare : kingNextSquares ) {

                    for ( Piece threateningPiece : threateningPieces ) {
//                    for ( Piece currentPlayerPiece : currentPlayerPieces ) {
                        if ( canPieceMoveToSquare( threateningPiece, kingSquare ) ) {
                            isKingSquareSafe = false;
                            break;
                        }
                    }

                    // todo: only add if currentPlayerPieces make 1 step moves, not multiple-step moves
                    if ( isKingSquareSafe ) {
                        GameMove gameMove = new NormalMove( otherKing.getSquare(), kingSquare );
                        availableMoves.add( gameMove );
                    }
                }
            }

            // early checkmate if more than one threatening pieces and still no available moves for other king
            // todo why?????
//            if ( availableMoves.size() == 0 && threateningPieces.size() > 1 ) {
//                return new GameStatus( GameStatusType.CHECK_MATE, availableMoves );
//            }

            // check the other available moves or drops to get out of check
            if ( threateningPieces.size() == 1 ) {
                Piece threateningPiece = threateningPieces.get( 0 );

                // check if other player pieces can help their king
                // pieces of threatening player
                List< Piece > otherPlayerPieces = board.getPieces( otherPlayerType );
//                otherPlayerPieces.remove( otherKing );

                // capture current player's threatening piece at its square
                for ( Piece otherPlayerPiece : otherPlayerPieces ) {
                    // if other player can capture our threatening piece, add to capturableThreateningPieces
                    if ( otherPlayerPiece != otherKing && canPieceMoveToSquare( otherPlayerPiece, threateningPiece.getSquare() ) ) {
                        availableMoves.add( new NormalMove( otherPlayerPiece.getSquare(), threateningPiece.getSquare() ) );
                    }
                }


                // drop
                // get squares on path of threatening piece
                List< Square > squaresOnPath = threateningPiece.getSquaresOnPath( otherKing.getSquare() );
                squaresOnPath.remove( otherKing.getSquare() );
                squaresOnPath.remove( threateningPiece.getSquare() );

                // get list of other player's captured pieces
                List< Piece > otherPlayerCapturedPieces = board.getCapturedPieces( otherPlayerType );

                for ( Square square : squaresOnPath ) {
                    for ( Piece otherPlayerCapturedPiece : otherPlayerCapturedPieces ) {
                        availableMoves.add( new Drop( otherPlayerCapturedPiece.getPieceType(), square ) );
                    }
                }

                // other player's pieces can block the current player's threatening piece on its path
                for ( Square square : squaresOnPath ) {
                    for ( Piece otherPlayerPiece : otherPlayerPieces ) {
                        if ( otherPlayerPiece != otherKing && canPieceMoveToSquare( otherPlayerPiece, square ) ) {
                            availableMoves.add( new NormalMove( otherPlayerPiece.getSquare(), square ) );
                        }
                    }
                }
            }

            if ( availableMoves.size() == 0 ) {
                return new GameStatus( GameStatusType.CHECK_MATE, availableMoves );
            }

        }

        return new GameStatus( isOtherKingInCheck ? GameStatusType.CHECK : GameStatusType.IN_PROGRESS, availableMoves );
    }

    public GameMove getLastGameMove() {
        return lastGameMove;
    }

    private boolean canAnyPieceMoveToSquare( List< Piece > pieces, Square toSquare ) {
        for ( Piece piece : pieces ) {
            if ( canPieceMoveToSquare( piece, toSquare ) ) {
                return true;
            }
        }

        return false;
    }

    private boolean canPieceMoveToSquare( Piece piece, Square toSquare ) {
        List< Square > squaresOnPath = piece.getSquaresOnPath( toSquare );
        List< Piece > piecesOnPath = board.getPiecesOnSquares( squaresOnPath );
        for (Piece pieceOnPath: piecesOnPath) {
            if (!pieceOnPath.getSquare().equals(piece.getSquare())
                && !pieceOnPath.getSquare().equals(toSquare)) {
                return false;
            }
        }
        return squaresOnPath.size() > 0;
    }

    private boolean isOutOfMoves(GameStatus gameStatus) {
      GameStatusType gameStatusType = gameStatus.getGameStatusType();
      // game ends when both players run out of moves
      if (!getPlayer(getOtherPlayerType()).hasMoreMoves() && !getPlayer( currentPlayerType ).hasMoreMoves()) {
        if (gameStatus.getGameStatusType() == GameStatusType.CHECK) {
          return true;
        } else if (gameStatusType == GameStatusType.NO_MORE_MOVE ) {
          return true;
        } else  {
          return true;
        }
      }
      return false;
    }

    private boolean isGameEnd( GameStatus gameStatus ) {
        GameStatusType gameStatusType = gameStatus.getGameStatusType();

        return gameStatusType == GameStatusType.ILLEGAL_MOVE ||
            gameStatusType == GameStatusType.NO_MORE_MOVE ||
            gameStatusType == GameStatusType.TIE ||
            gameStatusType == GameStatusType.CHECK_MATE;
    }
}