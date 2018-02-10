package GameComponents.Pieces;

public enum PieceType {
    KING,
    ROOK,
    BISHOP,
    GOLD_GENERAL,
    SILVER_GENERAL,
    PAWN;

    public static PieceType getPieceTypeByName( String name ) {
        switch ( name ) {
            case "k":
                return KING;
            case "r":
                return ROOK;
            case "b":
                return BISHOP;
            case "g":
                return GOLD_GENERAL;
            case "s":
                return SILVER_GENERAL;
            case "p":
                return PAWN;
            default:
                return null;
        }
    }

    public static String getPieceNameByType(PieceType pieceType) {
        switch ( pieceType ) {
            case KING:
                return "k";
            case ROOK:
                return "r";
            case BISHOP:
                return "b";
            case GOLD_GENERAL:
                return "g";
            case SILVER_GENERAL:
                return "s";
            case PAWN:
                return "p";
            default:
                return null;
        }
    }
}
