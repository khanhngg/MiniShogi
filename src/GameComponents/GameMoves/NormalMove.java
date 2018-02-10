package GameComponents.GameMoves;

import GameComponents.Square;

public class NormalMove extends GameMove {

    private Square fromSquare;

    public NormalMove( Square fromSquare, Square toSquare ) {
        super( toSquare );
        this.fromSquare = fromSquare;
    }

    public boolean isPromotionRequired() {
        return false;
    }

    @Override
    public GameMoveType getGameMoveType() {
        return GameMoveType.NORMAL_MOVE;
    }

    public Square getFromSquare() {
        return fromSquare;
    }
}
