package GameComponents.GameMoves;

import GameComponents.Square;

public class PromotedMove extends NormalMove {

    public PromotedMove( Square fromSquare, Square toSquare ) {
        super( fromSquare, toSquare );
    }

    @Override
    public boolean isPromotionRequired() {
        return true;
    }

    @Override
    public GameMoveType getGameMoveType() {
        return GameMoveType.PROMOTED_MOVE;
    }
}
