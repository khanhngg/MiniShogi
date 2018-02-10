package GameComponents.Players;

import GameComponents.GameMoves.GameMove;
import GameComponents.InternalUtils;

import java.util.Scanner;

public class InteractivePlayer extends Player {
    private Scanner scanner;

    public InteractivePlayer( PlayerType playerType, Scanner scanner ) {
        super( playerType );
        this.scanner = scanner;
    }

    @Override
    public GameMove nextMove() {
        String input;

        if ( scanner != null ) {
            input = scanner.nextLine();
            return InternalUtils.getInternalUtils().convertStringToMove( input );
        }

        return null;
    }

    @Override
    public boolean hasMoreMoves() {
        return true;
    }
}
