package pieces;

import board.Square;
import static general.Alliance.*;

import java.util.ArrayList;
import java.util.List;

public final class PieceUtils {

    private PieceUtils(){
        throw new AssertionError();
    }

    public static List<Square> knightStyleSquareSearch(ChessPiece pieceToSearchAround){
        int[] xDir = new int[]{2, 1, -1, -2, -2, -1, 1, 2};
        int[] yDir = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
        return directedSquareSearch(xDir, yDir, pieceToSearchAround);
    }

    public static List<Square> pawnStyleSquareSearch(ChessPiece pieceToSearchAround){
        int vertDir = pieceToSearchAround.getAlliance() == WHITE ? 1 : -1;
        int[] xDir = new int[]{1, -1};
        int[] yDir = new int[]{vertDir, vertDir};
        return directedSquareSearch(xDir, yDir, pieceToSearchAround);
    }

    public static List<Square> surroundingSquareSearch(ChessPiece pieceToSearchAround){
        int[] xDir = new int[]{1, 1, 0, -1, -1, -1, 0, 1};
        int[] yDir = new int[]{0, 1, 1, 1, 0, -1, -1, -1};
        return directedSquareSearch(xDir, yDir, pieceToSearchAround);
    }

    public static List<Square> diagonalSquareSearch(ChessPiece pieceToSearchAround){
        int[] xDir = new int[]{1, -1, -1, 1};
        int[] yDir = new int[]{1, 1, -1, -1};
        return directedLinearSquareSearch(xDir, yDir, pieceToSearchAround);
    }

    public static List<Square> plusShapedSquareSearch(ChessPiece pieceToSearchAround){
        int[] xDir = new int[]{1, 0, -1, 0};
        int[] yDir = new int[]{0, 1, 0, -1};
        return directedLinearSquareSearch(xDir, yDir, pieceToSearchAround);
    }

    public static List<Square> directedSquareSearch(int[] xDir, int[] yDir, ChessPiece pieceToSearchAround){
        List<Square> scannedSquares = new ArrayList<>();
        for (int i = 0; i < yDir.length; i++) {
            int xSpot = pieceToSearchAround.getX() + xDir[i];
            int ySpot = pieceToSearchAround.getY() + yDir[i];
            if (xSpot > 8 || xSpot < 1 || ySpot > 8 || ySpot < 1)
                continue;
            Square target = Square.getSquare(xSpot, ySpot);
            scannedSquares.add(target);
        }
        return scannedSquares;
    }

    public static List<Square> directedLinearSquareSearch(int[] xDir, int[] yDir, ChessPiece piece){
        List<Square> scannedSquares = new ArrayList<>();
        for (int i = 0; i < yDir.length; i++) {
            scannedSquares.addAll(linearSquareSearch(xDir[i], yDir[i], piece));
        }
        return scannedSquares;
    }

    public static List<Square> linearSquareSearch(int xDir, int yDir, ChessPiece piece){
        List<Square> scannedSquares = new ArrayList<>();
        int j = 1;
        int xDisplace  = j*xDir;
        int yDisplace = j*yDir;
        while (piece.getX() +xDisplace <= 8 && piece.getX() +xDisplace >= 1 && piece.getY() +yDisplace <= 8 && piece.getY() +yDisplace >= 1){
            Square target = piece.getSquareByDirection(xDisplace, yDisplace);
            scannedSquares.add(target);
            j++;
            xDisplace = j*xDir;
            yDisplace = j*yDir;
        }
        return scannedSquares;
    }

    public static List<Square> linearSquareSearchUntilBlocked(int xDir, int yDir, ChessPiece piece){
        List<Square> scannedSquares = new ArrayList<>();
        int j = 1;
        int xDisplace  = j*xDir;
        int yDisplace = j*yDir;
        while (piece.getX() +xDisplace <= 8 && piece.getX() +xDisplace >= 1 && piece.getY() +yDisplace <= 8 && piece.getY() +yDisplace >= 1){
            Square target = piece.getSquareByDirection(xDisplace, yDisplace);
            scannedSquares.add(target);
            if (target.isOccupied())
                break;
            j++;
            xDisplace = j*xDir;
            yDisplace = j*yDir;
        }
        return scannedSquares;
    }

}
