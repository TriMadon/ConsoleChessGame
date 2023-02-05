package pieces;


import board.CheckerBoard;
import board.Square;
import game.Move;
import general.IDistinctive;
import general.IOpponent;
import general.Alliance;

import java.util.*;

public abstract class ChessPiece implements IOpponent, IMovablePiece, IDistinctive {
    private final Alliance alliance;
    private final PieceType type;
    private final String id;
    private final CheckerBoard<ChessPiece> board;
    private Square location;
    private final LinkedHashSet<Square> validSquareSet;
    private final LinkedHashSet<Square> protectedSquares;
    protected int[] xDir;
    protected int[] yDir;

    ChessPiece(CheckerBoard<ChessPiece> board, Alliance alliance, Square initialLocation, PieceType type){
        this.board = board;
        this.alliance = alliance;
        this.location = initialLocation;
        this.type = type;
        this.id = generateID();
        this.validSquareSet = new LinkedHashSet<>();
        this.protectedSquares = new LinkedHashSet<>();
        occupy(initialLocation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return Objects.equals(id, piece.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String generateID(){
        String color = getAlliance().name().substring(0,1);
        String pieceType = getType().name().substring(0,2);
        int count = 1;
        for (ChessPiece piece : getAllyPieces())
            if (piece.getType() == this.type)
                count++;
        return color + "_" + pieceType + count;
    }

    @Override
    public String toString(){
        return this.id;
    }

    public void updateSquareSets(){
        clearSquareSets();
        generateSquareSet();
    }

    public void clearSquareSets(){
        getValidSquareSet().clear();
        getProtectedSquareSet().clear();
    }

    public abstract void generateSquareSet();

    public Square getLocation() {
        return this.location;
    }

    public Square getSquareByDirection(int byX, int byY){
        return Square.getSquare(getX() + byX, getY() + byY);
    }

    public ChessPiece getPieceByDirection(int byX, int byY){
        return board.getPieceAt(getSquareByDirection(byX, byY));
    }

    @Override
    public boolean isEnemyOf(IOpponent other){
        return this.alliance != other.getAlliance();
    }

    @Override
    public boolean isAllyOf(IOpponent other) { return  this.alliance == other.getAlliance(); }

    @Override
    public void execute(Move move){
        Square target = move.getTarget();
        occupy(target);
    }

    public void occupy(Square target){
        board.updatePieceLocation(this, target);
        setLocation(target);
    }

    public void setLocation(Square location) {
        this.location = location;
    }

    public Alliance getAlliance() {
        return alliance;
    }

    public PieceType getType() {
        return type;
    }

    public CheckerBoard<ChessPiece> getBoard(){
        return this.board;
    }

    public Set<Square> getValidSquareSet() {
        return this.validSquareSet;
    }

    public Set<Square> getProtectedSquareSet() {
        return this.protectedSquares;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    public List<ChessPiece> getOpponentPieces() {
        List<ChessPiece> opponentPieces = getBoard().getAllPieces();
        opponentPieces.removeIf(piece -> piece.getAlliance() == getAlliance());
        return new ArrayList<>(opponentPieces);
    }

    public List<ChessPiece> getAllyPieces() {
        List<ChessPiece> allyPieces = getBoard().getAllPieces();
        allyPieces.removeIf(piece -> piece.getAlliance() != getAlliance());
        return new ArrayList<>(allyPieces);
    }
}