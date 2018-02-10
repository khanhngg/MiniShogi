package GameComponents.Pieces;

import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

public class Pawn extends Piece {
    public Pawn( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        super( playerType, square, isCaptured, isPromoted );
        this.pieceType = PieceType.PAWN;
    }

    @Override
    public boolean isMoveDirectionValid( MoveDirectionType moveDirectionType ) {
        switch ( moveDirectionType ) {
            case NORTH:
                return true;

            case SOUTH:
            case EAST:
            case WEST:
            case NORTH_EAST:
            case NORTH_WEST:
                return this.isPromoted(); // promoted Pawn moves like a Gold General

            default:
                return false;
        }
    }

}
