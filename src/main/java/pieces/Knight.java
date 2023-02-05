package pieces;

import board.CheckerBoard;
import board.Square;
import general.Alliance;

public class Knight extends NonKingPiece {

    public Knight(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
    }

    @Override
    public void generateSquareSet(){
        for (Square square : PieceUtils.knightStyleSquareSearch(this)) {
            if (square.isEmpty() || (square.isOccupied() && isEnemyOf(getBoard().getPieceAt(square))))
                getValidSquareSet().add(square);
            else getProtectedSquareSet().add(square);
        }
    }

}
