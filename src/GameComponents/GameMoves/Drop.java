package GameComponents.GameMoves;
import GameComponents.Pieces.PieceType;
import GameComponents.Square;

public class Drop extends GameMove {

    private PieceType toDropPieceType;

    public Drop( PieceType toDropPieceType, Square toSquare ) {
        super( toSquare );
        this.toDropPieceType = toDropPieceType;
    }

    @Override
    public GameMoveType getGameMoveType() {
        return GameMoveType.DROP;
    }

    public PieceType getToDropPieceType() {
        return toDropPieceType;
    }
}
