package GameComponents.Players;

import GameComponents.GameMoves.GameMove;

public abstract class Player {

    private PlayerType playerType;

    public Player(PlayerType playerType) {
        this.playerType = playerType;
    }

    public abstract GameMove nextMove();

    public abstract boolean hasMoreMoves();
}

