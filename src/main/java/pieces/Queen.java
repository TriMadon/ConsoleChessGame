package pieces;

import board.CheckerBoard;
import board.Square;
import general.Alliance;

public class Queen extends SlidingPiece implements IDiagonalMover, IStraightMover{

    public Queen(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
        this.xDir = new int[]{1, 1, 0, -1, -1, -1, 0, 1};
        this.yDir = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
    }

    @Override
    public void createXRaySquareSets() {
        getXRaySquareSet().addAll(PieceUtils.plusShapedSquareSearch(this));
        getXRaySquareSet().addAll(PieceUtils.diagonalSquareSearch(this));
    }
}
