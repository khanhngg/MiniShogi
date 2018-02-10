package GameComponents.Pieces;

import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

public class SilverGeneral extends Piece {
    public SilverGeneral( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        super( playerType, square, isCaptured, isPromoted );
        this.pieceType = PieceType.SILVER_GENERAL;
    }

    @Override
    public boolean isMoveDirectionValid( MoveDirectionType moveDirectionType ) {
        switch ( moveDirectionType ) {
            case NORTH:
            case NORTH_EAST:
            case NORTH_WEST:
                return true; // shared directions between Silver and Gold Generals

            case SOUTH_EAST:
            case SOUTH_WEST:
                return !this.isPromoted(); // un-promoted Silver directions

            case SOUTH:
            case EAST:
            case WEST:
                return this.isPromoted(); // promoted Silver General moves like a Gold General

            default:
                return false;
        }
    }

}
