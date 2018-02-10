package GameComponents.Pieces;

import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

public class Rook extends Piece {
    public Rook( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        super( playerType, square, isCaptured, isPromoted );
        this.pieceType = PieceType.ROOK;
    }

    @Override
    public boolean isMoveDirectionValid( MoveDirectionType moveDirectionType ) {
        switch ( moveDirectionType ) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
            case NORTH_MULTIPLE:
            case SOUTH_MULTIPLE:
            case EAST_MULTIPLE:
            case WEST_MULTIPLE:
                return true;

            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:
                return this.isPromoted(); // promoted Rook behaves like a King

            default:
                return false;
        }
    }


}
