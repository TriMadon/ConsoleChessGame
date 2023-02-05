package board;

import java.util.List;
import java.util.Map;

public interface CheckerBoard<P> {

    void addPieceToBoard(P piece);
    List<P> getAllPieces();
    void updatePieceLocation(P piece, Square square);

    P getPieceAt(Square squareByDirection);

    Map <Square,P> getSquareMap();

    void updateSquareStates();
}
