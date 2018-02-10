package GameComponents.Pieces;

import GameComponents.MoveDirectionType;
import GameComponents.Players.PlayerType;
import GameComponents.Square;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King( PlayerType playerType, Square square, boolean isCaptured, boolean isPromoted ) {
        super( playerType, square, isCaptured, isPromoted );
        this.pieceType = PieceType.KING;
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
            case SOUTH_EAST:
            case SOUTH_WEST:
                return true;

            default:
                return false;
        }
    }

}
