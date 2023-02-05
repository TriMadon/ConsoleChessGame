package pieces;

import board.CheckerBoard;
import board.Square;
import general.Alliance;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public abstract class SlidingPiece extends NonKingPiece{

    private final LinkedHashSet<Square> xRaySquareSet;

    SlidingPiece(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
        this.xRaySquareSet = new LinkedHashSet<>();
    }

    public abstract void createXRaySquareSets();

    @Override
    public void clearSquareSets(){
        getXRaySquareSet().clear();
        getValidSquareSet().clear();
        getProtectedSquareSet().clear();
    }

    @Override
    public void generateSquareSet(){
        createXRaySquareSets();
        for (int i = 0; i < yDir.length; i++) {
            List<Square> squaresUntilBlocked = PieceUtils.linearSquareSearchUntilBlocked(xDir[i], yDir[i], this);
            if (squaresUntilBlocked.isEmpty())
                continue;
            getValidSquareSet().addAll(squaresUntilBlocked.subList(0, squaresUntilBlocked.size() - 1));
            Square lastSquareOnTheLine = squaresUntilBlocked.get(squaresUntilBlocked.size() - 1);
            if (lastSquareOnTheLine.isEmpty()) {
                getValidSquareSet().add(lastSquareOnTheLine);
                continue;
            }
            ChessPiece blockingPiece = getBoard().getPieceAt(lastSquareOnTheLine);
            if (isEnemyOf(blockingPiece)) getValidSquareSet().add(lastSquareOnTheLine);
            else getProtectedSquareSet().add(lastSquareOnTheLine);
        }
    }

    public Set<Square> getXRaySquareSet() {
        return xRaySquareSet;
    }

    public List<Square> getXRayLineFromDirection(int xDir, int yDir){
        return PieceUtils.linearSquareSearch(xDir, yDir, this);
    }

}
