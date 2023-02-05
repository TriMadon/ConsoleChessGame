package board;

import pieces.ChessPiece;
import static pieces.PieceType.*;
import general.Alliance;
import static general.Alliance.*;
import pieces.PieceType;

public final class BoardSetter {

    private BoardSetter(){
        throw new AssertionError();
    }

    public static void initialize(CheckerBoard<ChessPiece> board){
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                Square square = Square.getSquare(j, i);
                board.getSquareMap().put(square, null);
            }
        }

        Alliance[] alliances = {WHITE, BLACK};
        int[] kingRows = {1 , 8};
        int[] pawnRows = {2 , 7};
        PieceType[] endRowPieces = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};

        for (int i = 0; i < 2; i++) {
            for (int j = 1; j <= 8; j++) {
                Square pieceLocation = Square.getSquare(j, kingRows[i]);
                Square pawnLocation = Square.getSquare(j, pawnRows[i]);
                ChessPiece piece = PieceFactory.createPiece(board, endRowPieces[j-1], alliances[i], pieceLocation);
                ChessPiece pawn = PieceFactory.createPiece(board, PAWN, alliances[i], pawnLocation);
                board.addPieceToBoard(piece);
                board.addPieceToBoard(pawn);
                board.updateSquareStates();
            }
        }
    }
}
