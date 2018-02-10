package GameComponents.GameMoves;

import GameComponents.Square;

public abstract class GameMove {

    Square toSquare;

    public GameMove( Square toSquare ) {
        this.toSquare = toSquare;
    }

    public Square getToSquare() {
        return toSquare;
    }

    public abstract GameMoveType getGameMoveType();
}
