package GameComponents.Pieces;

import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

public class GoldGeneral extends Piece {
    public GoldGeneral( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        super( playerType, square, isCaptured, isPromoted );
        this.pieceType = PieceType.GOLD_GENERAL;
    }

    @Override
    public boolean isMoveDirectionValid( MoveDirectionType moveDirectionType ) {
        switch ( moveDirectionType ) {
            case NORTH:
            case SOUTH:
            case EAST:
            case WEST:
            case NORTH_EAST:
            case NORTH_WEST:
                return true;

            default:
                return false;
        }
    }
}
