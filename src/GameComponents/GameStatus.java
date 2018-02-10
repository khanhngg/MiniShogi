package GameComponents;

import GameComponents.GameMoves.GameMove;

import java.util.ArrayList;
import java.util.List;

public class GameStatus {
//    IN_PROGRESS("Game In Progress."),
//    CHECK(" player is in check!"),
//    CHECK_MATE("Checkmate."),
//    ILLEGAL_MOVE("Illegal move."),
//    TIE("Tie game.  Too many moves."),
//    GAME_OVER("Game Over.");


    private List<GameMove> availableMoves;
    private GameStatusType gameStatusType;

    public List< GameMove > getAvailableMoves() {
        return availableMoves;
    }

    public void setAvailableMoves( List< GameMove > availableMoves ) {
        this.availableMoves = availableMoves;
    }


    GameStatus(GameStatusType gameStatusType, List<GameMove> availableMoves) {
        this.gameStatusType = gameStatusType;
        this.availableMoves = availableMoves;
    }

    public boolean isInCheckMate() {
        // if in check and no avail moves
        if (gameStatusType == GameStatusType.CHECK && availableMoves.size() == 0) {
            return true;
        }
        return false;
    }

    public GameStatusType getGameStatusType() {
        return gameStatusType;
    }

    public void setGameStatusType( GameStatusType gameStatusType ) {
        this.gameStatusType = gameStatusType;
    }



}
