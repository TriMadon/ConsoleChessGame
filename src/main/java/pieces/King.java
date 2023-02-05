package pieces;

import static pieces.PieceType.*;
import static pieces.KingState.*;

import board.CheckerBoard;
import board.Square;
import game.Move;
import general.Alliance;

import java.util.*;

import static java.lang.Math.abs;

public class King extends ChessPiece {
    private int moveCount;
    private KingState kingState;

    private int numOfChecks;
    private final LinkedHashSet<Square> unsafeSquares;
    private final LinkedHashSet<Square> rescueSquares;

    public King(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
        this.moveCount = 0;
        this.kingState = SAFE;
        this.rescueSquares = new LinkedHashSet<>();
        this.unsafeSquares = new LinkedHashSet<>();
        this.xDir = new int[]{1, 1, 0, -1, -1, -1, 0, 1};
        this.yDir = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
    }

    @Override
    public void execute(Move move) {
        Square origin = move.getOrigin();
        Square target = move.getTarget();
        if (target.getX() - origin.getX() == 2 && isAbleToShortCastle()) executeShortCastle();
        if (target.getX() - origin.getX() == -2 && isAbleToLongCastle()) executeLongCastle();
        occupy(target);
        incrementMove();
    }

    @Override
    public void generateSquareSet() {
        for (Square square: PieceUtils.surroundingSquareSearch(this)) {
            if (isUnsafe(square))
                continue;
            if (square.isOccupied() && isAllyOf(getBoard().getPieceAt(square)))
                getProtectedSquareSet().add(square);
            else getValidSquareSet().add(square);
        }
        if (isAbleToShortCastle())
            getValidSquareSet().add(getSquareByDirection(2,0));
        if (isAbleToLongCastle())
            getValidSquareSet().add(getSquareByDirection(-2,0));
    }

    private boolean isUnsafe(Square square){
        return unsafeSquares.contains(square);
    }

    public Set<Square> getUnsafeSquares() {
        return unsafeSquares;
    }

    private boolean isAbleToShortCastle(){
        if (firstMoveIsDone())
            return false;
        Square rookSquare = getSquareByDirection(3, 0);
        if (rookSquare.isEmpty())
            return false;
        ChessPiece pieceAtRookSquare = getBoard().getPieceAt(rookSquare);
        if (pieceAtRookSquare.getType() != ROOK)
            return false;
        if (isEnemyOf(pieceAtRookSquare))
            return false;
        if (((Rook) pieceAtRookSquare).firstMoveIsDone())
            return false;
        if (getSquareByDirection(1,0).isOccupied() || getSquareByDirection(2,0).isOccupied())
            return false;
        if (isUnsafe(getSquareByDirection(1,0)) || isUnsafe(getSquareByDirection(2,0)))
            return false;
        return !isUnderCheck();
    }

    private void executeShortCastle() {
        ChessPiece rightRook = getPieceByDirection(3, 0);
        Square rookOldLocation = rightRook.getLocation();
        Square rookNewLocation = Square.getSquare(6, rookOldLocation.getY());
        rightRook.occupy(rookNewLocation);
    }

    private boolean isAbleToLongCastle(){
        if (firstMoveIsDone())
            return false;
        Square rookSquare = getSquareByDirection(-4, 0);
        if (rookSquare.isEmpty())
            return false;
        ChessPiece pieceAtRookSquare = getBoard().getPieceAt(rookSquare);
        if (pieceAtRookSquare.getType() != ROOK)
            return false;
        if (isEnemyOf(pieceAtRookSquare))
            return false;
        if (((Rook) pieceAtRookSquare).firstMoveIsDone())
            return false;
        if (getSquareByDirection(-1,0).isOccupied() || getSquareByDirection(-2,0).isOccupied())
            return false;
        if (isUnsafe(getSquareByDirection(-1,0)) || isUnsafe(getSquareByDirection(-2,0)))
            return false;
        return !isUnderCheck();
    }

    private void executeLongCastle() {
        ChessPiece leftRook = getPieceByDirection(-4, 0);
        Square rookOldLocation = leftRook.getLocation();
        Square rookNewLocation = Square.getSquare(4, rookOldLocation.getY());
        leftRook.occupy(rookNewLocation);
    }

    public Set<Square> getRescueSquares() {
        return rescueSquares;
    }

    public void checkSurroundings(){
        getUnsafeSquares().clear();
        getRescueSquares().clear();
        getUnsafeSquares().addAll(searchUnsafeSquares());
        numOfChecks = 0;
        checkSlidingEnemies();
        checkKnightEnemies();
        checkPawnEnemies();
        if (numOfChecks == 2)
            kingState = DOUBLE_CHECKED;
        else if (numOfChecks == 1)
            kingState = CHECKED;
        else kingState = SAFE;
    }

    private List<Square> searchUnsafeSquares(){
        List<ChessPiece> opponentPieces = getOpponentPieces();
        List<Square> squares = new ArrayList<>();
        for (ChessPiece p : opponentPieces) {
            squares.addAll(p.getValidSquareSet());
            squares.addAll(p.getProtectedSquareSet());
        }
        return squares;
    }

    private void checkSlidingEnemies() {
        for (int i = 0; i < 8; i++) {
            List<SlidingPiece> slidingEnemies = new ArrayList<>();
            List<ChessPiece> nonSlidingEnemies = new ArrayList<>();
            List<ChessPiece> allies = new ArrayList<>();
            List<Square> squaresOnTheLine = PieceUtils.linearSquareSearch(xDir[i], yDir[i], this);
            List<ChessPiece> piecesOnTheLine = new ArrayList<>();

            for (Square square : squaresOnTheLine){
                if (square.isOccupied()) {
                    ChessPiece piece = getBoard().getPieceAt(square);
                    piecesOnTheLine.add(piece);
                    if (isEnemyOf(piece)) {
                        boolean enemyMovesDiagonally = abs(xDir[i]) == abs(yDir[i]) && piece instanceof IDiagonalMover;
                        boolean enemyMovesStraight = (xDir[i] == 0 || yDir[i]==0) && piece instanceof IStraightMover;
                        if (enemyMovesDiagonally || enemyMovesStraight)
                            slidingEnemies.add((SlidingPiece) piece);
                        else nonSlidingEnemies.add(piece);
                    } else allies.add(piece);
                }
            }

            if (!slidingEnemies.isEmpty()) {
                if (allies.isEmpty() && nonSlidingEnemies.isEmpty()) {
                    numOfChecks++;
                    SlidingPiece checkingEnemy = slidingEnemies.get(0);
                    int k = squaresOnTheLine.size() - 1;
                    while(!squaresOnTheLine.get(k).equals(checkingEnemy.getLocation())){
                        squaresOnTheLine.remove(k);
                        k--;
                    }
                    getUnsafeSquares().addAll(checkingEnemy.getXRayLineFromDirection(-xDir[i], -yDir[i]));
                    for (ChessPiece ally : getAllyPieces()) {
                        if (ally.equals(this)) continue;
                        rescueSquares.addAll(squaresOnTheLine);
                    }
                }
                else if (!allies.isEmpty() && nonSlidingEnemies.isEmpty()) {
                    boolean slidingEnemyIsBlockedByOneAlly = allies.size() == 1 && piecesOnTheLine.indexOf(allies.get(0)) < piecesOnTheLine.indexOf(slidingEnemies.get(0));
                    if (slidingEnemyIsBlockedByOneAlly) {
                        pinAllyToLine((NonKingPiece) allies.get(0), squaresOnTheLine);
                    }
                }
            }
        }
    }

    private void pinAllyToLine(NonKingPiece piece, List<Square> squareList) {
        piece.isPinned = true;
        piece.pinningLine.addAll(squareList);
    }

    private void checkKnightEnemies() {
        for (Square square: PieceUtils.knightStyleSquareSearch(this)) {
            if (square.isEmpty()) continue;
            ChessPiece piece = getBoard().getPieceAt(square);
            if (isEnemyOf(piece) && piece.getType() == KNIGHT) {
                numOfChecks++;
                rescueSquares.add(square);
            }
        }
    }

    private void checkPawnEnemies() {
        for (Square square: PieceUtils.pawnStyleSquareSearch(this)) {
            if (square.isEmpty()) continue;
            ChessPiece piece = getBoard().getPieceAt(square);
            if (isEnemyOf(piece) && piece.getType() == PAWN) {
                numOfChecks++;
                rescueSquares.add(square);
            }
        }
    }

    public boolean firstMoveIsDone(){
        return this.moveCount >= 1;
    }

    public void incrementMove(){
        this.moveCount++;
    }

    public boolean isUnderCheck() {
        return kingState == CHECKED;
    }

    public boolean isUnderDoubleCheck(){
        return kingState == DOUBLE_CHECKED;
    }

    public boolean isCheckMated() {
        if (isUnderDoubleCheck() && getValidSquareSet().isEmpty())
            return true;
        return isUnderCheck() && getValidSquareSet().isEmpty() && allAlliesHaveNoValidMoves();
    }

    public boolean isStaleMated() {
        return !isUnderCheck() && !isUnderDoubleCheck() && getValidSquareSet().isEmpty() && allAlliesHaveNoValidMoves();
    }

    private boolean allAlliesHaveNoValidMoves() {
        for (ChessPiece piece: getAllyPieces()) {
            if (!piece.getValidSquareSet().isEmpty())
                return false;
        }
        return true;
    }
}
