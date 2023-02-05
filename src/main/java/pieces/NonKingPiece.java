package pieces;

import board.CheckerBoard;
import board.Square;
import general.Alliance;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import static pieces.PieceType.KING;


public abstract class NonKingPiece extends ChessPiece{

    protected boolean isPinned;
    protected LinkedHashSet<Square> pinningLine;
    NonKingPiece(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
        this.pinningLine = new LinkedHashSet<>();
        this.isPinned = false;
    }

    @Override
    public void updateSquareSets(){
        clearSquareSets();
        generateSquareSet();
        keepKingSafe();
    }

    public void keepKingSafe(){
        if (isPinned) {
            getValidSquareSet().retainAll(pinningLine);
            isPinned = false;
        }
        King allyKing = (King) getAllyPieces().stream().filter(piece -> piece.getType() == KING).collect(Collectors.toList()).get(0);
        if (allyKing.isUnderCheck())
            getValidSquareSet().retainAll(allyKing.getRescueSquares());
        if (allyKing.isUnderDoubleCheck())
            getValidSquareSet().clear();
    }
}
