package pieces;
import game.Move;

public interface IMovablePiece {

    void execute(Move move);

    void updateSquareSets();
}
