package pieces;


import board.CheckerBoard;
import board.Square;
import general.Alliance;

public class Bishop extends SlidingPiece implements IDiagonalMover{

    public Bishop(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board ,alliance, initialLocation, type);
        this.xDir = new int[]{1, -1, -1, 1};
        this.yDir = new int[]{1, 1, -1, -1};
    }

    @Override
    public void createXRaySquareSets() {
        getXRaySquareSet().addAll(PieceUtils.diagonalSquareSearch(this));
    }
}
