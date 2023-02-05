package board;

import pieces.*;
import general.Alliance;

public class PieceFactory {

    private PieceFactory(){
        throw new AssertionError();
    }

    public static ChessPiece createPiece(CheckerBoard<ChessPiece> board, PieceType type, Alliance alliance, Square location){
        switch (type){
            case KING: return new King(board, alliance, location, type);
            case QUEEN: return new Queen(board, alliance, location, type);
            case PAWN: return new Pawn(board, alliance, location, type);
            case ROOK: return new Rook(board, alliance, location, type);
            case BISHOP: return new Bishop(board, alliance, location, type);
            case KNIGHT: return new Knight(board, alliance, location, type);
            default: return null;
        }
    }
}
