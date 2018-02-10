package GameComponents.Players;

import GameComponents.GameMoves.GameMove;
import GameComponents.InternalUtils;

import java.util.List;

public class FilePlayer extends Player {
    private List<GameMove> moves;
    private int currentMoveIndex;

    public FilePlayer( PlayerType playerType, List<GameMove> moves ) {
        super(playerType);
        this.moves = moves;
        currentMoveIndex = 0;
    }

    @Override
    public GameMove nextMove() {
        GameMove gameMove = null;
        if (currentMoveIndex < moves.size()) {
            gameMove = moves.get( currentMoveIndex );
            currentMoveIndex++;
        }

        return gameMove;
    }

    @Override
    public boolean hasMoreMoves() {
        return currentMoveIndex < moves.size();
    }
}
