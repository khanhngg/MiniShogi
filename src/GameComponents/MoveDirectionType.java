package GameComponents;

import GameComponents.Players.PlayerType;

public enum MoveDirectionType {
    INVALID( false ),

    // non-multiple verticals and horizontals
    NORTH( false ),
    SOUTH( false ),
    EAST( false ),
    WEST( false ),

    // non-multiple diagonals
    NORTH_EAST( false ),
    NORTH_WEST( false ),
    SOUTH_EAST( false ),
    SOUTH_WEST( false ),

    // multiple verticals and horizontals
    NORTH_MULTIPLE( true ),
    SOUTH_MULTIPLE( true ),
    EAST_MULTIPLE( true ),
    WEST_MULTIPLE( true ),

    // multiple diagonals
    NORTH_EAST_MULTIPLE( true ),
    NORTH_WEST_MULTIPLE( true ),
    SOUTH_EAST_MULTIPLE( true ),
    SOUTH_WEST_MULTIPLE( true );

    private boolean isMultipleDirection;

    MoveDirectionType( boolean isMultipleDirection ) {
        this.isMultipleDirection = isMultipleDirection;
    }

    public boolean isMultipleDirection() {
        return isMultipleDirection;
    }

    public static MoveDirectionType detectMoveDirectionType( PlayerType playerType, Square fromSquare, Square toSquare ) {
        int rowDiff = toSquare.getRow() - fromSquare.getRow();
        int colDiff = toSquare.getCol() - fromSquare.getCol();

        if ( playerType == PlayerType.UPPER ) {
            rowDiff = -rowDiff;
            colDiff = -colDiff;
        }

        if ( rowDiff == 1 && colDiff == 0 ) {
            return MoveDirectionType.NORTH;
        } else if ( rowDiff == -1 && colDiff == 0 ) {
            return MoveDirectionType.SOUTH;
        } else if ( rowDiff == 0 && colDiff == 1 ) {
            return MoveDirectionType.EAST;
        } else if ( rowDiff == 0 && colDiff == -1 ) {
            return MoveDirectionType.WEST;

        } else if ( rowDiff == 1 && colDiff == 1 ) {
            return MoveDirectionType.NORTH_EAST;
        } else if ( rowDiff == 1 && colDiff == -1 ) {
            return MoveDirectionType.NORTH_WEST;
        } else if ( rowDiff == -1 && colDiff == 1 ) {
            return MoveDirectionType.SOUTH_EAST;
        } else if ( rowDiff == -1 && colDiff == -1 ) {
            return MoveDirectionType.SOUTH_WEST;

        } else if ( colDiff == 0 ) {
            if ( rowDiff > 0 ) {
                return NORTH_MULTIPLE;
            } else if ( rowDiff < 0 ) {
                return SOUTH_MULTIPLE;
            }
        } else if ( rowDiff == 0 ) {
            if ( colDiff > 0 ) {
                return EAST_MULTIPLE;
            } else if ( colDiff < 0 ) {
                return WEST_MULTIPLE;
            }

            // diagonal multiple
        } else if ( Math.abs( rowDiff ) == Math.abs( colDiff ) ) {
            if ( colDiff > 0 && rowDiff > 0 ) {
                return NORTH_EAST_MULTIPLE;
            } else if ( colDiff < 0 && rowDiff > 0 ) {
                return NORTH_WEST_MULTIPLE;
            } else if ( colDiff > 0 && rowDiff < 0 ) {
                return SOUTH_EAST_MULTIPLE;
            } else if ( colDiff < 0 && rowDiff < 0 ) {
                return SOUTH_WEST_MULTIPLE;
            }
        }

        return INVALID;
    }


}
