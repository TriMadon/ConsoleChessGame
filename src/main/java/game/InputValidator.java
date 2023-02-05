package game;

import board.CheckerBoard;
import board.Square;
import general.IOpponent;
import pieces.ChessPiece;
import player.ChessPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class InputValidator {

    private InputValidator() {
        throw new AssertionError();
    }

    public static void validateMove(String input, ChessPlayer currentPlayer, CheckerBoard<ChessPiece> board) {
        validateRegex(input);
        Move parsedMove = parseMove(input);
        parsedMove.setPlayer(currentPlayer);
        Square origin = parsedMove.getOrigin();
        Square target = parsedMove.getTarget();
        validateOriginIsNotEmpty(origin);
        validatePieceMatchesPlayerAlliance(board.getPieceAt(origin), currentPlayer);
        validateLegalPieceMove(board.getPieceAt(origin), target);
    }

    private static void validateRegex(String move){
        String s = "\\s*(?i)(move)\\s+(?i)[A-H][1-8]\\s+(?i)[A-H][1-8]\\s*";
        Pattern p = Pattern.compile(s);
        Matcher m = p.matcher(move);
        if (!m.matches())
            throw new IllegalArgumentException("Incorrect input format or out of bounds of the board");
    }

    public static Move parseMove(String move){
        String[] inputReadings = move.trim().split("\\s+");
        String[] from = inputReadings[1].split("");
        String[] to = inputReadings[2].split("");
        int originX = from[0].charAt(0) - 96;
        int originY = Integer.parseInt(from[1]);
        int targetX = to[0].charAt(0) - 96;
        int targetY = Integer.parseInt(to[1]);

        Square origin = Square.getSquare(originX, originY);
        Square target = Square.getSquare(targetX, targetY);

        return new Move(origin, target);
    }

    private static void validateOriginIsNotEmpty(Square origin){
        if (origin.isEmpty())
            throw new IllegalArgumentException("There is no piece to move.");
    }

    private static void validatePieceMatchesPlayerAlliance(IOpponent piece, IOpponent chessPlayer){
        if (piece.isEnemyOf(chessPlayer))
            throw new IllegalArgumentException("Cannot move opponent's piece.");
    }

    private static void validateLegalPieceMove(ChessPiece piece, Square target){
        piece.updateSquareSets();
        if (!piece.getValidSquareSet().contains(target))
            throw new IllegalArgumentException("Invalid target square for this piece.");
    }
}
