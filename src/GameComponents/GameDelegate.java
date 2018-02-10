package GameComponents;

public interface GameDelegate {

    // notification to let minishogi know which game status it is
    public void newGameStatus( Game game );
}
