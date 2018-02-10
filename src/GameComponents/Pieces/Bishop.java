package GameComponents.Pieces;

import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

public class Bishop extends Piece {
    public Bishop( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        super( playerType, square, isCaptured, isPromoted );
        this.pieceType = PieceType.BISHOP;
    }

    @Override
    public boolean isMoveDirectionValid( MoveDirectionType moveDirectionType ) {
        switch ( moveDirectionType ) {
            case NORTH_EAST:
            case NORTH_WEST:
            case SOUTH_EAST:
            case SOUTH_WEST:

            case NORTH_EAST_MULTIPLE:
            case NORTH_WEST_MULTIPLE:
            case SOUTH_EAST_MULTIPLE:
            case SOUTH_WEST_MULTIPLE:
                return true;

            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
                return this.isPromoted(); // promoted Bishop behaves like a king

            default:
                return false;
        }
    }

}
