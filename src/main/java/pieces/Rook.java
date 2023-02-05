package pieces;

import board.CheckerBoard;
import board.Square;
import game.Move;
import general.Alliance;

public class Rook extends SlidingPiece implements IStraightMover{
    private int moveCount;

    public Rook(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
        this.moveCount = 0;
        xDir = new int[]{1, 0, -1, 0};
        yDir = new int[]{0, 1, 0, -1};
    }

    @Override
    public void createXRaySquareSets() {
        getXRaySquareSet().addAll(PieceUtils.plusShapedSquareSearch(this));
    }

    @Override
    public void execute(Move move){
        Square target = move.getTarget();
        occupy(target);
        incrementMove();
    }

    public void incrementMove(){
        this.moveCount++;
    }

    public boolean firstMoveIsDone() {
        return this.moveCount >= 1;
    }

}
