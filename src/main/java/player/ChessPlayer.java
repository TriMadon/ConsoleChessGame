package player;

import board.CheckerBoard;
import game.Move;
import general.Alliance;
import general.IDistinctive;
import general.IOpponent;
import pieces.ChessPiece;
import pieces.King;

import java.util.List;
import java.util.Objects;

import static pieces.PieceType.KING;


public class ChessPlayer implements IPlayer, IDistinctive, IOpponent {
    private final String name;
    private final Alliance alliance;
    private final CheckerBoard<ChessPiece> board;
    private final int hashCode;
    private Move lastMove;

    public ChessPlayer(String name, Alliance alliance, CheckerBoard<ChessPiece> board) {
        this.board = board;
        this.name = name;
        this.alliance = alliance;
        this.hashCode = Objects.hash(name, alliance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ChessPlayer that = (ChessPlayer) o;
        return name.equals(that.name) && alliance.equals(that.alliance);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String generateID(){
        return name + "_" + alliance;
    }

    @Override
    public void makeAMove(Move move) {
        move.setPlayer(this);
        ChessPiece movedPiece = board.getPieceAt(move.getOrigin());
        movedPiece.execute(move);
        setLastMove(move);
    }

    public boolean isCheckMated(){
        return getKing().isCheckMated();
    }

    public boolean isStaleMated(){
        return getKing().isStaleMated();
    }

    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    public Move getLastPlayerMove() {
        return lastMove;
    }

    @Override
    public Alliance getAlliance() {
        return this.alliance;
    }

    @Override
    public boolean isEnemyOf(IOpponent other) {
        return this.alliance != other.getAlliance();
    }

    @Override
    public boolean isAllyOf(IOpponent other) {
        return this.alliance == other.getAlliance();
    }

    @Override
    public String toString(){
        return "[" + this.alliance + "] " + this.name ;
    }

    public King getKing() {
        List<ChessPiece> alliedPieces = board.getAllPieces();
        alliedPieces.removeIf(piece -> piece.getAlliance() != alliance || piece.getType() != KING);
        return ((King) alliedPieces.get(0));
    }
}
