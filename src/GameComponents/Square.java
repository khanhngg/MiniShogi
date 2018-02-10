package GameComponents;

public class Square {
    private int row;
    private int col;

    public Square( int row, int col ) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow( int row ) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol( int col ) {
        this.col = col;
    }



    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Square) {
            Square that = (Square) other;
            result = (this.getRow() == that.getRow() && this.getCol() == that.getCol());
        }
        return result;
    }
}
