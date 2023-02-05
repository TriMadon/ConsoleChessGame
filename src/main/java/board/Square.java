package board;

import java.util.Objects;
import static board.SquareState.*;

public class Square {
    private static final Square[] squareArray = new Square[64];
    private final int xSpot;
    private final int ySpot;

    private SquareState squareState;

    private Square(int column, int row){
        this.xSpot = column;
        this.ySpot = row;
    }

    public static Square getSquare(int column, int row){
        if (squareArray[(column - 1) * 8 + (row - 1)] == null) {
            squareArray[(column-1) * 8 + (row-1)] = new Square(column, row);
        }
        return squareArray[(column-1) * 8 + (row-1)];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return xSpot == square.xSpot && ySpot == square.ySpot;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xSpot, ySpot);
    }

    public int getX(){
        return this.xSpot;
    }

    public int getY(){
        return this.ySpot;
    }

    public void setEmpty(){
        this.squareState = EMPTY;
    }

    public void setOccupied(){
        this.squareState = OCCUPIED;
    }

    public boolean isOccupied(){
        return this.squareState == OCCUPIED;
    }

    public boolean isEmpty(){
        return this.squareState == EMPTY;
    }

    @Override
    public String toString(){
        return "(" + getX() + ", " + getY() + ")";
    }
}