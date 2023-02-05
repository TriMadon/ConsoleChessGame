package board;

import pieces.ChessPiece;
import static general.Alliance.*;

import java.util.*;

public class ChessBoard implements CheckerBoard<ChessPiece> {
    private final LinkedHashMap<Square, ChessPiece> squareMap;
    private final List<ChessPiece> graveYard;
    private final List<ChessPiece> whitePieces;
    private final List<ChessPiece> blackPieces;

    public ChessBoard() {
        this.squareMap = new LinkedHashMap<>();
        this.graveYard = new ArrayList<>();
        this.whitePieces = new ArrayList<>();
        this.blackPieces = new ArrayList<>();
    }

    public ChessPiece getPieceAt(Square square){
        return getSquareMap().get(square);
    }

    public List<ChessPiece> getBlackPieces() {
        return this.blackPieces;
    }

    public List<ChessPiece> getWhitePieces() {
        return this.whitePieces;
    }

    @Override
    public List<ChessPiece> getAllPieces(){
        List<ChessPiece> allPieces = new ArrayList<>();
        allPieces.addAll(blackPieces);
        allPieces.addAll(whitePieces);
        return allPieces;
    }

    @Override
    public Map<Square, ChessPiece> getSquareMap() {
        return this.squareMap;
    }

    public void addPieceToBoard(ChessPiece piece){
        getSquareMap().put(piece.getLocation(), piece);
        if (piece.getAlliance() == WHITE)
            getWhitePieces().add(piece);
        else
            getBlackPieces().add(piece);
    }

    @Override
    public void updatePieceLocation(ChessPiece piece, Square newLocation){
        if (newLocation.isOccupied())
            addPieceToGraveYard(getPieceAt(newLocation));
        getSquareMap().put(newLocation, piece);
        getSquareMap().put(piece.getLocation(), null);
        piece.getLocation().setEmpty();
        newLocation.setOccupied();
    }

    public void addPieceToGraveYard(ChessPiece piece){
        getGraveYard().add(piece);
        if (piece.getAlliance() == WHITE)
            getWhitePieces().remove(piece);
        else
            getBlackPieces().remove(piece);
    }

    public List<ChessPiece> getGraveYard(){
        return this.graveYard;
    }

    public void updateSquareStates(){
        for (Square key : getSquareMap().keySet()) {
            if (getSquareMap().get(key) != null)
                key.setOccupied();
            else key.setEmpty();
        }
    }
}
