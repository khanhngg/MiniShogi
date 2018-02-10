package GameComponents.Pieces;

import GameComponents.GameMoves.GameMove;
import GameComponents.GameMoves.GameMoveType;
import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Piece {

    PieceType pieceType;
    private Square square;
    private PlayerType playerType;

    private boolean isCaptured;
    private boolean isPromoted;

    private List< MoveDirectionType > moveDirections;

    public Piece( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        this.playerType = playerType;
        this.square = square;
        this.isCaptured = isCaptured;
        this.isPromoted = isPromoted;
    }

    public Piece deepCopy() {

        Piece piece = null;
        Square square = null;

        if (this.square!= null) {
            square = new Square( this.square.getRow(), this.square.getCol() );
        }

        switch ( this.pieceType ) {
            case KING:
                piece = new King(this.playerType, square, this.isCaptured, this.isPromoted);
                break;
            case BISHOP:
                piece = new Bishop(this.playerType, square, this.isCaptured, this.isPromoted);
                break;
            case ROOK:
                piece = new Rook(this.playerType, square, this.isCaptured, this.isPromoted);
                break;
            case GOLD_GENERAL:
                piece = new GoldGeneral(this.playerType, square, this.isCaptured, this.isPromoted);
                break;
            case SILVER_GENERAL:
                piece = new SilverGeneral(this.playerType, square, this.isCaptured, this.isPromoted);
                break;
            case PAWN:
                piece = new Pawn(this.playerType, square, this.isCaptured, this.isPromoted);
                break;
        }
        return piece;
    }

    public String getName() {
        String pieceTypeString = PieceType.getPieceNameByType( this.pieceType );

        if (this.playerType == PlayerType.UPPER) {
            pieceTypeString = pieceTypeString.toUpperCase();
        }

        if (this.isPromoted) {
            pieceTypeString = "+" + pieceTypeString;
        }
        return pieceTypeString;
    }

    public int getCol() {
        return square.getCol();
    }

    public int getRow() {
        return square.getRow();
    }

    public Square getSquare() {
        return square;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }

    public boolean isCaptured() {
        return isCaptured;
    }

    public boolean isPromoted() {
        return isPromoted;
    }

    public void setSquare( Square square ) {
        this.square = square;
    }

    public void setPieceType( PieceType pieceType ) {
        this.pieceType = pieceType;
    }

    public void setPlayerType( PlayerType playerType ) {
        this.playerType = playerType;
    }

    public void setCaptured( boolean captured ) {
        isCaptured = captured;
    }

    public void gotCaptured( PlayerType byPlayerType ) {
        this.isCaptured = true;
        this.isPromoted = false;
        this.playerType = byPlayerType;
    }

    public void setPromoted( boolean promoted ) {
        isPromoted = promoted;
    }

    public void gotPromoted() {
        this.isPromoted = true;
    }

    public boolean isMoveValid( GameMove gameMove ) {

        // drop logic
        if ( gameMove.getGameMoveType() == GameMoveType.DROP ) {
            if ( !this.isCaptured ) {
                return false;
            }
        } else if ( gameMove.getGameMoveType() == GameMoveType.PROMOTED_MOVE ) {
            if ( this.isPromoted ) {
                return false;
            }
        }

        return isMoveValid( gameMove.getToSquare() );
    }

    public boolean isMoveValid( Square square ) {

        // move logic
        MoveDirectionType moveDirectionType = calculateDirection( square );

        // check if move direction belongs to piece
        return this.isMoveDirectionValid( moveDirectionType );
    }

    private MoveDirectionType calculateDirection( Square toSquare ) {
        return MoveDirectionType.detectMoveDirectionType( this.playerType, this.getSquare(), toSquare );
    }

    public abstract boolean isMoveDirectionValid( MoveDirectionType moveDirectionType );

    public List< Square > getSquaresOnPath( Square toSquare ) {
        List< Square > squares = new ArrayList<>();
        MoveDirectionType moveDirectionType = calculateDirection( toSquare );

        if ( !isMoveDirectionValid( moveDirectionType ) ) {
            return squares;
        }

        int rowDiff = Math.abs( toSquare.getRow() - this.getRow() );

        // compute squares
        switch ( moveDirectionType ) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:
                squares.add( toSquare );
                break;

            case NORTH_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int row = this.getRow() - 1; row >= toSquare.getRow(); row-- ) {
                        squares.add( new Square( row, this.getCol() ) );
                    }
                } else {
                    for ( int row = this.getRow() + 1; row <= toSquare.getRow(); row++ ) {
                        squares.add( new Square( row, this.getCol() ) );
                    }
                }
                break;

            case SOUTH_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int row = this.getRow() + 1; row <= toSquare.getRow(); row++ ) {
                        squares.add( new Square( row, this.getCol() ) );
                    }
                } else {
                    for ( int row = this.getRow() - 1; row >= toSquare.getRow(); row-- ) {
                        squares.add( new Square( row, this.getCol() ) );
                    }
                }
                break;

            case EAST_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int col = this.getCol() - 1; col >= toSquare.getCol(); col-- ) {
                        squares.add( new Square( this.getRow(), col ) );
                    }
                } else {
                    for ( int col = this.getCol() + 1; col <= toSquare.getCol(); col++ ) {
                        squares.add( new Square( this.getRow(), col ) );
                    }
                }
                break;

            case WEST_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int col = this.getCol() + 1; col <= toSquare.getCol(); col++ ) {
                        squares.add( new Square( this.getRow(), col ) );
                    }
                } else {
                    for ( int col = this.getCol() - 1; col >= toSquare.getCol(); col-- ) {
                        squares.add( new Square( this.getRow(), col ) );
                    }
                }
                break;

            case NORTH_EAST_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() - step, this.getCol() - step ) );
                    }
                } else {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() + step, this.getCol() + step ) );
                    }
                }
                break;

            case NORTH_WEST_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() - step, this.getCol() + step ) );
                    }
                } else {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() + step, this.getCol() - step ) );
                    }
                }
                break;

            case SOUTH_EAST_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() + step, this.getCol() - step ) );
                    }
                } else {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() - step, this.getCol() + step ) );
                    }
                }
                break;

            case SOUTH_WEST_MULTIPLE:
                if ( this.playerType == PlayerType.UPPER ) {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() + step, this.getCol() + step ) );
                    }
                } else {
                    for ( int step = 1; step <= rowDiff; step++ ) {
                        squares.add( new Square( this.getRow() - step, this.getCol() - step ) );
                    }
                }
                break;

            default:
                break;

        }

        return squares;
    }

    // new rule: allow piece to add more moves
    // todo can separate a library of moves depending type of Piece --> don't have to worry abt fixed rules
    Set<MoveDirectionType> moveDirectionTypes = new HashSet<>(  );

    public Set<MoveDirectionType> getMoveDirections() {
        return moveDirectionTypes;
    }

    public void addMoveDirection(MoveDirectionType moveDirectionType) {
        moveDirectionTypes.add( moveDirectionType );
    }

}
