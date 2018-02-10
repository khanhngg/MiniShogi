package GameComponents;

import GameComponents.GameMoves.Drop;
import GameComponents.GameMoves.GameMove;
import GameComponents.Pieces.King;
import GameComponents.Pieces.Piece;
import GameComponents.Pieces.PieceType;
import GameComponents.Players.Player;
import GameComponents.Players.PlayerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Board {

    private List< Piece > upperPieces;
    private List< Piece > lowerPieces;
    private List< Piece > capturedPiecesForUpper;
    private List< Piece > capturedPiecesForLower;

    private Piece[][] piecesBoard;

    private int width;
    private int height;

    public Board( int width, int height, List< Piece > upperPieces, List< Piece > lowerPieces, List< Piece > capturedPiecesForUpper, List< Piece > capturedPiecesForLower ) {
        this.width = width;
        this.height = height;

        this.upperPieces = upperPieces;
        this.lowerPieces = lowerPieces;

        this.capturedPiecesForUpper = capturedPiecesForUpper;
        this.capturedPiecesForLower = capturedPiecesForLower;

        this.piecesBoard = new Piece[ height ][ width ];

        //todo validate unique pieces, diff list of pieces cant contain the other lists' pieces

        initPiecesBoard();
    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Board deepCopy() {
        Board newBoard = new Board( this.width, this.height, copyPieces(upperPieces),
            copyPieces(lowerPieces), copyPieces(capturedPiecesForUpper), copyPieces(capturedPiecesForLower));
        return newBoard;
    }

    private List<Piece> copyPieces(List<Piece> pieces) {
        List<Piece> copied = new ArrayList<>(  );
        for (Piece piece: pieces) {
            if (piece != null) {
                Piece copiedPiece = piece.deepCopy();
                copied.add( copiedPiece );
            }
        }
        return copied;
    }

    public Piece[][] getPiecesBoard() {
        return piecesBoard;
    }

    private boolean initPiecesBoard() {
        return addPiecesToBoard( this.upperPieces )
            && addPiecesToBoard( this.lowerPieces );
    }

    private boolean addPiecesToBoard( List< Piece > pieces ) {
        for ( Piece piece : pieces ) {
            if ( piece.getCol() >= 0 && piece.getCol() < this.width
                && piece.getRow() >= 0 && piece.getRow() < this.height ) {
                this.piecesBoard[ piece.getRow() ][ piece.getCol() ] = piece;
            } else {
                return false;
            }
        }

        return true;
    }

    public Piece getPieceByLocation( Square square ) {
        if (square.getRow() >= 0 && square.getRow() < this.getHeight()
            && square.getCol() >= 0 && square.getCol() < this.getWidth()) {
            return this.piecesBoard[ square.getRow() ][ square.getCol() ];
        } else {
            return null;
        }
    }

    public Piece getToDropPiece(PieceType pieceType, PlayerType playerType) {
        Piece toDropPiece = null;

        List<Piece> capturedPieces = this.getCapturedPieces( playerType );
        for (Piece capturedPiece : capturedPieces) {
            if (capturedPiece.getPieceType() == pieceType) {
                toDropPiece = capturedPiece;
                break;
            }
        }

        return toDropPiece;
    }

    public void drop( Piece currentPiece, Square toSquare ) {
        // get player type
        PlayerType currentPlayerType = currentPiece.getPlayerType();

        // rm from captures and update pieces of player
        switch ( currentPlayerType ) {
            case UPPER:
                upperPieces.add( currentPiece );
                capturedPiecesForUpper.remove( currentPiece );
                break;

            case LOWER:
                lowerPieces.add( currentPiece );
                capturedPiecesForLower.remove( currentPiece );
                break;

            default:
                break;
        }

        // update piece with new position
        currentPiece.setSquare( toSquare );

        // update board
        piecesBoard[ currentPiece.getRow() ][ currentPiece.getCol() ] = currentPiece;
    }

    public void move( Piece currentPiece, Square toSquare, boolean isPromotionRequired ) {
        // Get piece at toSquare
        Piece capturedPiece = getPieceByLocation( toSquare );

        // detect capture()
        // if capturing -> update captures of current + update pieces of other
        if ( capturedPiece != null ) {
            capture( capturedPiece );
        }

        // detect promotion
        if ( isPromotionRequired ) {
            currentPiece.gotPromoted();
        }

        // save current square
        Square currentSquare = new Square(currentPiece.getSquare().getRow(), currentPiece.getSquare().getCol());

        // update piece with new position
        currentPiece.setSquare( toSquare );


        // update board
        piecesBoard[ currentSquare.getRow() ][ currentSquare.getCol() ] = null;
        piecesBoard[ currentPiece.getRow() ][ currentPiece.getCol() ] = currentPiece;
//
//        // inherits piece from behind
//        inheritMovesFromPieceInFront(currentPiece);
    }

    private void capture( Piece capturedPiece ) {

        // update pieces lists for each player
        if ( capturedPiece.getPlayerType() == PlayerType.UPPER ) {
            this.capturedPiecesForLower.add( capturedPiece );
            this.upperPieces.remove( capturedPiece );
            capturedPiece.gotCaptured( PlayerType.LOWER );
        } else {
            this.capturedPiecesForUpper.add( capturedPiece );
            this.lowerPieces.remove( capturedPiece );
            capturedPiece.gotCaptured( PlayerType.UPPER );
        }

        // reset board state
        piecesBoard[ capturedPiece.getRow() ][ capturedPiece.getCol() ] = null;
    }

    public List<Piece> getPiecesOnSquares(List<Square> squares) {
        List<Piece> result = new ArrayList<>(  );

        for (Square square: squares) {
            Piece piece = this.getPieceByLocation( square );
            if (piece != null) {
                result.add( piece );
            }
        }

        return result;
    }

    public List<Piece> getPieces(PlayerType playerType) {
        if (playerType == PlayerType.UPPER) {
            return upperPieces;
        } else {
            return lowerPieces;
        }
    }
    public List<Piece> getCapturedPieces(PlayerType playerType) {
        if (playerType == PlayerType.UPPER) {
            return capturedPiecesForUpper;
        } else {
            return capturedPiecesForLower;
        }
    }

    public King getKingPiece(PlayerType playerType) {
        if (playerType == PlayerType.UPPER) {
            for (Piece piece: upperPieces) {
                if (piece.getPieceType() == PieceType.KING) {
                    return (King ) piece;
                }
            }
        } else {
            for (Piece piece: lowerPieces) {
                if (piece.getPieceType() == PieceType.KING) {
                    return (King) piece;
                }
            }
        }
        return null;
    }

    public List<Square> getPossibleNextSquaresForKing(King king) {
        Square kingSquare = king.getSquare();
        List<Square> squares = new ArrayList<>(  );

        for(int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int newRow = king.getRow() + row;
                int newCol = king.getCol() + col;
                if (newRow >= 0 && newRow < this.getHeight()
                    && newCol >= 0 && newCol < this.getWidth()) {
                    Square square = new Square( newRow, newCol );
                    Piece piece = this.getPieceByLocation( square );
                    if ( piece == null || piece.getPlayerType() != king.getPlayerType() ) {
                        squares.add( square );
                    }
                }
            }
        }

        return squares;
    }

    public boolean isSquareInPromotionZone( PlayerType playerType, Square toSquare ) {
        switch ( playerType ) {
            case LOWER:
                return toSquare.getRow() == this.height - 1;
            case UPPER:
                return toSquare.getRow() == 0;
            default:
                return false;
        }
    }

    // new rule!
    // piece can inherit moves from another piece in front of it
    public void inheritMovesFromPieceInFront(Piece piece) {
        int currentRow = piece.getRow();
        if (currentRow - 1 > 0 && currentRow - 1 > this.height) {
          // get the piece at row behind to inherit
            Piece pieceBehind = piecesBoard[piece.getRow() -1][piece.getCol()];

            // make sure pieceBehind is not empty and from same player
            if (pieceBehind != null && pieceBehind.getPlayerType() == piece.getPlayerType()) {

                // iter the list of moves from pieceBehind
                for ( MoveDirectionType moveDirectionType: pieceBehind.getMoveDirections()) {

                  // add each of pieceBehind's moves to current piece's list of moves
                    piece.addMoveDirection(moveDirectionType);
                }
            }

        }
    }


}
