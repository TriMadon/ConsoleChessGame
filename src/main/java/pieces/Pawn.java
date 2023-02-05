package pieces;

import static pieces.PieceType.*;
import static general.Alliance.*;

import board.CheckerBoard;
import board.PieceFactory;
import board.Square;
import game.Move;
import general.Alliance;

import static java.lang.Math.abs;

public class Pawn extends NonKingPiece {
    private int moveCount;
    private final Square initialLocation;
    private Move lastPawnMove;
    private final int vertDir;

    public Pawn(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type) {
        super(board, alliance, initialLocation, type);
        this.initialLocation = initialLocation;
        this.moveCount = 0;
        this.vertDir = getAlliance() == WHITE? 1 : -1;
    }

    @Override
    public void execute(Move move) {
        setLastPawnMove(move);
        Square origin = move.getOrigin();
        Square target = move.getTarget();
        int xDistance = target.getX() - origin.getX();
        int yDistance = target.getY() - origin.getY();
        if (abs(xDistance) == 1 && abs(yDistance) == 1 && canEnPassant(xDistance))
                executeEnPassant(xDistance);
        occupy(target);
        incrementMove();
        if (abs(getY() - getInitialLocation().getY()) == 6)
            promote();
    }

    @Override
    public void generateSquareSet() {
        for (Square square : PieceUtils.pawnStyleSquareSearch(this)) {
            boolean thereIsAnEnemyOnTheSquare = square.isOccupied() && isEnemyOf(getBoard().getPieceAt(square));
            boolean thereIsAnAllyOnTheSquare = square.isOccupied() && isAllyOf(getBoard().getPieceAt(square));
            boolean isPossibleToEnPassant = square.isEmpty() && canEnPassant(square.getX() - getX());
            if (thereIsAnEnemyOnTheSquare || isPossibleToEnPassant)
                getValidSquareSet().add(square);
            else if (thereIsAnAllyOnTheSquare || square.isEmpty())
                getProtectedSquareSet().add(square);
        }

        boolean frontIsEmpty = getSquareByDirection(0, vertDir).isEmpty();
        if (frontIsEmpty)
            getValidSquareSet().add(getSquareByDirection(0, vertDir));
        if (canDash())
            getValidSquareSet().add(getSquareByDirection(0, vertDir * 2));
    }

    private boolean canDash() {
        if (firstMoveIsDone())
            return false;
        Square frontSquare = getSquareByDirection(0, vertDir);
        Square secondFrontSquare = getSquareByDirection(0, vertDir*2);
        return frontSquare.isEmpty() && secondFrontSquare.isEmpty();
    }

    public void setLastPawnMove(Move lastPawnMove) {
        this.lastPawnMove = lastPawnMove;
    }

    public Move getLastPawnMove() {
        return lastPawnMove;
    }

    private boolean canEnPassant(int horiDir) {
        if (abs(getLocation().getY() - getInitialLocation().getY()) != 3)
            return false;
        Square adjacentSquare = getSquareByDirection(horiDir,0);
        if (adjacentSquare.isEmpty())
            return false;
        ChessPiece adjacentPiece = getBoard().getPieceAt(adjacentSquare);
        if (adjacentPiece.getType() != PAWN)
            return false;
        return ((Pawn) adjacentPiece).canBeCapturedWithEnPassant();
    }

    public boolean canBeCapturedWithEnPassant(){
        Move playersLastMove = getLastPawnMove().getPlayer().getLastPlayerMove();
        boolean pawnHasJustMoved = getLastPawnMove().equals(playersLastMove);
        if (!pawnHasJustMoved) return false;
        return abs(playersLastMove.getTarget().getY() - playersLastMove.getOrigin().getY()) == 2;
    }

    private void executeEnPassant(int horiDir) {
        ChessPiece adjacentPawn = getPieceByDirection(horiDir, 0);
        adjacentPawn.occupy(adjacentPawn.getSquareByDirection(0, vertDir));
    }

    private void promote() {
        ChessPiece newQueen = PieceFactory.createPiece(getBoard(), QUEEN, getAlliance(), getLocation());
        getBoard().addPieceToBoard(newQueen);
        getBoard().updateSquareStates();
        updateSquareSets();
    }

    public boolean firstMoveIsDone() {
        return this.moveCount >= 1;
    }

    public Square getInitialLocation(){
        return this.initialLocation;
    }

    public void incrementMove(){
        this.moveCount++;
    }
}
