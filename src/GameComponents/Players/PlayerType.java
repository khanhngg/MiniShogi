package GameComponents.Players;

public enum PlayerType {
    UPPER,
    LOWER;

    public static String getName(PlayerType playerType) {
        return playerType == UPPER ? "UPPER" : "lower";
    }
}
